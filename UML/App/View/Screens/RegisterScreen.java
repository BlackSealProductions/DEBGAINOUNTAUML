package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
// import Utils.GlobalConsts; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.stream.IntStream;

public class RegisterScreen implements View_t {

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();
    
    // Updated Resolution
    final int wWidth = 1600; 
    final int wHeight = 900; 

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    // --- 3. Form Components ---
    private JTextField nameField, surnameField, emailField, addressField, phoneField, citizenIdField;
    private JComboBox<String> dayBox, monthBox, yearBox;
    private String selectedUserType = "Individual";
    private JLabel titleLabel;

    @Override
    public void init() {
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setBorder(new EmptyBorder(40, 60, 40, 60)); // More padding for large screen

        // --- Title Section ---
        titleLabel = new JLabel("Εγγραφή Χρήστη", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 60)); // Larger font for 1600p
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(new EmptyBorder(0, 0, 50, 0)); // Push content down
        panel.add(titleLabel, BorderLayout.NORTH);

        // --- Main Content Container ---
        // 1 Row, 2 Cols, 100px gap between Left/Right panels
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 100, 0)); 
        contentPanel.setOpaque(false);

        // --- LEFT COLUMN: Form Fields ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Fix: Don't fill horizontally excessively. Center them.
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 15, 0); // Vertical spacing between fields
        gbc.gridx = 0;

        // Initialize Fields (Now wider but fixed)
        nameField = createStyledField("Name", gbc, leftPanel, 0);
        surnameField = createStyledField("Surname", gbc, leftPanel, 1);
        emailField = createStyledField("Email", gbc, leftPanel, 2);
        addressField = createStyledField("Address", gbc, leftPanel, 3);
        phoneField = createStyledField("Phone Number", gbc, leftPanel, 4);
        citizenIdField = createStyledField("Citizen ID", gbc, leftPanel, 5);

        contentPanel.add(leftPanel);

        // --- RIGHT COLUMN: Date, Type, Button ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.insets = new Insets(0, 0, 30, 0);

        // A. Date of Birth (Dropdowns)
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

        // B. User Type Selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        typePanel.setOpaque(false);

        JToggleButton indBtn = new JToggleButton("Ιδιώτης");
        JToggleButton comBtn = new JToggleButton("Επιχείρηση");
        styleToggleButton(indBtn);
        styleToggleButton(comBtn);

        ButtonGroup group = new ButtonGroup();
        group.add(indBtn);
        group.add(comBtn);
        indBtn.setSelected(true);

        indBtn.addActionListener(e -> selectedUserType = "Individual");
        comBtn.addActionListener(e -> selectedUserType = "Company");

        typePanel.add(indBtn);
        typePanel.add(comBtn);

        gbcRight.gridy = 2;
        gbcRight.insets = new Insets(50, 0, 50, 0); // More breathing room around buttons
        rightPanel.add(typePanel, gbcRight);

        // C. Register Button
        RoundedButton registerBtn = new RoundedButton("Register");
        registerBtn.setBackground(red);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Bodoni MT", Font.PLAIN, 32)); // Bigger font
        registerBtn.setPreferredSize(new Dimension(300, 70)); // Bigger button
        registerBtn.setFocusPainted(false);
        
        gbcRight.gridy = 3;
        rightPanel.add(registerBtn, gbcRight);

        contentPanel.add(rightPanel);
        panel.add(contentPanel, BorderLayout.CENTER);

        hide();
    }

    // --- Helpers for Styling ---

    private JTextField createStyledField(String placeholder, GridBagConstraints gbc, JPanel parent, int row) {
        JTextField field = new JTextField(placeholder);
        // FIX: Made them wider (450px) and taller (55px) to fit 1600x900 better
        field.setPreferredSize(new Dimension(450, 55)); 
        field.setFont(new Font("Bodoni MT", Font.ITALIC, 24)); // Larger text
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
        box.setFont(new Font("Bodoni MT", Font.PLAIN, 20)); // Larger font
        box.setPreferredSize(new Dimension(width, 40));
        return box;
    }

    private void styleToggleButton(JToggleButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 100)); // Larger toggle buttons
        btn.setFont(new Font("Bodoni MT", Font.PLAIN, 20));
    }

    // --- View_t Implementation ---

    @Override
    public JPanel getMainPanel() {
        return panel;
    }

    @Override
    public void show() {
        panel.setVisible(true);
        titleLabel.requestFocusInWindow(); 
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame("Test Register Screen");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        RegisterScreen screen = new RegisterScreen();
        screen.init(); 
        
        testFrame.setSize(1600, 900);
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
        testFrame.add(screen.getMainPanel());

        screen.show(); 
    }
}