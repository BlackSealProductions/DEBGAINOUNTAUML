package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BillPaymentScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont30 = FontLoader.loadCustomFont(fontPath, 30f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JPanel panel = new JPanel();
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color borderRed = Color.decode("#A02336"); 
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    private JTextField rfCodeField;
    private JTextField amountField;
    private JLabel balanceLabel;
    private RoundedButton searchBtn;
    private RoundedButton completeBtn;

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBounds(0, 0, wWidth, wHeight);

        // --- A. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // 1. Logo
        JPanel leftHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHead.setOpaque(false);
        leftHead.setPreferredSize(new Dimension(300, 100));
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        RoundedImage logoPanel = new RoundedImage(new ImageIcon(logo), 20);
        leftHead.add(logoPanel);

        // 2. Title
        JLabel titleLabel = new JLabel("Πληρωμή Λογαριασμού", SwingConstants.CENTER);
        titleLabel.setFont(customFont40);

        // 3. Balance
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHead.setOpaque(false);
        rightHead.setPreferredSize(new Dimension(300, 100));
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: 0.00€</u></html>");
        balanceLabel.setFont(customFont20);
        balanceLabel.setForeground(Color.decode("#003366")); 
        rightHead.add(balanceLabel);

        headerPanel.add(leftHead, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightHead, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // --- B. Form Content ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); 

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        row1.setOpaque(false);
        
        // Use "RF" and "0.00" as keys for the helper to assign fields
        JPanel rfPanel = createLabeledFieldPanel("ΠΡΟΣ: RF Λογαριασμός", "RF code", 400);
        JPanel amountPanel = createLabeledFieldPanel("Ποσό:", "0.00", 200);
        
        row1.add(rfPanel);
        row1.add(amountPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(row1, gbc);

        searchBtn = new RoundedButton("Αναζήτηση Λογαριασμού", 15);
        searchBtn.setBackground(Color.decode("#003366"));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(customFont20);
        searchBtn.setPreferredSize(new Dimension(350, 55));
        gbc.gridy = 1;
        contentPanel.add(searchBtn, gbc);

        completeBtn = new RoundedButton("Ολοκλήρωση", 15);
        completeBtn.setBackground(red);
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(customFont30);
        completeBtn.setPreferredSize(new Dimension(350, 65));
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 0, 0, 0); 
        contentPanel.add(completeBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);
        hide();
    }

    private JPanel createLabeledFieldPanel(String labelText, String placeholder, int width) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(customFont20);
        label.setForeground(Color.DARK_GRAY);
        
        JTextField field = new JTextField(placeholder);
        // field.setText(placeholder);
        field.setPreferredSize(new Dimension(width, 60)); 
        field.setMaximumSize(new Dimension(width, 60)); 
        field.setFont(customFont20); 
        field.setForeground(textColor);
        field.setBackground(Color.decode("#F8F8F8"));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderRed, 2, true), 
            new EmptyBorder(0, 15, 0, 0) 
        ));

        OnFocusEventHelper.setOnFocusText(field, placeholder, textColor, placeholderColor);

        // CRITICAL FIX: Ensure placeholders match the calls in init()
        if (placeholder.contains("RF")) this.rfCodeField = field;
        else if (placeholder.equals("0.00")) {
            this.amountField = field;
            this.amountField.setEditable(false);
        }

        container.add(label);
        container.add(Box.createVerticalStrut(8));
        container.add(field);
        return container;
    }

    public void setAmountField(String text) { this.amountField.setText(text); }
    public String getRFCode() { return rfCodeField.getText(); }
    public String getAmount() { return amountField.getText(); }
    public RoundedButton getCompleteBtn() { return completeBtn; }
    public RoundedButton getSearchBtn() { return searchBtn; }
    public JPanel getMainPanel() { return panel; }
    public void show() { panel.setVisible(true); }
    public void hide() { panel.setVisible(false); }

    public void setBalance(String amount) {
        try {
            double val = Double.parseDouble(amount);
            balanceLabel.setText("<html><u>Υπόλοιπο: " + String.format("%.2f", val) + "€</u></html>");
        } catch (Exception e) { balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>"); }
    }

    public void clearFields(){

        String rfPlaceholder = "RF code";
        String amountPlaceholder = "0.00";

        this.rfCodeField.setText(rfPlaceholder);
        this.rfCodeField.setForeground(placeholderColor);

        this.amountField.setText(amountPlaceholder);
        this.amountField.setForeground(placeholderColor);

    }
}