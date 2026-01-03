package App.View.helper_classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.JTextComponent;

public class OnFocusEventHelper {

    static String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont50 = FontLoader.loadCustomFont(fontPath, 50f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont30 = FontLoader.loadCustomFont(fontPath, 30f);
    static Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    public static void setOnFocusText(JTextComponent textComponent, String placeholderText, Color focusGainedColor, Color focusLostColor) {
        textComponent.setText(placeholderText);
        textComponent.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textComponent.getText().equals(placeholderText)) {
                    textComponent.setText("");
                    textComponent.setFont(customFont20);
                    textComponent.setForeground(focusGainedColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textComponent.getText().isEmpty()) {
                    textComponent.setText(placeholderText);
                    textComponent.setFont(customFont20);
                    textComponent.setForeground(focusLostColor);
                }
            }
        });
    }
}