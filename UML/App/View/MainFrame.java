package App.View;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import App.View.helper_classes.RoundedButton;


public class MainFrame{

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JFrame frame = new JFrame();
    private RoundedButton backButton = new RoundedButton(" <- ");

    Color darkgrey = new Color(22, 20, 19);
    Color blue = new Color(168, 237, 255);
    Color red = Color.decode("#D82F4B");



    public void init(){

        frame.setTitle("Titel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setBounds(140, 40, wWidth, wHeight); // 14,36
        frame.getContentPane().setBackground(darkgrey);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // close program when x button top right :3
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Top Bar (Back Button + Title) ---

        
        backButton.setBounds(10, 820, 100, 40);
        backButton.setBackground(red);
        backButton.setForeground(Color.white);
        backButton.setFont(new Font("Bodoni MT", Font.PLAIN, 26));
        backButton.setFocusPainted(false);
        backButton.setVisible(true);
        frame.getLayeredPane().add(backButton, JLayeredPane.PALETTE_LAYER);
    }

        
    
    public void addPanel(JPanel p){
        this.frame.add(p);
    }

    public RoundedButton getBackBtn(){
        return this.backButton;
    }
    
}
