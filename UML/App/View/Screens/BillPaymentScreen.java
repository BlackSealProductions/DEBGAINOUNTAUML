package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BillPaymentScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();
    // final int wWidth = 1200; 
    // final int wHeight = 800;

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color borderRed = Color.decode("#A02336"); 
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    // --- 3. Components ---
    private JTextField rfCodeField;
    private JTextField amountField;
    private JTextField timeField;
    private RoundedButton completeBtn;
    private JLabel balanceLabel;

    @Override
    public void init() {
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBounds(0, 0, wWidth, wHeight);


        // --- A. Header Section (Logo | Title | Balance) ---
        // FIX: Use BorderLayout, but set FIXED preferred widths for Left/Right panels.
        // This ensures the Center gets all remaining space (fixing clipping) 
        // while remaining perfectly centered (because borders are equal).
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // 1. LEFT PANEL (Logo) - Fixed Width 300px
        JPanel leftHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHead.setOpaque(false);
        leftHead.setPreferredSize(new Dimension(300, 100)); // Explicit width
        
        JLabel logoLabel = new JLabel();
        try {
            BufferedImage logoImg = ImageIO.read(new File("App/View/Assets/logo.png")); 
            Image scaled = logoImg.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } catch (IOException e) {
            logoLabel.setText("[LOGO]"); 
            logoLabel.setForeground(Color.RED); 
        }
        leftHead.add(logoLabel);

        // 2. CENTER PANEL (Title) - Takes remaining space
        JPanel centerHead = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerHead.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Πληρωμή Λογαριασμού");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 40)); 
        centerHead.add(titleLabel);

        // 3. RIGHT PANEL (Balance) - Fixed Width 300px (Matches Left)
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHead.setOpaque(false);
        rightHead.setPreferredSize(new Dimension(300, 100)); // Explicit width matches Left
        
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: 67.69€</u></html>");
        balanceLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        balanceLabel.setForeground(Color.decode("#003366")); 
        rightHead.add(balanceLabel);

        // Add to Header
        headerPanel.add(leftHead, BorderLayout.WEST);
        headerPanel.add(centerHead, BorderLayout.CENTER);
        headerPanel.add(rightHead, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // --- B. Center Form Content (unchanged) ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0); 

        // 1. RF Code Section
        JPanel rfPanel = createLabeledFieldPanel("ΠΡΟΣ: Λογαριασμός (25 ψηφία RF)", "RF code", 600);
        gbc.gridy = 0;
        contentPanel.add(rfPanel, gbc);

        // 2. Amount & Time Section
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        row2.setOpaque(false);
        
        JPanel amountPanel = createLabeledFieldPanel("Ποσό:", "3.00€", 280);
        JPanel timePanel = createLabeledFieldPanel("Ώρα χρέωσης:", "13:00", 280);
        
        row2.add(amountPanel);
        row2.add(timePanel);

        gbc.gridy = 1;
        contentPanel.add(row2, gbc);

        // 3. Complete Button
        completeBtn = new RoundedButton("Ολοκλήρωση",15);
        completeBtn.setBackground(red);
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Bodoni MT", Font.PLAIN, 26));
        completeBtn.setPreferredSize(new Dimension(300, 65));
        completeBtn.setFocusPainted(false);

        gbc.gridy = 2;
        gbc.insets = new Insets(60, 0, 0, 0); 
        contentPanel.add(completeBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);

        hide();
    }

    // --- Helpers ---

    private JPanel createLabeledFieldPanel(String labelText, String placeholder, int width) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.CENTER_ALIGNMENT); 

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Bodoni MT", Font.PLAIN, 16));
        label.setForeground(Color.DARK_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Field
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(width, 60)); 
        field.setMaximumSize(new Dimension(width, 60)); 
        field.setFont(new Font("Bodoni MT", Font.BOLD, 22)); 
        field.setForeground(textColor);
        field.setBackground(Color.decode("#F8F8F8")); 
        field.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        // Red Border
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderRed, 2, true), 
            new EmptyBorder(0, 15, 0, 0) 
        ));

        // Placeholders
        OnFocusEventHelper.setOnFocusText(field, placeholder, textColor, placeholderColor);

        if (placeholder.contains("RF")) this.rfCodeField = field;
        else if (placeholder.contains("3.00")) this.amountField = field;
        else if (placeholder.contains("13:00")) this.timeField = field;

        container.add(label);
        container.add(Box.createVerticalStrut(8));
        container.add(field);

        return container;
    }

    public JPanel getMainPanel() {
        return this.panel;
    }

    public void show() {
        this.panel.setVisible(true);
        // panel.requestFocusInWindow();
    }

    public void hide() {
        this.panel.setVisible(false);
    }

    public String getRFCode() { return rfCodeField.getText(); }
    public String getAmount() { return amountField.getText(); }
    public String getPaymentTime() { return timeField.getText(); }
    public RoundedButton getCompleteBtn() { return completeBtn; }
    public void setBalance(String amount) {
        this.balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }


}