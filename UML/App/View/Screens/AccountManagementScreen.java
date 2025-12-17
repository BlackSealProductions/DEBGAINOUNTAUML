package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.RoundedButton; // Using your custom button for "Change"

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AccountManagementScreen implements View_t {

    // --- 1. View_t Essentials ---
    private JPanel panel = new JPanel();
    final int wWidth = 1200; 
    final int wHeight = 800;

    // --- 2. Colors ---
    Color blue = Color.decode("#C2E5FF");   // Main Background
    Color rowBg = Color.decode("#DCE6F1");  // Light Grey-Blue for the info rows
    Color borderColor = Color.decode("#A02336"); // The deep red border from Figma
    Color buttonGreen = Color.decode("#5CB85C"); // A distinct green for "Change" button

    // --- 3. Components (for Controller access) ---
    private RoundedButton changeNameBtn;
    private RoundedButton changeOwnerBtn;
    private JLabel balanceLabel;

    @Override
    public void init() {
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // --- A. Header Section (Standard) ---
        JPanel headerPanel = new JPanel(new BorderLayout()); 
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 30, 0));

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
        JLabel titleLabel = new JLabel("Διαχείριση Λογαριασμού");
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

        // --- B. Main Content ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(0, 100, 0, 100)); // Side padding to center the list

        // 1. Sub-Header
        JLabel subTitle = new JLabel("Πληροφορίες Λογαριασμού:");
        subTitle.setFont(new Font("Verdana", Font.PLAIN, 24));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subTitle);
        contentPanel.add(Box.createVerticalStrut(30)); // Gap

        // 2. Info Rows
        // We create them one by one using a helper
        contentPanel.add(createRow("• IBAN: GR1770137038471374039483827", null));
        contentPanel.add(Box.createVerticalStrut(20));

        // Create the buttons first so we can assign them to the class variables
        changeNameBtn = createSmallButton("αλλαγή");
        contentPanel.add(createRow("• Όνομα Κατόχου: Ιωάννης Ιοστάριος", changeNameBtn));
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createRow("• Επιτόκιο: 5%", null));
        contentPanel.add(Box.createVerticalStrut(20));

        changeOwnerBtn = createSmallButton("αλλαγή");
        contentPanel.add(createRow("• Δευτερεύοντας Κάτοχος: Γύρος Ζέπελης", changeOwnerBtn));
        
        // Push everything up
        contentPanel.add(Box.createVerticalGlue());

        panel.add(contentPanel, BorderLayout.CENTER);

        hide();
    }

    // --- Helper: Create a styled row ---
    private JPanel createRow(String text, RoundedButton button) {
        // Use BorderLayout to separate Text (Left) and Button (Right)
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(rowBg);
        row.setMaximumSize(new Dimension(1000, 60)); // Fixed height
        row.setPreferredSize(new Dimension(800, 60));
        
        // The Red Border
        row.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 2),
            new EmptyBorder(0, 20, 0, 20) // Padding inside the border
        ));

        // Label
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.BLACK);
        row.add(label, BorderLayout.CENTER);

        // Button (if exists)
        if (button != null) {
            // Wrap in a flow layout so the button doesn't stretch vertically
            JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10)); // Vertical center
            btnContainer.setOpaque(false);
            btnContainer.add(button);
            row.add(btnContainer, BorderLayout.EAST);
        }

        return row;
    }

    private RoundedButton createSmallButton(String text) {
        RoundedButton btn = new RoundedButton(text);
        btn.setBackground(buttonGreen);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(100, 35)); // Small, distinct size
        btn.setFocusPainted(false);
        return btn;
    }

    // --- View_t Implementation ---
    @Override
    public JPanel getMainPanel() { return panel; }
    @Override
    public void show() { panel.setVisible(true); }
    @Override
    public void hide() { panel.setVisible(false); }

    // --- Getters for Controller ---
    public RoundedButton getChangeNameBtn() { return changeNameBtn; }
    public RoundedButton getChangeOwnerBtn() { return changeOwnerBtn; }
    public void setBalance(String amount) {
        balanceLabel.setText("<html><u>Υπόλοιπο: " + amount + "€</u></html>");
    }


}