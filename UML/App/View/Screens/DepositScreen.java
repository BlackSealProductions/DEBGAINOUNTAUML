package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;




public class DepositScreen implements View_t{
    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont50 = FontLoader.loadCustomFont(fontPath, 50f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont30 = FontLoader.loadCustomFont(fontPath, 30f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    Color bkgColor = Color.decode("#C2E5FF");



    private JPanel mainPanel = new JPanel();



    @Override
    public void init(){
        mainPanel.setBackground(bkgColor);
        mainPanel.setBounds(0,0,wWidth,wHeight);
        mainPanel.setBorder((new EmptyBorder(-10,0,0,0)));
        mainPanel.setLayout(null);

        
        // BoT logo 
        Image logo = new ImageIcon(getClass().getResource("/Images/bankOfTucLogo_white.png")).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon logoIcon = new ImageIcon(logo);
        RoundedImage logoPanel = new RoundedImage(logoIcon,20);
        logoPanel.setBounds(50,50,200,200);


        // τιτλος
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(null);
        welcomePanel.setBounds(400,120,800,200);
        JLabel welcomeLabel = new JLabel("Κατάθεση σε Λογαριασμό");
        welcomeLabel.setFont(customFont60);
        welcomePanel.add(welcomeLabel);;

        // deposit Panel

        Image white = new ImageIcon(getClass().getResource("/Images/white_ahh_image.png")).getImage().getScaledInstance(2000, 1000, Image.SCALE_SMOOTH);
        ImageIcon whiteIcon = new ImageIcon(white);
        RoundedImage whiteLabel = new RoundedImage(whiteIcon,100);
        whiteLabel.setBounds(150,400,1300,200);

        //depositPanel.add(whiteLabel);
        JLabel fromAccText = new JLabel("ΑΠΟ: Λογαριασμός:");
        fromAccText.setFont(customFont20);
        fromAccText.setBounds(200,430,400,50);
        JTextField fromAccount = new JTextField();
        fromAccount.setFont(customFont30);
        fromAccount.setBounds(200,480,400,50);
        
        JLabel toAccText = new JLabel("ΠΡΟΣ: Λογαριασμός:");
        toAccText.setFont(customFont20);
        toAccText.setBounds(700,430,400,50);
        JTextField toAccount = new JTextField();
        toAccount.setFont(customFont30);
        toAccount.setBounds(700,480,400,50);
        

        JTextField totalMoney = new JTextField();
        totalMoney.setFont(customFont20);
        totalMoney.setBounds(1300,480,100,50);
        
        JLabel moneyText = new JLabel("Ποσο:");
        moneyText.setFont(customFont20);
        moneyText.setBounds(1300,430,100,50);

        // ypoloipo 
        Color darkBlue = Color.decode("#082336");
        JLabel total = new JLabel("Υπόλοιπο:");
        total.setFont(customFont30);
        total.setForeground(darkBlue);
        total.setBounds(1250,20,200,100);
        JLabel totalNum = new JLabel("0€");
        totalNum.setBounds(1430,20,200,100);
        totalNum.setForeground(darkBlue);
        totalNum.setFont(customFont30);



        mainPanel.add(fromAccount);
        mainPanel.add(toAccount);
        mainPanel.add(moneyText);
        mainPanel.add(totalMoney);
        mainPanel.add(fromAccText);
        mainPanel.add(toAccText);
        mainPanel.add(total);
        mainPanel.add(totalNum);




        // deposit button 

        Color red = Color.decode("#A91A32");
        RoundedButton depositBut = new RoundedButton("Deposit",30);
        depositBut.setBackground(red);
        depositBut.setForeground(Color.white);
        depositBut.setFont(customFont40);
        depositBut.setAlignmentX(Component.CENTER_ALIGNMENT);
        depositBut.setMaximumSize(new Dimension(400, 100));
        depositBut.setMinimumSize(new Dimension(300, 150));
        depositBut.setFocusPainted(false);
        depositBut.setBounds(620,670,300,90);

        // add to main Panel
        mainPanel.add(whiteLabel);
        mainPanel.add(logoPanel);
        mainPanel.add(welcomePanel);
        mainPanel.add(depositBut);




    
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
