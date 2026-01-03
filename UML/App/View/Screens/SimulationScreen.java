package App.View.Screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Scanner;

import App.View.View_t; // Make sure this is imported!

public class SimulationScreen implements View_t {

    private JPanel mainPanel;

    // --- Components ---
    private JTextField botCountField, actionsField;
    private JButton runButton, backButton;
    private JTextArea terminalArea;
    private JLabel totalTxLabel, volumeLabel, errorsLabel;

    // --- Terminal State ---
    private int lastPromptPosition = 0; 
    private File currentDirectory = new File(System.getProperty("user.dir")); 
    
    // --- Styles ---
    private final Color COLOR_BG = Color.decode("#C2E5FF");
    private final Color COLOR_RED = Color.decode("#DC3545");
    private final Color COLOR_DARK = Color.decode("#333333");
    private final Font FONT_TERMINAL = new Font("Consolas", Font.PLAIN, 14); 

    public SimulationScreen() {
        // We leave the constructor empty or minimal
    }

    // --- View_t Interface Method ---
    @Override
    public void init() {
        // We do NOT create a new JFrame here anymore. 
        // We just build the JPanel that AdminMenuCon will display.
        
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel headerLabel = new JLabel("SYSTEM SIMULATION & TERMINAL ACCESS", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        headerLabel.setForeground(COLOR_DARK);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        backButton = new JButton("<-");
        backButton.setBackground(COLOR_RED);
        backButton.setForeground(Color.WHITE);
        headerPanel.add(backButton, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. CENTER (Config LEFT | Real Terminal RIGHT)
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);
        
        // Left Config
        JPanel configPanel = createConfigPanel();
        configPanel.setPreferredSize(new Dimension(300, 0));
        centerPanel.add(configPanel, BorderLayout.WEST);

        // Right Terminal
        JPanel terminalPanel = createRealTerminalPanel();
        centerPanel.add(terminalPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 3. BOTTOM Stats
        mainPanel.add(createStatsPanel(), BorderLayout.SOUTH);

        // Start the terminal prompt
        startTerminalSession();
    }

    // --- CRITICAL: This is what the Controller looks for ---
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void show() {
        if (mainPanel != null) mainPanel.setVisible(true);
    }

    @Override
    public void hide() {
        if (mainPanel != null) mainPanel.setVisible(false);
    }

    // ============================================================
    //      TERMINAL LOGIC
    // ============================================================

    private JPanel createRealTerminalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel title = new JLabel("LIVE ROOT SHELL ACCESS (Type commands here)");
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(title, BorderLayout.NORTH);

        terminalArea = new JTextArea();
        terminalArea.setFont(FONT_TERMINAL);
        terminalArea.setBackground(Color.BLACK);
        terminalArea.setForeground(Color.GREEN);
        terminalArea.setCaretColor(Color.WHITE);
        terminalArea.setEditable(true);
        terminalArea.setMargin(new Insets(10, 10, 10, 10));

        DefaultCaret caret = (DefaultCaret) terminalArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        terminalArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); 
                    executeUserCommand();
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (terminalArea.getCaretPosition() <= lastPromptPosition) {
                        e.consume();
                    }
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
                if (terminalArea.getCaretPosition() < lastPromptPosition) {
                    terminalArea.setCaretPosition(terminalArea.getDocument().getLength());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(terminalArea);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void executeUserCommand() {
        try {
            String fullText = terminalArea.getText();
            // Safety check in case prompt positions get messed up
            if (lastPromptPosition > fullText.length()) lastPromptPosition = fullText.length();
            
            String command = fullText.substring(lastPromptPosition).trim();
            terminalArea.append("\n");

            if (command.isEmpty()) {
                printPrompt();
                return;
            }

            if (command.startsWith("cd ")) {
                String path = command.substring(3).trim();
                File newDir = new File(currentDirectory, path);
                if (!newDir.exists()) newDir = new File(path);
                
                if (newDir.exists() && newDir.isDirectory()) {
                    currentDirectory = newDir.getCanonicalFile();
                } else {
                    terminalArea.append("cd: no such file or directory: " + path + "\n");
                }
                printPrompt();
                return;
            }
            
            if (command.equals("clear") || command.equals("cls")) {
                terminalArea.setText("");
                lastPromptPosition = 0;
                printPrompt();
                return;
            }
            
            // Note: 'exit' now just clears logic or does nothing 
            // because we don't want to close the whole App frame
            if (command.equals("exit")) {
               terminalArea.append("Use the Back button to exit simulation.\n");
               printPrompt();
               return;
            }

            runOSCommand(command);

        } catch (Exception ex) {
            terminalArea.append("Error: " + ex.getMessage() + "\n");
            printPrompt();
        }
    }

    private void runOSCommand(String command) {
        new Thread(() -> {
            try {
                ProcessBuilder pb;
                boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("win");

                if (isWindows) {
                    pb = new ProcessBuilder("cmd.exe", "/c", command);
                } else {
                    pb = new ProcessBuilder("/bin/bash", "-c", command);
                }

                pb.directory(currentDirectory);
                pb.redirectErrorStream(true);
                Process p = pb.start();

                InputStream is = p.getInputStream();
                // Simple byte read to avoid complicated Scanner issues in Thread
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    String outputChunk = new String(buffer, 0, len);
                    SwingUtilities.invokeLater(() -> terminalArea.append(outputChunk));
                }
                p.waitFor();

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> terminalArea.append("Execution Failed: " + e.getMessage() + "\n"));
            } finally {
                SwingUtilities.invokeLater(this::printPrompt);
            }
        }).start();
    }

    private void printPrompt() {
        String prompt = "admin@bank:" + currentDirectory.getName() + "$ ";
        terminalArea.append(prompt);
        terminalArea.setCaretPosition(terminalArea.getDocument().getLength());
        lastPromptPosition = terminalArea.getDocument().getLength();
    }

    private void startTerminalSession() {
        terminalArea.setText("--- BANK OF TUC MAINFRAME [ACCESS GRANTED] ---\n");
        terminalArea.append("OS: " + System.getProperty("os.name") + " | User: " + System.getProperty("user.name") + "\n\n");
        printPrompt();
    }

    // ============================================================
    //      UI PANELS
    // ============================================================

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,5,10,5); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CONFIGURATION");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(COLOR_RED);
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; panel.add(title, gbc);

        gbc.gridwidth=1; gbc.gridy++;
        panel.add(new JLabel("Bot Count:"), gbc);
        botCountField = new JTextField("10"); gbc.gridx=1; panel.add(botCountField, gbc);

        gbc.gridx=0; gbc.gridy++;
        panel.add(new JLabel("Actions:"), gbc);
        actionsField = new JTextField("5"); gbc.gridx=1; panel.add(actionsField, gbc);

        runButton = new JButton("RUN SIMULATION");
        runButton.setBackground(COLOR_RED); runButton.setForeground(Color.WHITE);
        gbc.gridx=0; gbc.gridy++; gbc.gridwidth=2; gbc.insets=new Insets(20,5,5,5);
        panel.add(runButton, gbc);

        gbc.gridy++; gbc.weighty=1.0; panel.add(Box.createVerticalGlue(), gbc);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setOpaque(false); panel.setPreferredSize(new Dimension(0, 100));
        totalTxLabel = createStatCard(panel, "TOTAL TRANSACTIONS", "0");
        volumeLabel = createStatCard(panel, "VOLUME MOVED", "0.00€");
        errorsLabel = createStatCard(panel, "ERRORS / FAILURES", "0");
        return panel;
    }

    private JLabel createStatCard(JPanel parent, String title, String initVal) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE); card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel t = new JLabel(title, SwingConstants.CENTER); t.setFont(new Font("SansSerif",Font.BOLD,12)); t.setForeground(Color.GRAY);
        JLabel v = new JLabel(initVal, SwingConstants.CENTER); v.setFont(new Font("SansSerif",Font.BOLD,24)); v.setForeground(COLOR_RED);
        card.add(t, BorderLayout.NORTH); card.add(v, BorderLayout.CENTER);
        parent.add(card);
        return v;
    }

    // --- GETTERS ---
    // Note: Used JButton explicitly to match this specific design
    public JButton getRunButton() { return runButton; }
    public JButton getBackButton() { return backButton; }
    public String getBotCount() { return botCountField.getText(); }
    public String getActionsCount() { return actionsField.getText(); }
    public void setTotalTx(String val) { totalTxLabel.setText(val); }
    public void setVolume(String val) { volumeLabel.setText(val + "€"); }
    
    // Helper for Controller to log events
    public void appendLog(String msg) {
        SwingUtilities.invokeLater(() -> {
            terminalArea.append("\n[SIM_LOG]: " + msg);
            // Don't print prompt here, just let the log sit
        });
    }
}