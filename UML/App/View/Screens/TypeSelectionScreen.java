package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TypeSelectionScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JPanel panel = new JPanel();
    
    // --- Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    

    private RoundedButton individualBtn;
    private RoundedButton companyBtn;

    // @Override
    public void init() {
        // --- Setup Main Panel ---
        
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);
        panel.setLayout(new BorderLayout()); 

        // --- 1. Top Logo Section ---
        // JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 40));
        // topPanel.setOpaque(false);
        
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(50,50,200,200);
        // topPanel.add(logoPanel);

        panel.add(logoPanel);

        // --- 2. Center Selection Section ---
        // Using BoxLayout to stack Title and Buttons vertically
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Add spacing at the top to push content down
        centerPanel.add(Box.createVerticalGlue());

        // Title Text
        JLabel titleLabel = new JLabel("Εγγραφή Χρήστη");
        titleLabel.setFont(customFont40);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        // Subtitle
        JLabel subTitle = new JLabel("Επιλέξτε τύπο λογαριασμού:");
        subTitle.setFont(customFont20);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(20)); // Spacing
        centerPanel.add(subTitle);

        centerPanel.add(Box.createVerticalStrut(50)); // Spacing before buttons

        // Buttons Panel
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonsRow.setOpaque(false);
        buttonsRow.setMaximumSize(new Dimension(wWidth, 200));

        individualBtn = new RoundedButton("Ιδιώτης", 30);
        styleChoiceButton(individualBtn);

        companyBtn = new RoundedButton("Επιχείρηση", 30);
        styleChoiceButton(companyBtn);

        buttonsRow.add(individualBtn);
        buttonsRow.add(companyBtn);

        centerPanel.add(buttonsRow);
        
        // Add spacing at the bottom to keep it centered
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);

   
        // panel.revalidate();
        // panel.repaint();

        hide();
    }

    private void styleChoiceButton(RoundedButton btn) {
        btn.setPreferredSize(new Dimension(280, 150));
        btn.setBackground(Color.WHITE);
        btn.setForeground(red);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JPanel getMainPanel() { return panel; }


    public void show() { 
        this.panel.setVisible(true); 
        
    }

    public void hide() { this.panel.setVisible(false); }

    // Getters for your Controller
    public RoundedButton getIndividualBtn() { return individualBtn; }
    public RoundedButton getCompanyBtn() { return companyBtn; }
}