package App.View.Screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import App.View.View_t;
import App.View.helper_classes.*;

public class DashboardScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color blue = Color.decode("#C2E5FF");
    Color red = Color.decode("#D82F4B");
    Color darkGrey = new Color(22, 20, 19);
    
    JPanel panel = new JPanel();

    // Placeholders for dynamic content
    public JLabel accountNumberLabel = new JLabel("Account: #00000000");
    public JLabel balanceLabel = new JLabel("$0,000.00");
    public JLabel userNameLabel = new JLabel("Welcome, User");
    public RoundedButton logoutBtn = new RoundedButton("X");

    public void init() {
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);

        // --- TOP HEADER PANEL ---
        JPanel header = new JPanel();
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
        accountNumberLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        accountNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Logout Button (Square, far right)
        logoutBtn.setBounds(1500, 50, 50, 50);
        logoutBtn.setBackground(red);
        logoutBtn.setForeground(Color.white);
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 18));
        logoutBtn.setFocusPainted(false);

        // User Name (To the left of the logout button)
        userNameLabel.setBounds(1100, 50, 380, 50);
        userNameLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(logo);
        header.add(accountNumberLabel); // This stays centered because of wWidth bounds
        header.add(userNameLabel);
        header.add(logoutBtn);

        // --- BALANCE SECTION ---
        JPanel balancePanel = new JPanel();
        balancePanel.setBounds(100, 180, 1400, 120);
        balancePanel.setBackground(Color.white);
        balancePanel.setLayout(new BorderLayout());
        balancePanel.setBorder(new EmptyBorder(10, 50, 10, 50));

        JLabel balTitle = new JLabel("Available Balance");
        balTitle.setFont(new Font("Verdana", Font.PLAIN, 18));
        
        balanceLabel.setFont(new Font("Verdana", Font.BOLD, 42));
        balanceLabel.setForeground(darkGrey);

        balancePanel.add(balTitle, BorderLayout.NORTH);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        // --- ACTION BUTTONS GRID (2x3) ---
        JPanel actionsPanel = new JPanel();
        // 2 rows, 3 columns, 20px gaps
        actionsPanel.setLayout(new GridLayout(2, 3, 25, 25)); 
        actionsPanel.setBounds(100, 330, 1400, 500);
        actionsPanel.setOpaque(false);

        // Add the 6 buttons
        actionsPanel.add(createMenuButton("Πληρωμή λογαριασμού"));
        actionsPanel.add(createMenuButton("Κινήσεις λογαριασμού"));
        actionsPanel.add(createMenuButton("Μεταφορές εντός τράπεζας"));
        actionsPanel.add(createMenuButton("Μεταφορές σε λογαριασμό"));
        actionsPanel.add(createMenuButton("Πάγιες πληρωμές"));
        actionsPanel.add(createMenuButton("Διαχείριση λογαριασμού"));

        panel.add(header);
        panel.add(balancePanel);
        panel.add(actionsPanel);

        hide();
    }

    /**
     * Creates a large square-ish button for the dashboard grid
     */
    private RoundedButton createMenuButton(String text) {
        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(Color.white);
        btn.setForeground(darkGrey);
        btn.setFont(new Font("Verdana", Font.BOLD, 20));
        btn.setFocusPainted(false);
        // You can add OnClickEventHelper here if needed
        return btn;
    }

    @Override
    public void show() {
        panel.setVisible(true);
        accountNumberLabel.requestFocusInWindow();
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }

    @Override
    public JPanel getMainPanel() {
        return panel;
    }
}