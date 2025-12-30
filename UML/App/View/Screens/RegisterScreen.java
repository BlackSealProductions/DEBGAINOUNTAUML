package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts; // Make sure this import works

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.stream.IntStream;

public class RegisterScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();
    
    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    // --- 3. Form Components ---
    // Make sure all these are Class Variables!
    private JTextField nameField, surnameField, emailField, addressField, phoneField, citizenIdField;
    // You also need Username/Password fields for registration!
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JComboBox<String> dayBox, monthBox, yearBox;
    private String selectedUserType = "Individual";
    private JLabel titleLabel;
    
    // FIX 1: Define buttons here so Getters can see them
    private RoundedButton registerBtn;
    private RoundedButton backBtn;

    @Override
    public void init() {
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // --- Top Bar (Back Button + Title) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        
        titleLabel = new JLabel("Εγγραφή Χρήστη", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 60)); 
        titleLabel.setForeground(Color.BLACK);
        
        // topPanel.add(backContainer, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);

        // --- Main Content Container ---
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 100, 0)); 
        contentPanel.setOpaque(false);

        // --- LEFT COLUMN: Form Fields ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // Reduced spacing slightly to fit more fields
        gbc.gridx = 0;

        // Initialize Fields
        // Added Username/Password fields because they are needed for login!
        usernameField = createStyledField("Username", gbc, leftPanel, 0);
        
        // Create Password Field manually since it's unique
        passwordField = new JPasswordField("Password");
        passwordField.setPreferredSize(new Dimension(450, 55));
        passwordField.setFont(new Font("Bodoni MT", Font.ITALIC, 24));
        passwordField.setForeground(placeholderColor);
        passwordField.setEchoChar((char)0); // Visible placeholder
        OnFocusEventHelper.setOnFocusText(passwordField, "Password", textColor, placeholderColor);
        gbc.gridy = 1;
        leftPanel.add(passwordField, gbc);

        nameField = createStyledField("Name", gbc, leftPanel, 2);
        surnameField = createStyledField("Surname", gbc, leftPanel, 3);
        emailField = createStyledField("Email", gbc, leftPanel, 4);
        phoneField = createStyledField("Phone Number", gbc, leftPanel, 5);
        citizenIdField = createStyledField("Tax ID", gbc, leftPanel, 6);

        contentPanel.add(leftPanel);

        // --- RIGHT COLUMN: Date, Type, Button ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.insets = new Insets(0, 0, 30, 0);

        // A. Date of Birth
        JLabel dobLabel = new JLabel("ΗΜΕΡΟΜΗΝΙΑ ΓΕΝΝΗΣΗΣ");
        dobLabel.setFont(new Font("Bodoni MT", Font.BOLD, 18));
        gbcRight.gridy = 0;
        rightPanel.add(dobLabel, gbcRight);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        datePanel.setOpaque(false);
        
        String[] days = IntStream.rangeClosed(1, 31).mapToObj(String::valueOf).toArray(String[]::new);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] years = IntStream.rangeClosed(1950, 2023).mapToObj(String::valueOf).sorted((a, b) -> b.compareTo(a)).toArray(String[]::new);

        dayBox = createStyledComboBox(days, 80);
        monthBox = createStyledComboBox(months, 100);
        yearBox = createStyledComboBox(years, 100);

        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);

        gbcRight.gridy = 1;
        rightPanel.add(datePanel, gbcRight);


        registerBtn = new RoundedButton("Register",15);
        registerBtn.setBackground(red);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Bodoni MT", Font.PLAIN, 32));
        registerBtn.setPreferredSize(new Dimension(300, 70));
        registerBtn.setFocusPainted(false);
        
        gbcRight.gridy = 3;
        rightPanel.add(registerBtn, gbcRight);

        contentPanel.add(rightPanel);
        panel.add(contentPanel, BorderLayout.CENTER);

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

    private JComboBox<String> createStyledComboBox(String[] items, int width) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setBackground(Color.WHITE);
        box.setFont(new Font("Bodoni MT", Font.PLAIN, 20));
        box.setPreferredSize(new Dimension(width, 40));
        return box;
    }

    private void styleToggleButton(JToggleButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 100));
        btn.setFont(new Font("Bodoni MT", Font.PLAIN, 20));
    }

    // --- View_t Implementation ---

    public JPanel getMainPanel() { return this.panel; }
    public void show() { this.panel.setVisible(true); this.titleLabel.requestFocusInWindow(); }
    public void hide() { this.panel.setVisible(false); }

    // --- GETTERS ---

    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getName() { return nameField.getText(); }
    public String getSurname() { return surnameField.getText(); }
    public String getCitizenId() { return citizenIdField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getAddress() { return addressField.getText(); }
    public String getPhone() { return phoneField.getText(); }

    public RoundedButton getRegisterBtn() { return registerBtn; } 
    public RoundedButton getBackBtn() { return backBtn; }
}   
