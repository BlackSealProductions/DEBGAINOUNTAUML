package App.View.Screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import App.View.View_t;
import App.View.helper_classes.*;

public class EditDataScreen implements View_t {

    // --- Fonts & Colors ---
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont40 = safeLoadFont(fontPath, 40f);
    Font customFont20 = safeLoadFont(fontPath, 20f);
    Font tableFont = new Font("SansSerif", Font.PLAIN, 16);
    Font headerFont = safeLoadFont(fontPath, 16f);
    Font arrowFont = new Font("SansSerif", Font.BOLD, 40); 

    final int wWidth = 1200;
    final int wHeight = 800;

    Color bgBlue = Color.decode("#C2E5FF");
    Color btnRed = Color.decode("#D82F4B");
    Color textDark = new Color(22, 20, 19);
    
    // Tab Colors
    Color tabBg = Color.decode("#E1F0FF");
    Color tableHeaderBg = Color.decode("#36454F");
    Color tableHeaderFg = Color.WHITE;

    JPanel panel = new JPanel();

    // --- Components ---
    private RoundedButton backBtn;
    private JTabbedPane tabbedPane;
    
    // Tab 1: Users
    private JTable userTable;
    private DefaultTableModel userModel;
    private RoundedButton deleteUserBtn;
    private RoundedButton editUserBtn;

    // Tab 2: Accounts
    private JTable accountTable;
    private DefaultTableModel accountModel;
    private RoundedButton deleteAccountBtn;
    private RoundedButton editBalanceBtn;

    public JLabel title = new JLabel("System Data Management");

    @Override
    public void init() {
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);
        panel.setBackground(bgBlue);

        // ==================== 1. HEADER ====================
        backBtn = new RoundedButton("⬅", 10); 
        backBtn.setBounds(30, 30, 80, 60); 
        backBtn.setBackground(btnRed);
        backBtn.setForeground(Color.WHITE); 
        backBtn.setFont(arrowFont);
        backBtn.setFocusPainted(false);
        backBtn.setMargin(new Insets(0,0,0,0)); 
        panel.add(backBtn);

        title.setBounds(120, 35, wWidth - 240, 50);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(customFont40);
        title.setForeground(textDark);
        panel.add(title);

        // ==================== 2. TABBED PANE ====================
        tabbedPane = new JTabbedPane();
        // Fix: Increased height slightly (wHeight - 150) to give more room
        tabbedPane.setBounds(50, 120, wWidth - 100, wHeight - 150);
        tabbedPane.setFont(customFont20);
        tabbedPane.setBackground(Color.WHITE);
        
        // --- Create the two tabs ---
        JPanel userTab = createUserTab();
        JPanel accountTab = createAccountTab();

        tabbedPane.addTab("Manage Users", userTab);
        tabbedPane.addTab("Manage Accounts", accountTab);

        panel.add(tabbedPane);

    }

    // =============================================================
    //                     TAB 1: USERS
    // =============================================================
    private JPanel createUserTab() {
        JPanel p = new JPanel(null);
        p.setBackground(tabBg);

        // Table
        String[] cols = {"Tax ID", "Username", "Type", "Email", "Phone"};
        userModel = new DefaultTableModel(cols, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        userTable = new JTable(userModel);
        styleTable(userTable);

        // Fix: Reduced ScrollPane Height from 500 to 420
        JScrollPane scroll = new JScrollPane(userTable);
        scroll.setBounds(20, 20, 1060, 420); 
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll);

        // Buttons - Fix: Moved Y up from 540 to 480
        int btnY = 480;
        
        deleteUserBtn = new RoundedButton("Delete User", 15);
        deleteUserBtn.setBounds(20, btnY, 200, 50);
        styleButton(deleteUserBtn);
        p.add(deleteUserBtn);

        editUserBtn = new RoundedButton("Edit Password", 15);
        editUserBtn.setBounds(240, btnY, 200, 50);
        styleButton(editUserBtn);
        p.add(editUserBtn);
        
        // Sample Data
        userModel.addRow(new Object[]{"0001", "stelios", "Individual", "s@mail.com", "69000"});
        userModel.addRow(new Object[]{"0002", "techCorp", "Company", "info@tech.com", "21099"});

        return p;
    }

    // =============================================================
    //                     TAB 2: ACCOUNTS
    // =============================================================
    private JPanel createAccountTab() {
        JPanel p = new JPanel(null);
        p.setBackground(tabBg);

        // Table
        String[] cols = {"IBAN", "Owner ID", "Type", "Balance (€)", "Currency"};
        accountModel = new DefaultTableModel(cols, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        accountTable = new JTable(accountModel);
        styleTable(accountTable);

        // Fix: Reduced ScrollPane Height from 500 to 420
        JScrollPane scroll = new JScrollPane(accountTable);
        scroll.setBounds(20, 20, 1060, 420);
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll);

        // Buttons - Fix: Moved Y up from 540 to 480
        int btnY = 480;

        deleteAccountBtn = new RoundedButton("Delete Account", 15);
        deleteAccountBtn.setBounds(20, btnY, 250, 50);
        styleButton(deleteAccountBtn);
        p.add(deleteAccountBtn);

        editBalanceBtn = new RoundedButton("Set Balance", 15);
        editBalanceBtn.setBounds(290, btnY, 200, 50);
        styleButton(editBalanceBtn);
        p.add(editBalanceBtn);

        // Sample Data
        accountModel.addRow(new Object[]{"GR50...202", "0001", "Savings", "1500.00", "EUR"});
        accountModel.addRow(new Object[]{"GR99...555", "0002", "Business", "50000.00", "EUR"});

        return p;
    }

    private void styleTable(JTable table) {
        table.setFont(tableFont);
        table.setRowHeight(35);
        table.setSelectionBackground(btnRed.brighter());
        table.setSelectionForeground(Color.WHITE);
        
        JTableHeader h = table.getTableHeader();
        h.setFont(headerFont);
        h.setBackground(tableHeaderBg);
        h.setForeground(tableHeaderFg);
        h.setPreferredSize(new Dimension(100, 40));
    }

    private void styleButton(JButton btn) {
        btn.setBackground(btnRed);
        btn.setForeground(Color.white);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
    }

    private Font safeLoadFont(String path, float size) {
        try { return FontLoader.loadCustomFont(path, size); }
        catch (Exception e) { return new Font("SansSerif", Font.BOLD, (int)size); }
    }

    @Override public JPanel getMainPanel() { return panel; }
    @Override public void show() { panel.setVisible(true); }
    @Override public void hide() { panel.setVisible(false); }

    // --- GETTERS ---
    public RoundedButton getBackBtn() { return backBtn; }
    
    public JTable getUserTable() { return userTable; }
    public DefaultTableModel getUserModel() { return userModel; }
    public RoundedButton getDeleteUserBtn() { return deleteUserBtn; }
    public RoundedButton getEditUserBtn() { return editUserBtn; }
    
    public JTable getAccountTable() { return accountTable; }
    public DefaultTableModel getAccountModel() { return accountModel; }
    public RoundedButton getDeleteAccountBtn() { return deleteAccountBtn; }
    public RoundedButton getEditBalanceBtn() { return editBalanceBtn; }

    // --- TEST MAIN ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Edit Data Test");
            EditDataScreen screen = new EditDataScreen();
            screen.init();
            frame.setContentPane(screen.getMainPanel());
            frame.setSize(1200, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}