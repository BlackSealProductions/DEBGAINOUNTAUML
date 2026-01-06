package App.View.Screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.View.View_t;
import App.View.helper_classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardScreen implements View_t {


    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);
    Font customFont15 = FontLoader.loadCustomFont(fontPath, 15f);


    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color blue = Color.decode("#C2E5FF");
    Color red = Color.decode("#D82F4B");
    Color darkGrey = new Color(22, 20, 19);
    
    JPanel panel = new JPanel();
    
    // DATA FOR DISPLAYING STATS
    public String userBalance = "0";
    public String username = "nao";

    // Placeholders for dynamic content
    private JLabel accountNumberLabel = new JLabel("Account: #00000000");
    private JLabel balanceLabel = new JLabel(userBalance+" €");
    private JLabel usertype = new JLabel();

    private JLabel userNameLabel = new JLabel("Welcome, " + username);
    private RoundedButton logoutBtn = new RoundedButton("X",15);
    private RoundedButton switchBtn = new RoundedButton("switch", 15);
    private RoundedButton issueBtn = new RoundedButton("issue bill", 15);

    private JPanel header;

    public RoundedButton plhrwmhBtn = createMenuButton("Πληρωμή λογαριασμού");
    public RoundedButton kinhseisBtn = createMenuButton("Κινήσεις λογαριασμού");
    public RoundedButton createAccBtn = createMenuButton("Δημιουργία Λογαριασμού");
    public RoundedButton metaforesBtn = createMenuButton("Μεταφορά σε λογαριασμό");
    public RoundedButton pagiesBtn = createMenuButton("Πάγιες πληρωμές");
    public RoundedButton diaxeirisiBtn = createMenuButton("Διαχείριση λογαριασμού");
    public RoundedButton depositBtn = createMenuButton("Κατάθεση");
    public RoundedButton withdrawBtn = createMenuButton("Ανάλυψη");

    int headeroffset = 990;
    int headeroffset_comp = 990 - 190;
    // Boolean comp = false;
    public void checkForCompany(){

        if(usertype.getText().equals("Company")){

            issueBtn.setBounds(1380-180-10, 50, 180, 50);
            issueBtn.setBackground(Color.green);
            issueBtn.setForeground(Color.white);
            issueBtn.setFont(customFont20);
            issueBtn.setFocusPainted(false);
            header.add(issueBtn);
            headeroffset = headeroffset_comp;
            userNameLabel.setBounds(headeroffset, 50, 380, 50);
            userNameLabel.setFont(customFont20);
            userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            issueBtn.setVisible(true);
        }
        else{
            headeroffset = 990;
            issueBtn.setVisible(false);
            userNameLabel.setBounds(headeroffset, 50, 380, 50);
            userNameLabel.setFont(customFont20);
            userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }

    public void init() {
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);

        // --- TOP HEADER PANEL ---
        header = new JPanel();
        header.setLayout(null);
        header.setBounds(0, 0, wWidth, 150);
        header.setBackground(Color.white);

        // Logo (Left side)
        JLabel logo = new JLabel();
        logo.setBounds(0, 10, 150, 150);
        try {
            Image logoImg = new ImageIcon(getClass().getResource("/App/View/Assets/bankoftuclogo.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(logoImg));
        } catch (Exception e) {
            logo.setText("BANK LOGO");
        }

        // Account Number (Centered relative to the frame)
        accountNumberLabel.setBounds(0, 50, wWidth, 50);
        accountNumberLabel.setFont(customFont20);
        accountNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usertype.setBounds(0,80, wWidth,50);
        usertype.setFont(customFont15);
        usertype.setHorizontalAlignment(SwingConstants.CENTER);


        switchBtn.setBounds(1380, 50, 110, 50);
        switchBtn.setBackground(Color.blue);
        switchBtn.setForeground(Color.white);
        switchBtn.setFont(customFont20);
        switchBtn.setFocusPainted(false);

        // Logout Button (Square, far right)
        logoutBtn.setBounds(1500, 50, 50, 50);
        logoutBtn.setBackground(red);
        logoutBtn.setForeground(Color.white);
        logoutBtn.setFont(customFont20);
        logoutBtn.setFocusPainted(false);

        userNameLabel.setBounds(headeroffset, 50, 380, 50);
        userNameLabel.setFont(customFont20);
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // User Name (To the left of the logout button)
        // userNameLabel.setBounds(headeroffset-190, 50, 380, 50);
        // userNameLabel.setFont(customFont20);
        // userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(logo);
        header.add(accountNumberLabel); // This stays centered because of wWidth bounds
        header.add(usertype);
        header.add(userNameLabel);
        header.add(switchBtn);
        header.add(logoutBtn);

        // --- BALANCE SECTION ---
        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(100, 180, 1400, 120);
        balancePanel.setBackground(Color.white);
        balancePanel.setLayout(new BorderLayout());
        balancePanel.setBorder(new EmptyBorder(10, 50, 10, 50));

        JLabel balTitle = new JLabel("Available Balance");
        balTitle.setFont(customFont20);
        
        balanceLabel.setFont(customFont40);
        balanceLabel.setForeground(darkGrey);

        balancePanel.add(balTitle, BorderLayout.NORTH);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        // --- ACTION BUTTONS GRID (2x3) ---
        JPanel actionsPanel = new JPanel();
        // 2 rows, 3 columns, 20px gaps
        actionsPanel.setLayout(new GridLayout(2, 4, 25, 25)); 
        actionsPanel.setBounds(100, 330, 1400, 500);
        actionsPanel.setOpaque(false);

        

        initActionButtons(actionsPanel);

        panel.add(header);
        panel.add(balancePanel);
        panel.add(actionsPanel);

        hide();
    }

    /**
     * Creates a large square-ish button for the dashboard grid
     */
    private RoundedButton createMenuButton(String text) {
        RoundedButton btn = new RoundedButton(text,15);
        btn.setBackground(Color.white);
        btn.setForeground(darkGrey);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
        // You can add OnClickEventHelper here if needed
        return btn;
    }

    // Store references to the dashboard action buttons
    

    /**
     * Helper to initialize and store the dashboard action buttons
     */
    private void initActionButtons(JPanel actionsPanel) {
        plhrwmhBtn.setBackground(red);
        plhrwmhBtn.setForeground(Color.white);
        kinhseisBtn.setBackground(red);
        kinhseisBtn.setForeground(Color.white);
        metaforesBtn.setBackground(red);
        metaforesBtn.setForeground(Color.white);
        createAccBtn.setBackground(red);
        createAccBtn.setForeground(Color.white);
        pagiesBtn.setBackground(red);
        pagiesBtn.setForeground(Color.white);
        diaxeirisiBtn.setBackground(red);
        diaxeirisiBtn.setForeground(Color.white);
        depositBtn.setBackground(red);
        depositBtn.setForeground(Color.white);
        withdrawBtn.setBackground(red);
        withdrawBtn.setForeground(Color.white);

        
        actionsPanel.add(plhrwmhBtn);
        actionsPanel.add(kinhseisBtn);
        actionsPanel.add(metaforesBtn);
        actionsPanel.add(createAccBtn);
        actionsPanel.add(pagiesBtn);
        actionsPanel.add(diaxeirisiBtn);
        actionsPanel.add(depositBtn);
        actionsPanel.add(withdrawBtn);
        
    }

    @Override
    public void show() {
        panel.setVisible(true);
        accountNumberLabel.requestFocusInWindow();
        checkForCompany();
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }

    @Override
    public JPanel getMainPanel() {
        return panel;
    }

    public JLabel getAccountNumberLabel() {
        return accountNumberLabel;
    }

    public void setBalance(String amount) {
        try {
            // Parse the string to a double to perform formatting
            double val = Double.parseDouble(amount);
            // %.2f limits the output to exactly two decimal places
            String formattedBalance = String.format("%.2f", val);
            balanceLabel.setText("Υπόλοιπο: " + formattedBalance + "€");
        } catch (NumberFormatException e) {
            // Fallback in case the string isn't a valid number
            balanceLabel.setText("Υπόλοιπο: " + amount + "€");
        }
    }

    public JLabel getUserNameLabel() {
        return userNameLabel;
    }

    public RoundedButton getLogoutBtn(){
        return this.logoutBtn;
    }

    public RoundedButton getSwitchBtn(){
        return this.switchBtn;
    }

    public RoundedButton getIssueBtn(){
        return this.issueBtn;
    }

    public void setAccountDetails(String username, String balance, String acctId, String type){
        userNameLabel.setText("Welcome, " + username);
        accountNumberLabel.setText("Account: #" + acctId);
        usertype.setText(type);
        setBalance(balance);
        // if(Float.parseFloat(balance)  <= 0){

        //     balanceLabel.setText(balance+" € (8a se valoun mesa se ligo :())");
        // }
        // else if (Float.parseFloat(balance) > 100 ) {
        //    balanceLabel.setText(balance+" € (eisai leftas :))"); 
        // }
        // else{
        //     balanceLabel.setText(balance+" € (eisai duskola :/)"); 
        // }
    }

    public void refresh(Account account){
        setAccountDetails(account.getOwnerName(), account.getBalance(), account.getAccountId(), Session.getInstance().getActiveCustomer().getUserTypeString());

    }

    public Map<String, String> issueBill(){

        // 1. Create the fields
        JTextField iban = new JTextField(10);
        JTextField amount = new JTextField(10);

        // 2. Create a panel to hold them
        JPanel myPanel = new JPanel(new GridLayout(0, 1)); // 0 rows, 1 column
        myPanel.add(new JLabel("IBAN:"));
        myPanel.add(iban);
        myPanel.add(Box.createVerticalStrut(15)); // Add some spacing
        myPanel.add(new JLabel("Amount:"));
        myPanel.add(amount);

        int result = JOptionPane.showConfirmDialog(null, myPanel, 
            "Issue New Utility Bill", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Map<String, String> vals = new HashMap<>();
            vals.put("iban", iban.getText());
            vals.put("amount", amount.getText());
            return vals;
        }   
        return null; // Return null if cancelled
        
    }

}
