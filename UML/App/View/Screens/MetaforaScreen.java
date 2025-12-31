package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MetaforaScreen implements View_t {
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    private JLabel balanceLabel;
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont30 = FontLoader.loadCustomFont(fontPath, 30f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;
    Color bkgColor = Color.decode("#C2E5FF");

    private JPanel mainPanel = new JPanel();
    
    // Controls for the Controller
    private JTextField fromAccount;
    private JTextField toAccount;
    private JTextField totalMoney;
    private RoundedButton depositBut; // The Confirm button

    @Override
    public void init() {
        mainPanel.setBackground(bkgColor);
        mainPanel.setBounds(0, 0, wWidth, wHeight);
        mainPanel.setBorder((new EmptyBorder(-10, 0, 0, 0)));
        mainPanel.setLayout(null);

        // --- NO LOCAL BACK BUTTON HERE (Universal one is used) ---

        // Logo
        Image logo = new ImageIcon(getClass().getResource("/App/View/Assets/bankoftuclogo.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logo));
        logoLabel.setBounds(50, 50, 150, 150);
        mainPanel.add(logoLabel);

        // Title
        JLabel welcomeLabel = new JLabel("Μεταφορά σε Λογαριασμό");
        welcomeLabel.setFont(customFont40);
        welcomeLabel.setBounds(400, 120, 800, 50);
        mainPanel.add(welcomeLabel);

        // Balance (Top Right)
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: 0.00€</u></html>");
        balanceLabel.setFont(customFont20);
        balanceLabel.setForeground(Color.decode("#003366"));
        balanceLabel.setBounds(1200, 50, 300, 50);
        mainPanel.add(balanceLabel);

        // --- FORM ---
        
        // FROM (Sender)
        JLabel fromAccText = new JLabel("ΑΠΟ: Λογαριασμός (IBAN):");
        fromAccText.setFont(customFont20);
        fromAccText.setBounds(200, 350, 400, 30);
        mainPanel.add(fromAccText);

        fromAccount = new JTextField();
        fromAccount.setFont(customFont20);
        fromAccount.setBounds(200, 390, 400, 50);
        fromAccount.setEditable(false); // Auto-filled
        mainPanel.add(fromAccount);
        
        // TO (Receiver)
        JLabel toAccText = new JLabel("ΠΡΟΣ: Λογαριασμός (IBAN):");
        toAccText.setFont(customFont20);
        toAccText.setBounds(700, 350, 400, 30);
        mainPanel.add(toAccText);

        toAccount = new JTextField();
        toAccount.setFont(customFont20);
        toAccount.setBounds(700, 390, 400, 50);
        mainPanel.add(toAccount);

        // AMOUNT
        JLabel moneyText = new JLabel("Ποσό (€):");
        moneyText.setFont(customFont20);
        moneyText.setBounds(1200, 350, 200, 30);
        mainPanel.add(moneyText);

        totalMoney = new JTextField();
        totalMoney.setFont(customFont20);
        totalMoney.setBounds(1200, 390, 200, 50);
        mainPanel.add(totalMoney);

        // CONFIRM BUTTON
        Color red = Color.decode("#A91A32");
        depositBut = new RoundedButton("Εκτέλεση", 30);
        depositBut.setBackground(red);
        depositBut.setForeground(Color.white);
        depositBut.setFont(customFont30);
        depositBut.setFocusPainted(false);
        depositBut.setBounds((wWidth - 300) / 2, 600, 300, 80);
        mainPanel.add(depositBut);

        hide();
    }

    // --- GETTERS ---
    public String getToIban() { return toAccount.getText(); }
    public String getAmount() { return totalMoney.getText(); }
    public JButton getConfirmBtn() { return depositBut; }

    // --- SETTERS ---
    public void setFromIban(String text) { fromAccount.setText(text); }
    public void setBalance(String amount) {
        balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }
    public void clearFields() {
        toAccount.setText("");
        totalMoney.setText("");
    }

    @Override public JPanel getMainPanel() { return mainPanel; }
    @Override public void hide() { this.mainPanel.setVisible(false); }
    @Override public void show() { this.mainPanel.setVisible(true); }
}