package App.View.Screens;
import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RegisterUserScreen implements View_t{

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color bkgColor = Color.decode("#C2E5FF");
    Color darkgrey = new Color(22, 20, 19);
    Color blue = Color.decode("#C2E5FF");
    Color lightblue = Color.decode("#BBE8E2");
    Color red = Color.decode("#D82F4B");
    Color darkred = Color.decode("#A02336");

    public JTextArea usrname = new JTextArea(20, 200);
    public JTextArea pword = new JTextArea(20,200);



    private JPanel mainPanel = new JPanel();



    @Override
    public void init(){
        mainPanel.setBackground(bkgColor);
        mainPanel.setBounds(0,0,wWidth,wHeight);
        mainPanel.setBorder((new EmptyBorder(-10,0,0,0)));
        mainPanel.setLayout(null);


         // BoT logo 
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(50,50,200,200);

        // τιτλος
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(null);
        welcomePanel.setBounds(400,120,800,200);
        JLabel welcomeLabel = new JLabel("Εγγραφή Χρήστη");
        welcomeLabel.setFont(customFont60);
        welcomePanel.add(welcomeLabel);;

          // --- Username field ---
        JTextField usernameField = new JTextField("Username: ");
        usernameField.setMaximumSize(new Dimension(380, 50));
        usernameField.setPreferredSize(new Dimension(380, 10));
        usernameField.setBackground(Color.white);
        usernameField.setForeground(Color.decode("#C6D3D0"));
        usernameField.setFont(customFont20);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.transferFocus();
        OnFocusEventHelper.setOnFocusText(usernameField, "Username: ", Color.black, Color.decode("#C6D3D0"));

        
        // --- Password field ---
        JTextField passwordField = new JTextField("Password: ");
        passwordField.setMaximumSize(new Dimension(380, 50));
        passwordField.setPreferredSize(new Dimension(380, 10));
        passwordField.setBackground(Color.white);
        passwordField.setForeground(Color.decode("#C6D3D0"));
        passwordField.setFont(customFont20);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.transferFocus();
        OnFocusEventHelper.setOnFocusText(passwordField, "Password: ", Color.black, Color.decode("#C6D3D0"));


        // --- mail field ---
        JTextField mailField = new JTextField("Gmail: ");
        mailField.setMaximumSize(new Dimension(380, 50));
        mailField.setPreferredSize(new Dimension(380, 100));
        mailField.setBackground(Color.white);
        mailField.setForeground(Color.decode("#C6D3D0"));
        mailField.setFont(customFont20);
        mailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mailField.transferFocus();
        OnFocusEventHelper.setOnFocusText(mailField, "Gmail: ", Color.black, Color.decode("#C6D3D0"));
        
        RoundedButton registerButton = new RoundedButton("Εγγραφή",15);
        registerButton.setBackground(red);
        registerButton.setForeground(Color.white);
        registerButton.setFont(customFont20);
        registerButton.setMaximumSize(new Dimension(290, 95));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFocusPainted(false);
        // OnClickEventHelper.setOnClickColor(registerButton, darkred, red);


           // --- Inner panel for login content ---
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBounds(GlobalConsts.wxCenter(wWidth, 800),220,800,560);
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginPanel.setBackground(Color.black);

        // loginPanel.add(Box.createVerticalStrut(200));
        // loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(130));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(mailField);
        loginPanel.add(Box.createVerticalStrut(75));
        loginPanel.add(registerButton);

        mainPanel.add(logoPanel);
        mainPanel.add(welcomePanel);
        mainPanel.add(loginPanel);


    }

     @Override
    public JPanel getMainPanel(){
        return mainPanel;
    }

    @Override
    public void hide(){
        this.mainPanel.setVisible(false);
    }

    @Override
    public void show(){
        this.mainPanel.setVisible(true);
    }


    
}
