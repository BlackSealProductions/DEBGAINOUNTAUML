package App.View.Screens;

import App.Model.Database.UserDB;
import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class StandingOrdersScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);
    Font customFont16 = FontLoader.loadCustomFont(fontPath, 16f); // Used for list rows now
    Font customFont12 = FontLoader.loadCustomFont(fontPath, 12f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JPanel panel = new JPanel();

    // --- Colors ---
    Color blue = Color.decode("#C2E5FF");
    Color red = Color.decode("#D82F4B");
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    private JTextField nameField, ibanField, amountField;
    private JSpinner dateSpinner;
    private RoundedButton completeBtn;
    private JLabel balanceLabel;
    private JComboBox<String> freqBox;

    private int currRow = 1;
    private GridBagConstraints gbcForm;
    private JPanel listContainer;

    private JLabel titleLabel;
    private JScrollPane scrollPane;

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBounds(0, 0, wWidth, wHeight);

        // --- A. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 30, 0));

        // 1. Logo
        JPanel leftHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHead.setOpaque(false);
        leftHead.setPreferredSize(new Dimension(300, 100));
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        RoundedImage logoPanel = new RoundedImage(new ImageIcon(logo), 20);
        leftHead.add(logoPanel);

        // 2. Title
        titleLabel = new JLabel("Πάγιες Πληρωμές", SwingConstants.CENTER);
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

        // --- B. Split Content Panel ---
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        contentPanel.setOpaque(false);

        // --- LEFT COLUMN: Scrollable List ---
        JPanel leftColumn = new JPanel(new BorderLayout());
        leftColumn.setOpaque(false);

        JLabel listTitle = new JLabel("Ενεργές Πάγιες Πληρωμές", SwingConstants.LEFT);
        listTitle.setFont(customFont20);
        listTitle.setBorder(new EmptyBorder(0, 10, 15, 0));
        leftColumn.add(listTitle, BorderLayout.NORTH);

        listContainer = new JPanel(new GridBagLayout());
        listContainer.setOpaque(false);
    
        GridBagConstraints gbcList = new GridBagConstraints();
        gbcList.fill = GridBagConstraints.HORIZONTAL;
        gbcList.anchor = GridBagConstraints.NORTH;
    
        // 1. Add Headers immediately
        addListHeader(listContainer, gbcList);
    
        // 2. Add Glue immediately so the header starts at the top
        GridBagConstraints glueGbc = new GridBagConstraints();
        glueGbc.gridx = 0;
        glueGbc.gridy = 999; // Stays at the bottom
        glueGbc.weighty = 1.0;
        glueGbc.fill = GridBagConstraints.BOTH;
        listContainer.add(Box.createVerticalGlue(), glueGbc);

        scrollPane = new JScrollPane(listContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        leftColumn.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(leftColumn);

        // --- RIGHT COLUMN: Create Form ---
        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setOpaque(false);
        rightColumn.setBorder(new EmptyBorder(10, 20, 10, 20));

        gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.insets = new Insets(5, 0, 5, 0);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        JLabel formTitle = new JLabel("Δημιουργία Πάγιας Χρέωσης");
        formTitle.setFont(customFont20);
        gbcForm.gridy = 0;
        gbcForm.insets = new Insets(0, 0, 25, 0);
        rightColumn.add(formTitle, gbcForm);

        nameField = addFormField(rightColumn, "ΟΝΟΜΑ", "π.χ. Ενοίκιο", gbcForm, 1);
        ibanField = addFormField(rightColumn, "ΠΡΟΣ: ΛΟΓΑΡΙΑΣΜΟΣ (IBAN)", "GR...", gbcForm, 3);
        dateSpinner = addDateSpinner(rightColumn, "ΗΜΕΡΟΜΗΝΙΑ ΕΝΑΡΞΗΣ", gbcForm, 5);

        JLabel boxlabel = new JLabel("ΚΑΘΕ ΠΟΤΕ");
        boxlabel.setFont(customFont12);
        boxlabel.setForeground(Color.GRAY);
        gbcForm.gridy = 7;
        gbcForm.insets = new Insets(5, 0, 2, 0);
        rightColumn.add(boxlabel, gbcForm);

        String[] choices = {"ΜΗΝΑ", "ΧΡΟΝΟ"};
        freqBox = createStyledComboBox(choices, 300);
        gbcForm.gridy = 8;
        gbcForm.insets = new Insets(0, 0, 15, 0);
        rightColumn.add(freqBox, gbcForm);

        amountField = addFormField(rightColumn, "ΠΟΣΟ ΠΛΗΡΩΜΗΣ", "0.00", gbcForm, 9);

        completeBtn = new RoundedButton("Ολοκλήρωση", 15);
        completeBtn.setBackground(red);
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(customFont20);
        completeBtn.setPreferredSize(new Dimension(220, 55));
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

    // --- Helper Methods ---

    private JSpinner addDateSpinner(JPanel parent, String labelText, GridBagConstraints gbc, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(customFont12);
        label.setForeground(Color.DARK_GRAY);
        gbc.gridy = y;
        gbc.insets = new Insets(5, 0, 2, 0);
        parent.add(label, gbc);

        SpinnerDateModel model = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);

        JFormattedTextField tf = editor.getTextField();
        tf.setFont(customFont16);
        tf.setForeground(textColor);
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        spinner.setPreferredSize(new Dimension(300, 45));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(placeholderColor, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        spinner.setBackground(Color.WHITE);

        gbc.gridy = y + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        parent.add(spinner, gbc);
        return spinner;
    }

    private JComboBox<String> createStyledComboBox(String[] items, int width) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(customFont16);
        box.setPreferredSize(new Dimension(width, 45));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(placeholderColor, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 0)
        ));
        return box;
    }

    private JTextField addFormField(JPanel parent, String labelText, String placeholder, GridBagConstraints gbc, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(customFont12);
        label.setForeground(Color.DARK_GRAY);
        gbc.gridy = y;
        gbc.insets = new Insets(5, 0, 2, 0);
        parent.add(label, gbc);

        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(300, 45));
        field.setFont(customFont16);
        field.setForeground(placeholderColor);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(placeholderColor, 1),
            BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        OnFocusEventHelper.setOnFocusText(field, placeholder, textColor, placeholderColor);

        gbc.gridy = y + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        parent.add(field, gbc);
        return field;
    }

    public void addListRow2(String[] data) {
        // Remove the previous glue/spacer before adding a new row
        Component[] components = listContainer.getComponents();
        for (Component comp : components) {
            if (comp instanceof Box.Filler || (comp instanceof Box && ((Box)comp).getLayout() instanceof BoxLayout)) {
                 listContainer.remove(comp);
            }
        }
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = this.currRow;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Rows should not stretch vertically
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH; // Anchor to the top
        gbc.insets = new Insets(5, 10, 5, 10);
    
        // Load background image
        Image whiteImg = new ImageIcon(getClass().getResource("/App/View/Assets/white_ahh_image.png"))
                            .getImage().getScaledInstance(800, 100, Image.SCALE_SMOOTH);
        ImageIcon whiteIcon = new ImageIcon(whiteImg);
    
        RoundedImage rowCard = new RoundedImage(whiteIcon, 25);
        rowCard.setLayout(new GridLayout(1, 6, 5, 0)); 
        rowCard.setPreferredSize(new Dimension(620, 80)); // Reduced height slightly for better fit
        rowCard.setBorder(new EmptyBorder(0, 20, 0, 20));
    
        for (String d : data) {
            JLabel label = new JLabel(d);
            label.setFont(customFont16); 
            label.setForeground(textColor);
            rowCard.add(label);
        }
    
        this.listContainer.add(rowCard, gbc);
        this.currRow++;
    
        // Re-add the "Glue" at the very bottom to push everything up
        GridBagConstraints glueGbc = new GridBagConstraints();
        glueGbc.gridx = 0;
        glueGbc.gridy = this.currRow + 1;
        glueGbc.weighty = 1.0; // This eats all remaining vertical space
        glueGbc.fill = GridBagConstraints.BOTH;
        listContainer.add(Box.createVerticalGlue(), glueGbc);
    
        // listContainer.revalidate();
        // listContainer.repaint();
    }
    
    // --- UPDATED CLEAR METHOD ---
    public void clearListContainer() {

        SwingUtilities.invokeLater(() -> {
            scrollPane.getViewport().setViewPosition(new Point(0, 0));
        });

        this.listContainer.removeAll();
        this.currRow = 1;
        
        GridBagConstraints gbcList = new GridBagConstraints();
        gbcList.fill = GridBagConstraints.HORIZONTAL;
        gbcList.anchor = GridBagConstraints.NORTH;
        
        // Re-add Header
        addListHeader(this.listContainer, gbcList);
        
        // Re-add Glue to keep header at the top
        GridBagConstraints glueGbc = new GridBagConstraints();
        glueGbc.gridx = 0;
        glueGbc.gridy = 999;
        glueGbc.weighty = 1.0;
        glueGbc.fill = GridBagConstraints.BOTH;
        this.listContainer.add(Box.createVerticalGlue(), glueGbc);
        
        this.listContainer.revalidate();
        this.listContainer.repaint();
    }
    // --- UPDATED LIST HEADER ---
    private void addListHeader(JPanel parent, GridBagConstraints gbc) {
        // CHANGED: Added "ΕΠΟΜΕΝΗ" (Next Due)
        String[] headers = {"ΟΝΟΜΑ", "ΛΟΓΑΡΙΑΣΜΟΣ", "ΗΜΕΡΑ", "ΣΥΧΝΟΤΗΤΑ", "ΠΟΣΟ", "ΕΠΟΜΕΝΗ"};
        
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        // CHANGED: GridLayout columns from 5 to 6
        JPanel headerContainer = new JPanel(new GridLayout(1, 6, 5, 0));
        headerContainer.setOpaque(false);

        for (String h : headers) {
            JLabel label = new JLabel("<html><b>" + h + "</b></html>");
            label.setFont(customFont12);
            label.setForeground(Color.DARK_GRAY);
            headerContainer.add(label);
        }

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        parent.add(headerContainer, gbc);
    }

    public boolean validateInputs() {
        String name = nameField.getText().trim();
        String iban = ibanField.getText().trim();
        String amount = amountField.getText().trim();

        if (name.isEmpty() || name.equals("π.χ. Ενοίκιο") ||
            iban.isEmpty() || iban.equals("GR...") ||
            amount.isEmpty() || amount.equals("0.00")) {
            JOptionPane.showMessageDialog(panel, "Παρακαλώ συμπληρώστε όλα τα πεδία.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!iban.toUpperCase().contains("GR")) {
            JOptionPane.showMessageDialog(panel, "Το IBAN πρέπει να περιέχει 'GR'.", "Σφάλμα IBAN", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if date is in the past
        Date spinnerValue = (Date) dateSpinner.getValue();
        LocalDate selectedDate = spinnerValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();

        if (selectedDate.isBefore(today)) {
            JOptionPane.showMessageDialog(panel, "Η ημερομηνία δεν μπορεί να είναι στο παρελθόν.", "Σφάλμα Ημερομηνίας", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!amount.matches("^\\d+\\.\\d{2}$")) {
            JOptionPane.showMessageDialog(panel, "Το ποσό πρέπει να είναι της μορφής xx.yy (π.χ. 50.00).", "Σφάλμα Ποσού", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void warnIban(){
        JOptionPane.showMessageDialog(panel, "Μη-έγκυρο IBAN..", "Σφάλμα IBAN", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); titleLabel.requestFocusInWindow(); }
    @Override
    public void hide() { panel.setVisible(false); 
        clearListContainer();
    }

    public String getName() { return nameField.getText(); }
    public String getIban() { return ibanField.getText(); }
    
    public String getDate() { 
        Date date = (Date) dateSpinner.getValue();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    
    public String getFrequency() { return (String) freqBox.getSelectedItem(); }
    public String getAmount() { return amountField.getText(); }
    public RoundedButton getCompleteBtn() { return completeBtn; }
    
    public void setBalance(String amount) {
        try {
            double val = Double.parseDouble(amount);
            String formattedBalance = String.format("%.2f", val);
            balanceLabel.setText("<html><u>Υπόλοιπο: " + formattedBalance + "€</u></html>");
        } catch (NumberFormatException e) {
            balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
        }
    }
    
    public JComboBox<String> getFreqBox() { return freqBox; }

    
    public void resetRowCounter(){
        this.currRow=1;
    }
}