package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StandingOrdersScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color formBg = Color.decode("#FFE8B6"); 
    Color red = Color.decode("#D82F4B");    
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    // --- 3. Form Components ---
    private JTextField nameField, ibanField, dateField, frequencyField, amountField;
    private RoundedButton completeBtn;
    private JLabel balanceLabel;

    @Override
    public void init() {
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBounds(0, 0, wWidth, wHeight);


        // --- A. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // 1. LEFT Logo
        JPanel leftHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHead.setOpaque(false);
        leftHead.setPreferredSize(new Dimension(300, 100)); 
        
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

        // 2. CENTER Title
        JPanel centerHead = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerHead.setOpaque(false);
        JLabel titleLabel = new JLabel("Πάγιες Πληρωμές");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 40)); 
        centerHead.add(titleLabel);

        // 3. RIGHT Balance
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHead.setOpaque(false);
        rightHead.setPreferredSize(new Dimension(300, 100)); 
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: 67.69€</u></html>");
        balanceLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        balanceLabel.setForeground(Color.decode("#003366")); 
        rightHead.add(balanceLabel);

        headerPanel.add(leftHead, BorderLayout.WEST);
        headerPanel.add(centerHead, BorderLayout.CENTER);
        headerPanel.add(rightHead, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // --- B. Split Content Panel ---
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 40, 0)); 
        contentPanel.setOpaque(false);

        // --- LEFT COLUMN: Scrollable List ---
        JPanel leftColumn = new JPanel(new BorderLayout());
        leftColumn.setOpaque(false);
        
        JLabel listTitle = new JLabel("Ενεργές Πάγιες Πληρωμές", SwingConstants.CENTER);
        listTitle.setFont(new Font("Verdana", Font.BOLD, 22));
        listTitle.setBorder(new EmptyBorder(0,0,10,0));
        leftColumn.add(listTitle, BorderLayout.NORTH);

        // 1. The Container for rows (GridBagLayout)
        JPanel listContainer = new JPanel(new GridBagLayout());
        listContainer.setOpaque(false); // Make transparent
        GridBagConstraints gbcList = new GridBagConstraints();
        gbcList.fill = GridBagConstraints.HORIZONTAL;
        gbcList.insets = new Insets(10, 5, 10, 5); 
        
        // 2. Add Headers
        addListHeader(listContainer, gbcList);

        // 3. Add Many Rows (Simulating scrolling)
        addListRow(listContainer, gbcList, 1, "Ρεύμα", "9807410239...", "1η του Μήνα", "Μηνιαία", "50€");
        addListRow(listContainer, gbcList, 2, "Netflix", "7663456243...", "1η του Μήνα", "Μηνιαία", "12€");
        
        // Loop to create dummy data so you can test scrolling
        for (int i = 3; i < 20; i++) {
             addListRow(listContainer, gbcList, i, "Test " + i, "GR123456...", "15η του Μήνα", "Μηνιαία", (i*10)+"€");
        }
        
        // Push content to top
        GridBagConstraints spacer = new GridBagConstraints();
        spacer.gridy = 100;
        spacer.weighty = 1.0;
        listContainer.add(Box.createVerticalGlue(), spacer);

        // 4. THE SCROLL PANE WRAPPER
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false); // Transparent background
        scrollPane.setBorder(null); // Remove ugly border
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling speed

        leftColumn.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(leftColumn);

        // --- RIGHT COLUMN: Create Form (Unchanged) ---
        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setBackground(formBg); 
        rightColumn.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.insets = new Insets(10, 0, 10, 0);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Δημιουργία Πάγιας Χρέωσης");
        formTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 30, 0); 
        rightColumn.add(formTitle, gbcForm);

        gbcForm.insets = new Insets(5, 0, 15, 0);

        nameField = addFormField(rightColumn, "ΟΝΟΜΑ", "name", gbcForm, 1);
        ibanField = addFormField(rightColumn, "ΠΡΟΣ: ΛΟΓΑΡΙΑΣΜΟΣ", "IBAN", gbcForm, 3);
        dateField = addFormField(rightColumn, "ΗΜΕΡΟΜΗΝΙΑ ΠΛΗΡΩΜΗΣ", "date", gbcForm, 5);
        frequencyField = addFormField(rightColumn, "ΚΑΘΕ ΠΟΤΕ", "ΜΕΡΑ / ΜΗΝΑ / ΧΡΟΝΟΣ", gbcForm, 7);
        amountField = addFormField(rightColumn, "ΠΟΣΟ ΠΛΗΡΩΜΗΣ", "$", gbcForm, 9);

        completeBtn = new RoundedButton("Ολοκλήρωση",15);
        completeBtn.setBackground(red);
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Bodoni MT", Font.PLAIN, 20));
        completeBtn.setPreferredSize(new Dimension(200, 50));
        completeBtn.setFocusPainted(false);

        gbcForm.gridy = 11;
        gbcForm.insets = new Insets(30, 0, 0, 0);
        gbcForm.fill = GridBagConstraints.NONE; 
        gbcForm.anchor = GridBagConstraints.CENTER;
        rightColumn.add(completeBtn, gbcForm);

        contentPanel.add(rightColumn);
        panel.add(contentPanel, BorderLayout.CENTER);

        hide();
    }

    // --- Helpers ---

    private JTextField addFormField(JPanel parent, String labelText, String placeholder, GridBagConstraints gbc, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Bodoni MT", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        gbc.gridy = y;
        parent.add(label, gbc);

        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Bodoni MT", Font.ITALIC, 16));
        field.setForeground(placeholderColor);
        field.setBackground(Color.WHITE);
        OnFocusEventHelper.setOnFocusText(field, placeholder, textColor, placeholderColor);
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 0)); 

        gbc.gridy = y + 1;
        parent.add(field, gbc);
        return field;
    }

    private void addListHeader(JPanel parent, GridBagConstraints gbc) {
        String[] headers = {"ΟΝΟΜΑ", "ΠΡΟΣ ΛΟΓΑΡΙΑΣΜΟΣ", "ΜΕΡΑ ΠΛΗΡΩΜΗΣ", "ΤΥΠΟΣ ΠΛΗΡΩΜΗΣ", "ΠΟΣΟ"};
        gbc.gridy = 0;
        int x = 0;
        for (String h : headers) {
            JLabel label = new JLabel("<html><b>" + h + "</b></html>");
            label.setFont(new Font("SansSerif", Font.PLAIN, 11));
            gbc.gridx = x++;
            gbc.weightx = (x == 2) ? 2.0 : 1.0; 
            parent.add(label, gbc);
        }
    }

    private void addListRow(JPanel parent, GridBagConstraints gbc, int row, String name, String acc, String date, String type, String amount) {
        gbc.gridy = row;
        String[] data = {name, acc, date, type, amount};
        int x = 0;
        for (String d : data) {
            JLabel label = new JLabel(d);
            label.setFont(new Font("SansSerif", Font.PLAIN, 12));
            gbc.gridx = x++;
            gbc.weightx = (x == 2) ? 2.0 : 1.0;
            parent.add(label, gbc);
        }
    }

    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); panel.requestFocusInWindow(); }
    @Override
    public void hide() { panel.setVisible(false); }

    public String getName() { return nameField.getText(); }
    public String getIban() { return ibanField.getText(); }
    public String getDate() { return dateField.getText(); }
    public String getFrequency() { return frequencyField.getText(); }
    public String getAmount() { return amountField.getText(); }
    public RoundedButton getCompleteBtn() { return completeBtn; }
    public void setBalance(String amount) {
        balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }



}