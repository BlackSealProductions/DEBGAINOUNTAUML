package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.RoundedButton;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class SimulationScreen implements View_t {

    private JPanel panel;
    
    // --- UPDATED INPUTS ---
    public JTextField startDateField; 
    public JTextField daysField;      
    
    public JTextArea logArea;
    public RoundedButton runBtn;
    public RoundedButton backBtn;
    
    public JLabel currentSimDateLabel;
    public JLabel totalTxLabel;
    public JLabel volumeLabel;

    public SimulationScreen() {
        init();
    }

    @Override
    public void init() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.decode("#C2E5FF")); 

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        backBtn = new RoundedButton("Back", 15);
        backBtn.setBackground(Color.decode("#D82F4B"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(80, 40));
        
        JLabel title = new JLabel("Time-Step Simulator", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 26));

        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(title, BorderLayout.CENTER);
        topBar.add(Box.createHorizontalStrut(80), BorderLayout.EAST); 
        
        panel.add(topBar, BorderLayout.NORTH);

        // --- CENTER CONTENT ---
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        // LEFT: Configuration
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setBackground(Color.WHITE);
        configPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        configPanel.setPreferredSize(new Dimension(300, 0));

        JLabel configTitle = new JLabel("CONFIGURATION");
        configTitle.setFont(new Font("Arial", Font.BOLD, 18));
        configTitle.setForeground(Color.decode("#D82F4B"));
        configTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Default: Tomorrow, Run for 30 days
        startDateField = new JTextField(LocalDate.now().plusDays(1).toString());
        daysField = new JTextField("30");
        
        runBtn = new RoundedButton("START TIMELINE", 10);
        runBtn.setBackground(Color.decode("#D82F4B"));
        runBtn.setForeground(Color.WHITE);
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        runBtn.setMaximumSize(new Dimension(200, 50));

        configPanel.add(configTitle);
        configPanel.add(Box.createVerticalStrut(30));
        configPanel.add(createInputRow("Start Date (YYYY-MM-DD):", startDateField));
        configPanel.add(Box.createVerticalStrut(20));
        configPanel.add(createInputRow("Duration (Days):", daysField));
        configPanel.add(Box.createVerticalStrut(40));
        configPanel.add(runBtn);

        // RIGHT: Log Area
        logArea = new JTextArea();
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Daily Processing Logs"));

        centerPanel.add(configPanel, BorderLayout.WEST);
        centerPanel.add(scroll, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM BAR ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(0, 40, 20, 40));
        
        currentSimDateLabel = createStatLabel("Date: Waiting...");
        totalTxLabel = createStatLabel("Total Tx: 0");
        volumeLabel = createStatLabel("Vol: €0.00");

        statsPanel.add(currentSimDateLabel);
        statsPanel.add(totalTxLabel);
        statsPanel.add(volumeLabel);

        panel.add(statsPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputRow(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(250, 30));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(field, BorderLayout.CENTER);
        return p;
    }
    
    private JLabel createStatLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(Color.WHITE);
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        l.setFont(new Font("Monospaced", Font.BOLD, 16));
        return l;
    }

    public void appendLog(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void setDateLabel(String val) { currentSimDateLabel.setText(val); }
    public void setTotalTx(String val) { totalTxLabel.setText("Total Tx: " + val); }
    public void setVolume(String val) { volumeLabel.setText("Vol: €" + val); }

    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); }
    @Override
    public void hide() { panel.setVisible(false); }
}