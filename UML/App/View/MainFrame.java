package App.View;

import javax.swing.*;

import Utils.GlobalConsts;

import java.awt.*;


public class MainFrame{

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    private JFrame frame = new JFrame();
    Color darkgrey = new Color(22, 20, 19);
    Color blue = new Color(168, 237, 255);


    public void init(){

        frame.setTitle("NIGGA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setBounds(140, 40, wWidth, wHeight); // 14,36
        frame.getContentPane().setBackground(darkgrey);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
        
    
    public void addPanel(JPanel p){
        this.frame.add(p);

    }
    
}
