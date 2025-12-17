package App.View.helper_classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OnClickEventHelper {

    static Boolean userClicked = false;
    static Boolean passClicked = false;

    public static void setOnClickColor(RoundedButton button, Color pressedColor, Color originalColor) {
    button.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                button.setContentAreaFilled(false);
                button.setBackground(pressedColor);
                button.setOpaque(true);
                // button.repaint();
                // System.out.println("Button Clicked");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(originalColor);
                // button.repaint();
            }
        });
    }
    // public static void clearUser(JTextArea text) {
    // text.addMouseListener(new MouseAdapter() {

    //         @Override
    //         public void mousePressed(MouseEvent e) {
    //             if(userClicked==false){

    //                 text.setText("");
    //                 text.setForeground(Color.decode("#3A3F3E"));
    //                 userClicked=true;
    //             }
    //         }
    //     });
    // }
    // public static void clearPass(JTextArea text) {
    // text.addMouseListener(new MouseAdapter() {

    //         @Override
    //         public void mousePressed(MouseEvent e) {
    //             if(passClicked==false){

    //                 text.setText("");
    //                 text.setForeground(Color.decode("#3A3F3E"));
    //                 passClicked=true;
    //             }
    //         }
    //     });
    // }

}