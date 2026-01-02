package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class MetaforaScreen implements View_t {
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    
    // --- Components ---
    private JLabel balanceLabel;
    private JTextField fromAccount;
    private JTextField toAccount;
    private JTextField totalMoney;
    private RoundedButton depositBut;
    
    
    // NEW: Radio Buttons for Bank Choice
    private JRadioButton inBankRadio;
    private JRadioButton otherBankRadio;
    private ButtonGroup bankGroup;
    // ------------------

    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont30 = FontLoader.loadCustomFont(fontPath, 30f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color bkgColor = Color.decode("#C2E5FF");
    private JPanel mainPanel = new JPanel();

    @Override
    public void init() {
        mainPanel.setBackground(bkgColor);
        mainPanel.setBounds(0, 0, wWidth, wHeight);
        mainPanel.setBorder((new EmptyBorder(-10, 0, 0, 0)));
        mainPanel.setLayout(null);

        // Logo
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon, 20);
        logoPanel.setBounds(50, 50, 200, 200);

        // Title
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(null);
        welcomePanel.setBounds(400, 120, 800, 200);
        JLabel welcomeLabel = new JLabel("Μεταφορά σε Λογαριασμό");
        welcomeLabel.setFont(customFont40);
        welcomePanel.add(welcomeLabel);

        // --- NEW: Radio Buttons Section ---
        inBankRadio = new JRadioButton("Bank of TUC (Internal)");
        inBankRadio.setFont(customFont20);
        inBankRadio.setBackground(bkgColor);
        inBankRadio.setBounds(200, 350, 350, 50);
        inBankRadio.setSelected(true); // Default to Internal

        otherBankRadio = new JRadioButton("Other Bank (Fee 2%)");
        otherBankRadio.setFont(customFont20);
        otherBankRadio.setBackground(bkgColor);
        otherBankRadio.setBounds(700, 350, 350, 50);

        bankGroup = new ButtonGroup();
        bankGroup.add(inBankRadio);
        bankGroup.add(otherBankRadio);

        mainPanel.add(inBankRadio);
        mainPanel.add(otherBankRadio);
        // ----------------------------------

        // Fields
        JLabel fromAccText = new JLabel("ΑΠΟ: Λογαριασμός:");
        fromAccText.setFont(customFont20);
        fromAccText.setBounds(200, 430, 400, 50);
        
        fromAccount = new JTextField();
        fromAccount.setFont(customFont20);
        fromAccount.setBounds(200, 480, 400, 50);
        fromAccount.setEditable(true);
        fromAccount.setEnabled(false);
        // FIX: Make text visible even when disabled
        fromAccount.setDisabledTextColor(Color.BLACK); 
        fromAccount.setBackground(Color.WHITE);
        
        JLabel toAccText = new JLabel("ΠΡΟΣ: Λογαριασμός:");
        toAccText.setFont(customFont20);
        toAccText.setBounds(700, 430, 400, 50);
        
        toAccount = new JTextField();
        toAccount.setFont(customFont20);
        toAccount.setBounds(700, 480, 400, 50);
        
        totalMoney = new JTextField();
        totalMoney.setFont(customFont20);
        totalMoney.setBounds(1300, 480, 100, 50);
        
        JLabel moneyText = new JLabel("Ποσο:");
        moneyText.setFont(customFont20);
        moneyText.setBounds(1300, 430, 100, 50);

        // Balance 
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHead.setOpaque(false);
        rightHead.setPreferredSize(new Dimension(300, 100)); 
        rightHead.setBounds(1200, 30, 300, 100);
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: -- €</u></html>");
        balanceLabel.setFont(customFont20);
        balanceLabel.setForeground(Color.decode("#003366")); 
        rightHead.add(balanceLabel);

        mainPanel.add(fromAccount);
        mainPanel.add(toAccount);
        mainPanel.add(moneyText);
        mainPanel.add(totalMoney);
        mainPanel.add(fromAccText);
        mainPanel.add(toAccText);

        // Button
        Color red = Color.decode("#A91A32");
        depositBut = new RoundedButton("Transfer", 30);
        depositBut.setBackground(red);
        depositBut.setForeground(Color.white);
        depositBut.setFont(customFont40);
        depositBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositBut.setMaximumSize(new Dimension(400, 100));
        depositBut.setMinimumSize(new Dimension(300, 150));
        depositBut.setFocusPainted(false);
        depositBut.setBounds(620, 670, 300, 90);

        mainPanel.add(logoPanel);
        mainPanel.add(welcomePanel);
        mainPanel.add(depositBut);
        mainPanel.add(rightHead);
        hide();
    }

    @Override
    public JPanel getMainPanel() { return mainPanel; }
    @Override
    public void hide() { this.mainPanel.setVisible(false); }
    @Override
    public void show() { this.mainPanel.setVisible(true); }

    public void setBalance(String amount) {
        balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }

    // Getters
    public JTextField getFromAccountField() { return fromAccount; }
    public JTextField getToAccountField() { return toAccount; }
    public JTextField getAmountField() { return totalMoney; }
    public JButton getConfirmButton() { return depositBut; }
    public void setFromAccountLabel(String accInfo) { this.fromAccount.setText(accInfo); this.fromAccount.setEditable(false); }
    // New Getters for Radio Buttons
    public boolean isInBankSelected() { return inBankRadio.isSelected(); }
}