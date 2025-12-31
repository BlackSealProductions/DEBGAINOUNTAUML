package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import Utils.GlobalConsts;

import javax.swing.*;
import java.awt.*;

public class AccountCreationScreen implements View_t {

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    // Font Loading
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    private JPanel panel = new JPanel();
    
    // Theme Colors
    Color blue = Color.decode("#C2E5FF");   
    Color red = Color.decode("#D82F4B");    
    Color placeholderColor = Color.decode("#C6D3D0");
    Color textColor = Color.BLACK;

    // Components
    private JLabel helloUserLabel;
    private JLabel primaryOwnerLabel; 
    private RoundedButton logoutBtn;
    private RoundedButton finishBtn;
    private JTextField secondaryOwnerField;

    @Override
    public void init() {
        // Using null layout on the main panel to position the content container precisely
        panel.setLayout(null);
        panel.setBackground(blue); 
        panel.setBounds(0, 0, wWidth, wHeight);

        // --- Logout Button (X) ---
        // Positioned absolutely in the top right corner
        logoutBtn = new RoundedButton("X", 15);
        logoutBtn.setBounds(wWidth - 70, 20, 50, 50);
        logoutBtn.setBackground(red);
        logoutBtn.setForeground(Color.white);
        logoutBtn.setFont(customFont20);
        logoutBtn.setFocusPainted(false);
        panel.add(logoutBtn);

        // --- Hello User Label ---
        // Positioned at the top center
        helloUserLabel = new JLabel("Hello, user", SwingConstants.CENTER);
        helloUserLabel.setFont(customFont40);
        helloUserLabel.setForeground(textColor);
        helloUserLabel.setBounds(0, 20, wWidth, 60);
        panel.add(helloUserLabel);

        // --- Center Content Wrapper ---
        // Starting at Y=100 to pull the titles and form much higher up the screen
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);
        centerContainer.setBounds(0, 100, wWidth, 600); 

        // Titles
        JLabel title1 = new JLabel("Δεν βρέθηκε λογαριασμος,");
        title1.setFont(customFont60);
        title1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title2 = new JLabel("φτιάξτε έναν:");
        title2.setFont(customFont60);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Spacing between fields
        gbc.gridx = 0;

        // 1. Primary Owner Display
        primaryOwnerLabel = new JLabel("Primary owner: ");
        primaryOwnerLabel.setFont(customFont20);
        primaryOwnerLabel.setForeground(textColor);
        primaryOwnerLabel.setPreferredSize(new Dimension(500, 50));
        gbc.gridy = 0;
        formPanel.add(primaryOwnerLabel, gbc);

        // 2. Secondary Owner Field
        secondaryOwnerField = new JTextField("Secondary owner");
        secondaryOwnerField.setPreferredSize(new Dimension(500, 55)); 
        secondaryOwnerField.setFont(customFont20); 
        secondaryOwnerField.setForeground(placeholderColor);
        secondaryOwnerField.setBackground(Color.WHITE);
        OnFocusEventHelper.setOnFocusText(secondaryOwnerField, "Secondary owner", textColor, placeholderColor);
        gbc.gridy = 1;
        formPanel.add(secondaryOwnerField, gbc);

        // 3. Finish Button
        finishBtn = new RoundedButton("Ολοκλήρωση", 15);
        finishBtn.setBackground(red);
        finishBtn.setForeground(Color.WHITE);
        finishBtn.setFont(customFont20);
        finishBtn.setPreferredSize(new Dimension(300, 60));
        finishBtn.setFocusPainted(false);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 0, 0, 0); // Space above the finish button
        formPanel.add(finishBtn, gbc);

        // Assemble components into the container
        centerContainer.add(title1);
        centerContainer.add(title2);
        centerContainer.add(Box.createVerticalStrut(30)); // Space between titles and form
        centerContainer.add(formPanel);
        // Vertical glue at the bottom pushes all content in this BoxLayout to the top of the container
        centerContainer.add(Box.createVerticalGlue());

        panel.add(centerContainer);

        hide();
    }

    public void setHelloMessage(String name) {
        helloUserLabel.setText("Hello, " + name);
    }

    public void setPrimaryOwnerLabel(String username) {
        primaryOwnerLabel.setText("Primary owner: " + username);
    }

    public RoundedButton getFinishBtn(){ return this.finishBtn; }
    public RoundedButton getLogoutBtn(){ return this.logoutBtn; }
    
    public String getPrimaryOwner() { 
        return primaryOwnerLabel.getText().replace("Primary owner: ", ""); 
    }
    
    public String getSecondaryOwner() { return secondaryOwnerField.getText(); }

    @Override public JPanel getMainPanel() { return this.panel; }

    @Override
    public void show() { 
        panel.setVisible(true); 
        // Focus the container to avoid immediate focus on the text field
        panel.requestFocusInWindow();
    }

    @Override public void hide() { panel.setVisible(false); }
}