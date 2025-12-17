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



public class LoginScreen implements View_t{

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color darkgrey = new Color(22, 20, 19);
    Color blue = Color.decode("#C2E5FF");
    Color lightblue = Color.decode("#BBE8E2");
    Color red = Color.decode("#D82F4B");
    Color darkred = Color.decode("#A02336");
    JPanel panel = new JPanel();


    public JLabel title = new JLabel("Καλως ήρθατε");
    public JLabel title2 = new JLabel("στην Bank of TUC");
    public JTextArea usrname = new JTextArea(20, 200);
    public JTextArea pword = new JTextArea(20,200);


    public void init(){


        // panel.setFont(new Font("Verdana", Font.BOLD, 48));
        // panel.setForeground(Color.white);
        panel.setBackground(Color.white); //E7E8E5
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setBorder(new EmptyBorder(-10, 0, 0, 0));
        panel.setLayout(null);
        panel.setBackground(blue);

                
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBounds(0,0,1600,220);
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        titlePanel.setBackground(Color.decode("#D6D8D7"));

        // --- Inner panel for login content ---
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBounds(GlobalConsts.wxCenter(wWidth, 800),220,800,560);
        loginPanel.setOpaque(false);
        loginPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        loginPanel.setBackground(Color.black);

        // --- Title label ---

        JLabel logo = new JLabel();
        logo.setBounds(0,0,1159,158);
        try{
            BufferedImage logoImg= ImageIO.read(new File("UML/App/View/Assets/logo_.png"));
            logo = new JLabel(new ImageIcon(logoImg));
        }catch(Exception e){

        }

        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Username field ---
        JTextField usernameField = new JTextField("Username: ");
        usernameField.setMaximumSize(new Dimension(380, 100));
        usernameField.setPreferredSize(new Dimension(380, 100));
        usernameField.setBackground(Color.white);
        usernameField.setForeground(Color.decode("#C6D3D0"));
        usernameField.setFont(new Font("Bodoni MT", Font.ITALIC, 20));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.transferFocus();
        OnFocusEventHelper.setOnFocusText(usernameField, "Username: ", Color.black, Color.decode("#C6D3D0"));

        
        // --- Password field ---
        JTextField passwordField = new JTextField("Password: ");
        passwordField.setMaximumSize(new Dimension(380, 100));
        passwordField.setPreferredSize(new Dimension(380, 100));
        passwordField.setBackground(Color.white);
        passwordField.setForeground(Color.decode("#C6D3D0"));
        passwordField.setFont(new Font("Bodoni MT", Font.ITALIC, 20));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.transferFocus();
        OnFocusEventHelper.setOnFocusText(passwordField, "Password: ", Color.black, Color.decode("#C6D3D0"));


        RoundedButton loginButton = new RoundedButton("login");
        loginButton.setBackground(red);
        loginButton.setForeground(Color.white); //93A09D
        loginButton.setFont(new Font("Bodoni MT", Font.PLAIN, 26));
        loginButton.setMaximumSize(new Dimension(290, 95));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFocusPainted(false);
        // OnClickEventHelper.setOnClickColor(loginButton, darkred, red);
        
        RoundedButton registerButton = new RoundedButton("register");
        registerButton.setBackground(red);
        registerButton.setForeground(Color.white);
        registerButton.setFont(new Font("Bodoni MT", Font.PLAIN, 26));
        registerButton.setMaximumSize(new Dimension(290, 95));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setFocusPainted(false);
        // OnClickEventHelper.setOnClickColor(registerButton, darkred, red);



        // (add titleLabel, fields, buttons... same as before)
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Verdana", Font.BOLD, 80));
        title.setBackground(Color.black);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setFont(new Font("Verdana", Font.BOLD, 80));
        title2.setBackground(Color.black);
        
        // loginPanel.add(Box.createVerticalStrut(200));
        // loginPanel.add(titleLabel);
        loginPanel.add(Box.createVerticalStrut(130));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(passwordField);

        loginPanel.add(Box.createVerticalStrut(75));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(15));
        loginPanel.add(registerButton);
        loginPanel.add(Box.createVerticalStrut(140+20));


        // titlePanel.add(Box.createVerticalStrut(20));
        titlePanel.add(Box.createVerticalStrut(25));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(1));
        titlePanel.add(title2);
        
        
        // // --- Add login panel to main panel (centered) ---
        // panel.add(loginPanel);
        
    
        panel.add(titlePanel);
        panel.add(loginPanel);


        hide();
    }

    public JPanel getMainPanel(){
        return panel;
    }

    public void changeColor(Color c){

        hide();

    }

    public void hide(){
        this.panel.setVisible(false);
    }

    public void show(){
        this.panel.setVisible(true);
        title.requestFocusInWindow();
    }
    
}
