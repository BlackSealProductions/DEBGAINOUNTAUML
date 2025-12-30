package App.View.Screens;

import App.View.View_t;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StatementsScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   // Main Background
    Color lightBlueCard = Color.decode("#E6F2FF"); // Lighter Blue for the Central Card
    
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


        // --- A. Header Section (Logo | Title | Balance) ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

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
        
        JLabel titleLabel = new JLabel("Κινήσεις Λογαριασμού");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 36)); 
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

        // --- B. Central Card Container ---
        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(lightBlueCard); // NEW COLOR HERE
        contentCard.setBorder(new EmptyBorder(20, 40, 20, 40)); 

        // 1. Wrapper for Header + List
        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(lightBlueCard); // NEW COLOR HERE
        
        // -- Column Headers --
        JPanel headerRow = new JPanel(new GridLayout(1, 4, 10, 0));
        headerRow.setBackground(lightBlueCard); // NEW COLOR HERE
        headerRow.setBorder(new EmptyBorder(0, 10, 5, 10)); 
        
        String[] headers = {"Ημερομηνία Συναλλαγής", "Αριθμός Συναλλαγής", "Γιάννης Τρανσακτορ", "Ποσό"};
        for (String h : headers) {
            JLabel l = new JLabel(h, SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.BOLD, 12));
            headerRow.add(l);
        }
        listWrapper.add(headerRow, BorderLayout.NORTH);

        // 2. Transaction List Panel
        transactionListPanel = new JPanel(new GridLayout(0, 1, 0, 0)); 
        transactionListPanel.setBackground(lightBlueCard); // NEW COLOR HERE

        // --- POPULATE DATA ---
        addTransaction("Τετάρτη 12/11/2025", "202511120993432782", "RIOT GAMES", "-25.00€");
        addTransaction("Τετάρτη 04/11/2025", "202511040954390940", "ΣΤΕΛΙΟΣ ΛΟΙΔΩΡΙΚΗΣ", "+69.00€");
        addTransaction("Τετάρτη 14/11/2025", "202511140948930574", "GUANG GUANG", "-ΟΛΑ ΤΑ ΕΥΡΩ€");
        addTransaction("Τετάρτη 32/11/2025", "202511320947671476", "ΛΕΣΧΗ TUC", "-2.60€");
        
        for(int i=0; i<6; i++) {
             addTransaction("Δευτέρα 01/01/2026", "TRANS_ID_" + i, "TEST USER", "+100.00€");
        }

        // 3. Scroll Pane
        JPanel aligner = new JPanel(new BorderLayout());
        aligner.setBackground(lightBlueCard); // NEW COLOR HERE
        aligner.add(transactionListPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(aligner);
        scrollPane.setBorder(null); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(lightBlueCard); // NEW COLOR HERE
        scrollPane.getViewport().setBackground(lightBlueCard); // Ensures the empty area is also blue
        
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
    private void addTransaction(String date, String id, String name, String amount) {
        boolean isNegative = amount.contains("-") || amount.contains("ΟΛΑ");
        Color bgLight = isNegative ? redLight : greenLight;
        Color bgDark  = isNegative ? redDark : greenDark;

        JPanel rowPanel = new JPanel(new GridLayout(1, 4, 15, 0)); 
        rowPanel.setBackground(bgLight);
        
        rowPanel.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 2), 
            new EmptyBorder(10, 10, 10, 10) 
        ));
        rowPanel.setPreferredSize(new Dimension(0, 70)); 

        rowPanel.add(createDarkCell(date, bgDark));
        rowPanel.add(createDarkCell(id, bgDark));
        rowPanel.add(createDarkCell(name, bgDark));
        rowPanel.add(createDarkCell(amount, bgDark));

        transactionListPanel.add(rowPanel);
    }

    private JLabel createDarkCell(String text, Color bg) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true); 
        label.setBackground(bg);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
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
        balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }



}