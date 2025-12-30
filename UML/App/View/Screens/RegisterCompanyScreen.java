package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterCompanyScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    private JPanel panel = new JPanel();
    
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    private JTextField emailField, phoneField, citizenIdField, usernameField, companyNameField;
    private JPasswordField passwordField;

    private JLabel titleLabel;
    private RoundedButton registerBtn;

    @Override
    public void init() {

        
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // --- Top Bar ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        titleLabel = new JLabel("Εγγραφή Επιχείρησης", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 60)); 
        titleLabel.setForeground(Color.BLACK);
        
        topPanel.add(titleLabel, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.NORTH);

        // --- Center Container for Form ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); 
        gbc.gridx = 0;

        // 1. Username
        usernameField = createStyledField("Username", gbc, centerWrapper, 0);
        
        // 2. Password 
        passwordField = new JPasswordField("Password");
        passwordField.setPreferredSize(new Dimension(450, 55));
        passwordField.setFont(customFont20);
        passwordField.setForeground(placeholderColor);
        passwordField.setEchoChar((char)0); 
        OnFocusEventHelper.setOnFocusText(passwordField, "Password", textColor, placeholderColor);
        gbc.gridy = 1;
        centerWrapper.add(passwordField, gbc);

        // 3. Company Name (New field under Password)
        companyNameField = createStyledField("Company Name", gbc, centerWrapper, 2);

        // 4. Other Fields
        emailField = createStyledField("Email", gbc, centerWrapper, 3);
        phoneField = createStyledField("Phone Number", gbc, centerWrapper, 4);
        citizenIdField = createStyledField("Citizen ID", gbc, centerWrapper, 5);

        // 5. Register Button (Placed at the bottom of the stack)
        registerBtn = new RoundedButton("Register", 15);
        registerBtn.setBackground(red);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(customFont20);
        registerBtn.setPreferredSize(new Dimension(300, 70));
        registerBtn.setFocusPainted(false);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(40, 0, 0, 0); // Extra space above the button
        gbc.fill = GridBagConstraints.NONE; // Don't stretch the button
        centerWrapper.add(registerBtn, gbc);

        panel.add(centerWrapper, BorderLayout.CENTER);

        hide();
    }

    // --- Helpers ---
    private JTextField createStyledField(String placeholder, GridBagConstraints gbc, JPanel parent, int row) {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(450, 55)); 
        field.setFont(customFont20); 
        field.setForeground(placeholderColor);
        field.setBackground(Color.WHITE);
        OnFocusEventHelper.setOnFocusText(field, placeholder, textColor, placeholderColor);
        gbc.gridy = row;
        parent.add(field, gbc);
        return field;
    }

    // --- View_t Implementation ---
    public JPanel getMainPanel() { return this.panel; }
    public void show() { this.panel.setVisible(true); this.titleLabel.requestFocusInWindow(); }
    public void hide() { this.panel.setVisible(false); }

    // --- GETTERS ---
    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getCompanyName() { return companyNameField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getCitizenId() { return citizenIdField.getText(); }
    public RoundedButton getRegisterBtn() { return registerBtn; }
}