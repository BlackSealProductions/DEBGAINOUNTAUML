package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.FontLoader;
import App.View.helper_classes.RoundedImage;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import App.Model.Entities.OperationEntities.*;

public class StatementsScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);
    Font customFont12 = FontLoader.loadCustomFont(fontPath, 12f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    List<Statement> allStatements = new ArrayList<>();

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color lightBlueCard = Color.decode("#E6F2FF"); 
    
    Color redLight = Color.decode("#FFC0B6"); 
    Color redDark = Color.decode("#FF7661");  
    Color greenLight = Color.decode("#C9EFA5"); 
    Color greenDark = Color.decode("#75D665");  
    Color darkText = Color.decode("#333333");

    // --- 3. Components ---
    private JLabel balanceLabel;
    private JPanel transactionListPanel; 

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
        panel.setBounds(0, 0, wWidth, wHeight);

        // --- A. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // 1. LEFT Logo
        JPanel leftHead = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHead.setOpaque(false);
        leftHead.setPreferredSize(new Dimension(300, 100)); 
        
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(15,20,100,100);

        leftHead.add(logoPanel);

        // 2. CENTER Title
        JPanel centerHead = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerHead.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Κινήσεις Λογαριασμού");
        titleLabel.setFont(customFont40); 
        centerHead.add(titleLabel);

        // 3. RIGHT Balance
        JPanel rightHead = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHead.setOpaque(false);
        rightHead.setPreferredSize(new Dimension(300, 100)); 
        
        balanceLabel = new JLabel("<html><u>Υπόλοιπο: -- €</u></html>");
        balanceLabel.setFont(customFont20);
        balanceLabel.setForeground(Color.decode("#003366")); 
        rightHead.add(balanceLabel);

        headerPanel.add(leftHead, BorderLayout.WEST);
        headerPanel.add(centerHead, BorderLayout.CENTER);
        headerPanel.add(rightHead, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // --- B. Central Card Container ---
        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(lightBlueCard);
        contentCard.setBorder(new EmptyBorder(20, 40, 20, 40)); 

        // 1. Wrapper for Header + List
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(lightBlueCard);
        
        // -- Column Headers --
        // CHANGED: Added "Περιγραφή" and adjusted GridLayout to 5 columns
        JPanel headerRow = new JPanel(new GridLayout(1, 5, 10, 0));
        headerRow.setBackground(lightBlueCard);
        headerRow.setBorder(new EmptyBorder(0, 10, 5, 10)); 
        
        String[] headers = {"Ημερομηνία", "Αριθμός Συναλλαγής", "Ιωάννης Τρανζάκτορ", "Περιγραφή", "Ποσό"};
        for (String h : headers) {
            JLabel l = new JLabel(h, SwingConstants.CENTER);
            l.setFont(customFont12);
            headerRow.add(l);
        }
        listWrapper.add(headerRow, BorderLayout.NORTH);

        // 2. Transaction List Panel
        transactionListPanel = new JPanel(new GridLayout(0, 1, 0, 0)); 
        transactionListPanel.setBackground(lightBlueCard);
        
        // 3. Scroll Pane
        JPanel aligner = new JPanel(new BorderLayout());
        aligner.setBackground(lightBlueCard);
        aligner.add(transactionListPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(aligner);
        scrollPane.setBorder(null); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(lightBlueCard); 
        scrollPane.getViewport().setBackground(lightBlueCard);
        
        listWrapper.add(scrollPane, BorderLayout.CENTER);
        contentCard.add(listWrapper, BorderLayout.CENTER);

        // Center the card
        JPanel cardContainer = new JPanel(new GridBagLayout());
        cardContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 100, 20, 100); 
        
        cardContainer.add(contentCard, gbc);
        panel.add(cardContainer, BorderLayout.CENTER);

        hide();
    }

    // --- Helper: Create 2-Shade Transaction Row ---
    // CHANGED: Added 'description' parameter
    private void addTransaction(String date, String id, String name, String description, String amount) {
        boolean isNegative = amount.contains("-") || amount.contains("ΟΛΑ");
        Color bgLight = isNegative ? redLight : greenLight;
        Color bgDark  = isNegative ? redDark : greenDark;

        // CHANGED: GridLayout to 5 columns
        JPanel rowPanel = new JPanel(new GridLayout(1, 5, 15, 0)); 
        rowPanel.setBackground(bgLight);
        
        rowPanel.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 2), 
            new EmptyBorder(10, 10, 10, 10) 
        ));
        rowPanel.setPreferredSize(new Dimension(0, 70)); 

        rowPanel.add(createDarkCell(date, bgDark));
        rowPanel.add(createDarkCell(id, bgDark));
        rowPanel.add(createDarkCell(name, bgDark));
        rowPanel.add(createDarkCell(description, bgDark)); // ADDED: Description cell
        rowPanel.add(createDarkCell(amount, bgDark));

        transactionListPanel.add(rowPanel);
    }

    private JLabel createDarkCell(String text, Color bg) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true); 
        label.setBackground(bg);
        label.setFont(customFont12);
        label.setForeground(darkText);
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        return label;
    }
    
    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); }
    @Override
    public void hide() { panel.setVisible(false); }
    
    public void setBalance(String amount) {
        try {
            // Parse the string to a double to perform formatting
            // %.2f limits the output to exactly two decimal places
            double val = Double.parseDouble(amount);
            String formattedBalance = String.format("%.2f", val);
            balanceLabel.setText("<html><u>Υπόλοιπο: " + formattedBalance + "€</u></html>");
        } catch (NumberFormatException e) {
            // Fallback in case the string isn't a valid number
            balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
        }
    }

    public void giveAccStatements(List<Statement> statements) {
        transactionListPanel.removeAll(); 
        String sign = "";
        this.allStatements = statements;

        if (statements != null && !statements.isEmpty()) {
            for (Statement state : statements) {
                String type = state.getTransaction().getType();
                String desc = state.getTransaction().getDescription(); // Get Description

                if (desc == null) desc = "-"; // Safety check

                if ("receive".equalsIgnoreCase(type)) {
                    sign = "+";
                    addTransaction(
                        state.getDate().toString(), 
                        state.getStatementId(), 
                        state.getTransaction().getSenderId(), // Name
                        desc,                                 // Description
                        sign + String.valueOf(state.getTransaction().getAmount()) + "€"
                    );
                } else {
                    sign = "-";
                    addTransaction(
                        state.getDate().toString(), 
                        state.getStatementId(), 
                        state.getTransaction().getRecieverId(), // Name
                        desc,                                   // Description
                        sign + String.valueOf(state.getTransaction().getAmount()) + "€"
                    );
                }
            }
        } 
        
        transactionListPanel.revalidate();
        transactionListPanel.repaint();
    }
}