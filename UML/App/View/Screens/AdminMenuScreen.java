package App.View.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import App.View.View_t;
import App.View.helper_classes.*; 
import Utils.GlobalConsts; 

public class AdminMenuScreen implements View_t {

    // --- 1. FONTS & COLORS ---
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    
    // Safety: Fallback to standard font if the file is missing/fails
    Font customFont60 = safeLoadFont(fontPath, 60f);
    Font customFont30 = safeLoadFont(fontPath, 30f);
    Font customFont20 = safeLoadFont(fontPath, 20f);
    Font arrowFont = new Font("SansSerif", Font.BOLD, 24); 

    // --- FIX: Force default size ---
    final int wWidth = 1200; 
    final int wHeight = 800;

    Color blue = Color.decode("#C2E5FF");
    Color red = Color.decode("#D82F4B");
    Color darkred = Color.decode("#A02336");
    Color darkgrey = new Color(22, 20, 19);

    JPanel panel = new JPanel();

    // --- 2. COMPONENTS ---
    private RoundedButton simulationBtn;
    private RoundedButton auditBtn;
    private RoundedButton editDataBtn;
    private RoundedButton logoutBtn;
    
    public JLabel title = new JLabel("Admin Dashboard");
    public JLabel subTitle = new JLabel("Account: ID_242490");
    public JLabel userLabel = new JLabel("Mr Admin the 3rd");

    @Override
    public void init() {
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);
        panel.setBackground(blue);

        // ==========================================
        //            HEADER SECTION
        // ==========================================

        // 1. LOGO
        ImageIcon logoIcon = loadLogo("/Images/bankOfTucLogo_white.png");
        RoundedImage logoPanel = new RoundedImage(logoIcon, 25);
        logoPanel.setBounds(50, 50, 200, 200);
        panel.add(logoPanel);

        // 2. TITLE (Centered)
        title.setBounds(0, 60, wWidth, 70); 
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(customFont60);
        title.setForeground(darkgrey);
        panel.add(title);

        // 3. SUBTITLE
        subTitle.setBounds(0, 130, wWidth, 40);
        subTitle.setHorizontalAlignment(SwingConstants.CENTER);
        subTitle.setFont(customFont30);
        subTitle.setForeground(darkred); 
        panel.add(subTitle);

        // 4. USER INFO (Top Right)
        int userX = wWidth - 300; 
        userLabel.setBounds(userX - 60, 60, 250, 30); 
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        userLabel.setFont(customFont20);
        userLabel.setForeground(darkgrey);
        panel.add(userLabel);

        // 5. LOGOUT BUTTON
        logoutBtn = new RoundedButton("➜", 10);
        logoutBtn.setBounds(wWidth - 100, 50, 50, 50); 
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(red);
        logoutBtn.setFont(arrowFont); 
        logoutBtn.setFocusPainted(false);
        panel.add(logoutBtn);

        // ==========================================
        //            BUTTON SECTION
        // ==========================================
        
        int buttonWidth = 300;  
        int buttonHeight = 200; 
        int gap = 50;           
        int startY = 400;       
        
        int totalWidth = (buttonWidth * 3) + (gap * 2);
        int startX = (wWidth - totalWidth) / 2;

        // --- Simulation Button ---
        simulationBtn = new RoundedButton("Simulation", 20);
        simulationBtn.setBounds(startX, startY, buttonWidth, buttonHeight);
        styleButton(simulationBtn);
        panel.add(simulationBtn);

        // --- Audit Logs Button ---
        auditBtn = new RoundedButton("Audit Logs", 20);
        auditBtn.setBounds(startX + buttonWidth + gap, startY, buttonWidth, buttonHeight);
        styleButton(auditBtn);
        panel.add(auditBtn);

        // --- Edit Data Button ---
        editDataBtn = new RoundedButton("Edit Data", 20);
        editDataBtn.setBounds(startX + (buttonWidth + gap) * 2, startY, buttonWidth, buttonHeight);
        styleButton(editDataBtn);
        panel.add(editDataBtn);

    }

    private void styleButton(JButton btn) {
        btn.setBackground(red);
        btn.setForeground(Color.white);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
    }

    // --- Helpers ---
    private ImageIcon loadLogo(String path) {
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage();
            return new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics(); g.setColor(Color.WHITE); g.fillRect(0,0,200,200); g.setColor(Color.BLACK); g.drawString("LOGO", 80, 100);
            return new ImageIcon(bi);
        }
    }
    
    private Font safeLoadFont(String path, float size) {
        try {
            return FontLoader.loadCustomFont(path, size);
        } catch (Exception e) {
            return new Font("SansSerif", Font.BOLD, (int)size);
        }
    }

    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); }
    @Override
    public void hide() { panel.setVisible(false); }

    public RoundedButton getSimulationBtn() { return simulationBtn; }
    public RoundedButton getAuditBtn() { return auditBtn; }
    public RoundedButton getEditDataBtn() { return editDataBtn; }
    public RoundedButton getLogoutBtn() { return logoutBtn; }

    // NOTE: Removed main() method to prevent "getFrame" errors. 
    // Navigation is now handled exclusively by AdminMenuCon.
}