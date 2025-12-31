package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;
import javax.swing.*;
import java.awt.*;

public class WithdrawScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JPanel panel = new JPanel();
    private Color blue = Color.decode("#C2E5FF");
    private Color red = Color.decode("#D82F4B");
    
    // Fonts
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);
    Font customFont12 = FontLoader.loadCustomFont(fontPath, 12f);

    private JTextField amountField;
    private RoundedButton confirmBtn, checkBtn, backBtn;
    private JLabel balanceLabel, newBalanceLabel;

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);

        // --- TOP LOGO ---
        JPanel topPanel = new JPanel(null); 
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(wWidth, 200));

        try {
            Image logo = new ImageIcon(getClass().getResource("/App/View/Assets/bankoftuclogo.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logo));
            logoLabel.setBounds(50, 20, 150, 150);
            topPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Logo not found");
        }
        
        panel.add(topPanel, BorderLayout.NORTH);

        // --- CENTER FORM ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Title (Changed for Withdrawal)
        JLabel title = new JLabel("Ανάληψη Χρημάτων");
        title.setFont(customFont40);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        // Current Balance Label
        balanceLabel = new JLabel("Τρέχον Υπόλοιπο: 0€");
        balanceLabel.setFont(customFont20);
        gbc.gridy = 1;
        centerPanel.add(balanceLabel, gbc);

        // Input Field
        amountField = new JTextField("0.00");
        amountField.setPreferredSize(new Dimension(350, 50));
        amountField.setFont(customFont20);
        amountField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        centerPanel.add(amountField, gbc);

        // Calculate Button
        checkBtn = new RoundedButton("Υπολογισμός", 10);
        checkBtn.setFont(customFont12);
        checkBtn.setPreferredSize(new Dimension(150, 30));
        gbc.gridy = 3;
        centerPanel.add(checkBtn, gbc);

        // New Balance Preview
        newBalanceLabel = new JLabel("Νέο Υπόλοιπο: -");
        newBalanceLabel.setFont(customFont12);
        newBalanceLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 30, 0);
        centerPanel.add(newBalanceLabel, gbc);

        // Confirm Button
        confirmBtn = new RoundedButton("Επιβεβαίωση", 15);
        confirmBtn.setBackground(red);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setPreferredSize(new Dimension(250, 60));
        confirmBtn.setFont(customFont20);
        gbc.gridy = 5;
        centerPanel.add(confirmBtn, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);
        
        // --- BACK BUTTON (Kept to match your template) ---
        // If your MainFrame has a universal back button, you can remove this block 
        // to avoid having two buttons.
        backBtn = new RoundedButton("<-", 15);
        backBtn.setBackground(red);
        backBtn.setForeground(Color.WHITE);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        hide();
    }

    // --- GETTERS ---
    public String getAmountInput() { return amountField.getText(); }
    public JButton getCalculateBtn() { return checkBtn; }
    public JButton getConfirmBtn() { return confirmBtn; }
    public JButton getBackBtn() { return backBtn; }

    // --- SETTERS ---
    public void setCurrentBalanceLabel(String text) {
        balanceLabel.setText("Τρέχον Υπόλοιπο: " + text);
    }

    public void setNewBalanceLabel(String text) {
        newBalanceLabel.setText("Νέο Υπόλοιπο: " + text);
    }

    public void clearInput() {
        amountField.setText("");
        newBalanceLabel.setText("Νέο Υπόλοιπο: -");
    }

    @Override public JPanel getMainPanel() { return this.panel; }
    @Override public void show() { this.panel.setVisible(true); }
    @Override public void hide() { this.panel.setVisible(false); }
}