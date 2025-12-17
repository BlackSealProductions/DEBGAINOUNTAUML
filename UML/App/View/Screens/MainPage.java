package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainPage implements View_t{

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color bkgColor = Color.decode("#C2E5FF");



    private JPanel mainPanel = new JPanel();



    @Override
    public void init(){

        // main panel // background
        mainPanel.setBackground(bkgColor);
        mainPanel.setBounds(0,0,wWidth,wHeight);
        mainPanel.setBorder((new EmptyBorder(-10,0,0,0)));
        mainPanel.setLayout(null);

        // BoT logo 
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(50,50,200,200);


        // welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(null);
        welcomePanel.setBounds(520,120,600,200);
        
        JLabel welcomeJLabel = new JLabel("Καλως Ήλθατέ");
        welcomeJLabel.setFont(customFont60);

        JLabel bankoftuccJLabel = new JLabel("στην Bank of TUC");
        bankoftuccJLabel.setFont(customFont60);

        welcomePanel.add(welcomeJLabel);
        welcomePanel.add(bankoftuccJLabel);


        // hmmu image
        Image hmmy = new ImageIcon(getClass().getResource("/Images/hmmyIMG.png")).getImage().getScaledInstance(700, 350, Image.SCALE_SMOOTH);
        ImageIcon immyIcon = new ImageIcon(hmmy);
        RoundedImage hmmyPanel = new RoundedImage(immyIcon,10);
        hmmyPanel.setBounds(465,350,700,350);


        // 9/10 dentists
        JPanel dentistPanel = new JPanel();
        dentistPanel.setBackground(null);
        dentistPanel.setBounds(1225,400,300,300);
  
        JLabel odont = new JLabel("9/10 Οδοντίατρους");
        odont.setFont(customFont20);

        JLabel epilegoun = new JLabel("επιλέγουν Bank of TUC!");
        epilegoun.setFont(customFont20);

        Image trust = new ImageIcon(getClass().getResource("/Images/trustpilot.png")).getImage().getScaledInstance(280, 120, Image.SCALE_SMOOTH);
        ImageIcon trustIcon = new ImageIcon(trust);
        RoundedImage trustPanel = new RoundedImage(trustIcon,1);
        
        dentistPanel.add(odont);
        dentistPanel.add(epilegoun);
        dentistPanel.add(trustPanel);

        // koumparas prasino prama aristera

        JPanel greenPanel = new JPanel();
        Color green = Color.decode("#CDF4D3");
        
        greenPanel.setBackground(green);
        greenPanel.setBounds(25,320,420,420);
        greenPanel.setBorder(BorderFactory.createDashedBorder(Color.decode("#66D575"), 3, 3, 1, true));


        JLabel apo = new JLabel("Απολαύστε Προνόμια όπως:");
        apo.setFont(customFont20);

        JLabel kat = new JLabel("• Κατάθεση σε Λογαριασμό");
        kat.setFont(customFont20);

        JLabel met = new JLabel("• Μεταφορές μεταξύ λογαριασμών");
        met.setFont(customFont20);

        JLabel plh = new JLabel("• Πληρωμή Οφειλών");
        plh.setFont(customFont20);

        JLabel pag = new JLabel("• Πάγιες Πληρωμές Λογαριασμών");
        pag.setFont(customFont20);

        JLabel plhr = new JLabel("• Πληροφιρές Κινήσεων Λογαρισμού");
        plhr.setFont(customFont20);
        
        JLabel polla = new JLabel("και πολλα άλλα!");
        polla.setFont(customFont20);

        
        Image pig = new ImageIcon(getClass().getResource("/Images/pig.png")).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon pigIcon = new ImageIcon(pig);
        RoundedImage pigPanel = new RoundedImage(pigIcon,1);


        // register and login buttons 

        

        


        greenPanel.add(apo);
        greenPanel.add(pigPanel);
        greenPanel.add(kat);
        greenPanel.add(met);
        greenPanel.add(plh);
        greenPanel.add(pag);
        greenPanel.add(plhr);
        greenPanel.add(polla);






        mainPanel.add(logoPanel);
        mainPanel.add(welcomePanel);
        mainPanel.add(hmmyPanel);
        mainPanel.add(dentistPanel);
        mainPanel.add(greenPanel);





        //
        


    }






    @Override
    public JPanel getMainPanel(){
        return mainPanel;
    }

    @Override
    public void hide(){
        this.mainPanel.setVisible(false);
    }

    @Override
    public void show(){
        this.mainPanel.setVisible(true);
    }


}