package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DepositScreen implements View_t {

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
    private RoundedButton confirmBtn, checkBtn;
    private JLabel balanceLabel, newBalanceLabel;
    private double currentBalance = 0.0; // This should be updated by your Controller

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);

        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(50,50,200,200);
        panel.add(logoPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Κατάθεση Χρημάτων");
        title.setFont(customFont40);
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        balanceLabel = new JLabel("Τρέχον Υπόλοιπο: " + currentBalance + "€");
        balanceLabel.setFont(customFont20);
        gbc.gridy = 1;
        centerPanel.add(balanceLabel, gbc);

        amountField = new JTextField("0.00");
        amountField.setPreferredSize(new Dimension(350, 50));
        amountField.setFont(customFont20);
        amountField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 10, 0);
        centerPanel.add(amountField, gbc);

        // Check Button
        checkBtn = new RoundedButton("Υπολογισμός", 10);
        checkBtn.setFont(customFont12);
        checkBtn.setPreferredSize(new Dimension(150, 30));
        checkBtn.addActionListener(e -> calculateNewBalance(true));
        gbc.gridy = 3;
        centerPanel.add(checkBtn, gbc);

        newBalanceLabel = new JLabel("Νέο Υπόλοιπο: "+currentBalance+"€");
        newBalanceLabel.setFont(customFont12);
        newBalanceLabel.setForeground(Color.DARK_GRAY);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 30, 0);
        centerPanel.add(newBalanceLabel, gbc);

        confirmBtn = new RoundedButton("Επιβεβαίωση", 15);
        confirmBtn.setBackground(red);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setPreferredSize(new Dimension(250, 60));
        confirmBtn.setFont(customFont20);
        gbc.gridy = 5;
        centerPanel.add(confirmBtn, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);
        hide();
    }

    private void calculateNewBalance(boolean isDeposit) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            double result = isDeposit ? (currentBalance + amount) : (currentBalance - amount);
            newBalanceLabel.setText("Νέο Υπόλοιπο: " + String.format("%.2f", result) + "€");
        } catch (NumberFormatException e) {
            newBalanceLabel.setText("Σφάλμα: Έγκυρο ποσό παρακαλώ");
        }
    }

    public JPanel getMainPanel() { return this.panel; }
    public void show() { this.panel.setVisible(true);amountField.requestFocusInWindow();}
    public void hide() { this.panel.setVisible(false); }
    public void setCurrentBalance(String bal) { 
        this.currentBalance = Float.parseFloat(bal); 
        balanceLabel.setText("Τρέχον Υπόλοιπο: " + bal + "€");
        newBalanceLabel.setText(bal);
    }
    public JButton getConfirmBtn() { return confirmBtn; }

    public JTextField getAmountField() {
        return amountField;
    }
}