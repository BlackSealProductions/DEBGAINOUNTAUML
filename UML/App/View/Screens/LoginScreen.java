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
    Color placeholderColor = Color.decode("#C6D3D0");

    JPanel panel = new JPanel();

    // --- 1. Promote these to Class Variables so Getters can see them ---
    private JTextField usernameField;
    private JPasswordField passwordField; // Changed to PasswordField for security
    private RoundedButton loginButton;
    private RoundedButton registerButton;
    
    public JLabel title = new JLabel("Είσοδος Χρήστη");

    @Override
    public void init() {
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(null);
        panel.setBackground(blue);

        // --- Logo Positioning ---
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png"))
                        .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon, 25);
        // Positioned 40px from top and left
        logoPanel.setBounds(50, 50, 200, 200);
        panel.add(logoPanel);

        // --- Header Section ---
        title.setBounds(0, 45, wWidth, 70);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(customFont60);


        panel.add(title);

        // --- Layout Variables ---
        int fieldWidth = 450;
        int fieldHeight = 75; // Taller fields
        int buttonWidth = 320;
        int buttonHeight = 80; // Bigger buttons
        
        // Using your wxCenter utility for horizontal alignment
        int centerX = Utils.GlobalConsts.wxCenter(wWidth, fieldWidth);
        int buttonX = Utils.GlobalConsts.wxCenter(wWidth, buttonWidth);
        int startY = 280+70; 

        // --- Username field ---
        String userPlaceholder = "Username";
        usernameField = new JTextField(userPlaceholder);
        usernameField.setBounds(centerX, startY, fieldWidth, fieldHeight);
        usernameField.setBackground(Color.white);
        usernameField.setForeground(placeholderColor);
        usernameField.setFont(customFont20);
        usernameField.setBorder(new EmptyBorder(0, 15, 0, 15));
        OnFocusEventHelper.setOnFocusText(usernameField, userPlaceholder, Color.black, placeholderColor);

        // --- Password field ---
        String passPlaceholder = "Password";
        passwordField = new JPasswordField(passPlaceholder);
        passwordField.setEchoChar((char) 0);
        passwordField.setBounds(centerX, startY + 90, fieldWidth, fieldHeight); // Tight 15px gap (90 - 75)
        passwordField.setBackground(Color.white);
        passwordField.setForeground(placeholderColor);
        passwordField.setFont(customFont20);
        passwordField.setBorder(new EmptyBorder(0, 15, 0, 15));
        OnFocusEventHelper.setOnFocusText(passwordField, passPlaceholder, Color.black, placeholderColor);

        // --- Login Button ---
        loginButton = new RoundedButton("login", 20);
        loginButton.setBounds(centerX, startY + 190, 220, buttonHeight);
        loginButton.setBackground(red);
        loginButton.setForeground(Color.white);
        loginButton.setFont(customFont20);
        loginButton.setFocusPainted(false);

        // --- Register Button ---
        registerButton = new RoundedButton("register", 20);
        registerButton.setBounds(centerX+220+10, startY + 190, 220, buttonHeight);
        registerButton.setBackground(red);
        registerButton.setForeground(Color.white);
        registerButton.setFont(customFont20);
        registerButton.setFocusPainted(false);

        // Assembly
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        hide();
    }

    public void clearFields(){
        String userPlaceholder = "Username";
        usernameField.setText(userPlaceholder);
        // usernameField.setBounds(centerX, startY, fieldWidth, fieldHeight);
        // usernameField.setBackground(Color.white);
        usernameField.setForeground(placeholderColor);
        // usernameField.setFont(customFont20);
        // usernameField.setBorder(new EmptyBorder(0, 15, 0, 15));
        // OnFocusEventHelper.setOnFocusText(usernameField, userPlaceholder, Color.black, placeholderColor);

        // --- Password field ---
        String passPlaceholder = "Password";
        passwordField.setText(passPlaceholder);
        // passwordField.setEchoChar((char) 0);
        // passwordField.setBounds(centerX, startY + 90, fieldWidth, fieldHeight); // Tight 15px gap (90 - 75)
        // passwordField.setBackground(Color.white);
        passwordField.setForeground(placeholderColor);
        // passwordField.setFont(customFont20);
        // passwordField.setBorder(new EmptyBorder(0, 15, 0, 15));
        // OnFocusEventHelper.setOnFocusText(passwordField, passPlaceholder, Color.black, placeholderColor);
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