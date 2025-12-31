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
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JPanel panel = new JPanel();
    
    // --- Colors ---
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    

    private RoundedButton individualBtn;
    private RoundedButton companyBtn;
    private JLabel titleLabel;

    @Override
    public void init() {
        // --- Setup Main Panel ---
        panel.setLayout(new BorderLayout());
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);
        // Matching the padding of your RegisterCompanyScreen
        panel.setBorder(new EmptyBorder(40, 60, 40, 60));

        // --- 1. Top Bar (Logo and Title) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Logo (Absolute positioned or added to topPanel)
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png"))
                        .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon, 20);
        
        // Container for Logo to prevent it from stretching in BorderLayout
        JPanel logoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoWrapper.setOpaque(false);
        logoWrapper.add(logoPanel);

        // Title Label matched to RegisterCompanyScreen style
        titleLabel = new JLabel("Εγγραφή Χρήστη", SwingConstants.CENTER);
        titleLabel.setFont(customFont60 != null ? customFont60 : new Font("Verdana", Font.BOLD, 60)); 
        // titleLabel.setForeground(Color.BLACK);
        
        // topPanel.add(logoWrapper, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(logoPanel);
        logoPanel.setBounds(50,50,200,200);


        // --- 2. Center Selection Section ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Selection Instruction
        JLabel subTitle = new JLabel("Επιλέξτε τύπο λογαριασμού:");
        subTitle.setFont(customFont20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        centerWrapper.add(subTitle, gbc);

        // Buttons Row
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonsRow.setOpaque(false);

        individualBtn = new RoundedButton("Ιδιώτης", 30);
        styleChoiceButton(individualBtn);

        companyBtn = new RoundedButton("Επιχείρηση", 30);
        styleChoiceButton(companyBtn);

        buttonsRow.add(individualBtn);
        buttonsRow.add(companyBtn);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerWrapper.add(buttonsRow, gbc);

        panel.add(centerWrapper, BorderLayout.CENTER);

        hide();
    }

    private void styleChoiceButton(RoundedButton btn) {
        btn.setPreferredSize(new Dimension(320, 180));
        btn.setBackground(Color.WHITE);
        btn.setForeground(red);
        btn.setFont(customFont20);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public JPanel getMainPanel() { return panel; }

    @Override
    public void show() { 
        this.panel.setVisible(true); 
    }

    @Override
    public void hide() { this.panel.setVisible(false); }

    public RoundedButton getIndividualBtn() { return individualBtn; }
    public RoundedButton getCompanyBtn() { return companyBtn; }
}