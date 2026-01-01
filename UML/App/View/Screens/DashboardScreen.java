package App.View.Screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import App.View.View_t;
import App.View.helper_classes.*;

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
    
    // Labels
    private JLabel accountNumberLabel = new JLabel("Account: #...");
    private JLabel balanceLabel = new JLabel("0 €");
    private JLabel userNameLabel = new JLabel("Welcome...");
    private RoundedButton logoutBtn = new RoundedButton("X", 15);

    // --- THE 8 BUTTONS ---
    // Row 1
    public RoundedButton plhrwmhBtn = createMenuButton("Πληρωμή λογαριασμού");
    public RoundedButton kinhseisBtn = createMenuButton("Κινήσεις λογαριασμού");
    public RoundedButton metaforaBtn = createMenuButton("Μεταφορά σε λογαριασμό");
    public RoundedButton createAccBtn = createMenuButton("Δημιουργία Λογαριασμού"); // NEW

    // Row 2
    public RoundedButton pagiesBtn = createMenuButton("Πάγιες πληρωμές");
    public RoundedButton diaxeirisiBtn = createMenuButton("Διαχείριση λογαριασμού");
    public RoundedButton katathesiBtn = createMenuButton("Κατάθεση"); // NEW
    public RoundedButton analipsiBtn = createMenuButton("Ανάληψη");   // NEW

    public void init() {
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);

        // --- HEADER ---
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, wWidth, 150);
        header.setBackground(Color.white);

        JLabel logo = new JLabel();
        logo.setBounds(0, 10, 150, 150);
        try {
            Image logoImg = new ImageIcon(getClass().getResource("/App/View/Assets/bankoftuclogo.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(logoImg));
        } catch (Exception e) { logo.setText("LOGO"); }

        accountNumberLabel.setBounds(0, 50, wWidth, 50);
        accountNumberLabel.setFont(customFont20);
        accountNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);

        logoutBtn.setBounds(1500, 50, 50, 50);
        logoutBtn.setBackground(red);
        logoutBtn.setForeground(Color.white);

        userNameLabel.setBounds(1100, 50, 380, 50);
        userNameLabel.setFont(customFont20);
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(logo);
        header.add(accountNumberLabel);
        header.add(userNameLabel);
        header.add(switchBtn);
        header.add(logoutBtn);

        // --- BALANCE PANEL ---
        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setBounds(100, 180, 1400, 120);
        balancePanel.setBackground(Color.white);
        balancePanel.setBorder(new EmptyBorder(10, 50, 10, 50));

        JLabel balTitle = new JLabel("Available Balance");
        balTitle.setFont(customFont20);
        balanceLabel.setFont(customFont40);
        balanceLabel.setForeground(darkGrey);

        balancePanel.add(balTitle, BorderLayout.NORTH);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        // --- BUTTONS GRID (2 Rows, 4 Columns) ---
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(2, 4, 25, 25)); // Changed from 3 to 4 columns
        actionsPanel.setBounds(100, 330, 1400, 500);
        actionsPanel.setOpaque(false);

        // Row 1
        addButton(actionsPanel, plhrwmhBtn);
        addButton(actionsPanel, kinhseisBtn);
        addButton(actionsPanel, metaforaBtn);
        addButton(actionsPanel, createAccBtn); // Add to grid
        
        // Row 2
        addButton(actionsPanel, pagiesBtn);
        addButton(actionsPanel, diaxeirisiBtn);
        addButton(actionsPanel, katathesiBtn); // Add to grid
        addButton(actionsPanel, analipsiBtn);  // Add to grid

        panel.add(header);
        panel.add(balancePanel);
        panel.add(actionsPanel);
        hide();
    }

    private void addButton(JPanel p, RoundedButton btn) {
        btn.setBackground(red);
        btn.setForeground(Color.white);
        p.add(btn);
    }

    private RoundedButton createMenuButton(String text) {
        RoundedButton btn = new RoundedButton(text, 15);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
        return btn;
    }

    // --- GETTERS ---
    public RoundedButton getLogoutBtn() { return logoutBtn; }
    public RoundedButton getDepositBtn() { return katathesiBtn; } // New Getter
    public RoundedButton getCreateAccountBtn() { return createAccBtn; } // New Getter
    public RoundedButton getWithdrawBtn() { return analipsiBtn; } // New Getter

    // ... Getters for others if needed ...

    public RoundedButton getSwitchBtn(){
        return this.switchBtn;
    }

    public void setAccountDetails(String username, String balance, String acctId, String type){
        userNameLabel.setText("Welcome, " + username);
        accountNumberLabel.setText("Account: #" + acctId);
        
        // Keep your fun logic
        try {
            float balValue = Float.parseFloat(balance);
            if(balValue <= 0) balanceLabel.setText(balance + " € (8a se valoun mesa se ligo :())");
            else if (balValue > 100) balanceLabel.setText(balance + " € (eisai leftas :))"); 
            else balanceLabel.setText(balance + " €"); 
        } catch (Exception e) {
             balanceLabel.setText(balance + " €");
        }
    }

    @Override public void show() { panel.setVisible(true); }
    @Override public void hide() { panel.setVisible(false); }
    @Override public JPanel getMainPanel() { return panel; }
    
    // Setters for balance/account labels just in case
    public void setBalanceLabel(String t) { balanceLabel.setText(t); }
    public void setAccountIdLabel(String t) { accountNumberLabel.setText(t); }
    public void setWelcomeMessage(String t) { userNameLabel.setText(t); }
}