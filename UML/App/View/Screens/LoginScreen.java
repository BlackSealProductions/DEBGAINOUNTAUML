package App.View.Screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

public class LoginScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color darkgrey = new Color(22, 20, 19);
    Color blue = Color.decode("#C2E5FF");
    Color lightblue = Color.decode("#BBE8E2");
    Color red = Color.decode("#D82F4B");
    Color darkred = Color.decode("#A02336");

    JPanel panel = new JPanel();

    // --- 1. Promote these to Class Variables so Getters can see them ---
    private JTextField usernameField;
    private JPasswordField passwordField; // Changed to PasswordField for security
    private RoundedButton loginButton;
    private RoundedButton registerButton;
    
    public JLabel title = new JLabel("Καλως ήρθατε");
    public JLabel title2 = new JLabel("στην Bank of TUC");

    @Override
    public void init() {
        panel.setBackground(Color.white); 
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setLayout(null);
        panel.setBackground(blue);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBounds(0, 0, 1600, 240);
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        titlePanel.setBackground(Color.decode("#D6D8D7"));

        // --- Inner panel for login content ---
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBounds(GlobalConsts.wxCenter(wWidth, 800), 220, 800, 560);
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginPanel.setBackground(Color.black);

        // --- Logo ---
        JLabel logo = new JLabel();
        logo.setBounds(0, 0, 1159, 158);
        try {
            BufferedImage logoImg = ImageIO.read(new File("UML/App/View/Assets/logo_.png"));
            logo = new JLabel(new ImageIcon(logoImg));
        } catch (Exception e) {
            System.out.println("Logo not found");
        }
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Username field ---
        usernameField = new JTextField("Username: ");
        usernameField.setMaximumSize(new Dimension(380, 100));
        usernameField.setPreferredSize(new Dimension(380, 100));
        usernameField.setBackground(Color.white);
        usernameField.setForeground(Color.decode("#C6D3D0"));
        usernameField.setFont(customFont20);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Helper to handle placeholder text
        OnFocusEventHelper.setOnFocusText(usernameField, "Username: ", Color.black, Color.decode("#C6D3D0"));

        // --- Password field ---
        // Using JPasswordField masking characters by default
        passwordField = new JPasswordField("Password: ");
        passwordField.setEchoChar((char) 0); // Start visible for placeholder "Password:"
        passwordField.setMaximumSize(new Dimension(380, 100));
        passwordField.setPreferredSize(new Dimension(380, 100));
        passwordField.setBackground(Color.white);
        passwordField.setForeground(Color.decode("#C6D3D0"));
        passwordField.setFont(customFont20);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Custom logic might be needed for Password placeholders, but applying your helper:
        OnFocusEventHelper.setOnFocusText(passwordField, "Password: ", Color.black, Color.decode("#C6D3D0"));

        // --- Buttons ---
        loginButton = new RoundedButton("login",15);
        loginButton.setBackground(red);
        loginButton.setForeground(Color.white);
        loginButton.setFont(new Font("Bodoni MT", Font.PLAIN, 26));
        loginButton.setMaximumSize(new Dimension(290, 95));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);

        registerButton = new RoundedButton("register",15);
        registerButton.setBackground(red);
        registerButton.setForeground(Color.white);
        registerButton.setFont(customFont20);
        registerButton.setMaximumSize(new Dimension(290, 95));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFocusPainted(false);

        // --- Title Styling ---
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(customFont60);
        title.setBackground(Color.black);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setFont(customFont60);
        title2.setBackground(Color.black);

        // --- Assembly ---
        loginPanel.add(Box.createVerticalStrut(130));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(passwordField);

        loginPanel.add(Box.createVerticalStrut(75));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(registerButton);
        loginPanel.add(Box.createVerticalStrut(160));

        titlePanel.add(Box.createVerticalStrut(25));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(1));
        titlePanel.add(title2);

        panel.add(titlePanel);
        panel.add(loginPanel);
        panel.add(logo);

        hide();
    }

    @Override
    public JPanel getMainPanel() {
        return panel;
    }

    @Override
    public void show() {
        this.panel.setVisible(true);
        title.requestFocusInWindow();
    }

    @Override
    public void hide() {
        this.panel.setVisible(false);
    }

    // ==========================================
    //       GETTERS FOR CONTROLLER (MVC)
    // ==========================================
    
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public RoundedButton getLoginBtn() {
        return loginButton;
    }
    
    public RoundedButton getRegisterBtn() {
        return registerButton;
    }
}