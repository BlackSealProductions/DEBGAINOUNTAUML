package App.View.helper_classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountCellRenderer extends JPanel implements ListCellRenderer<String> {
    private JLabel label = new JLabel("", SwingConstants.CENTER);
    private Color selectionColor = Color.decode("#D82F4B"); // Your theme red
    private Color panelColor = Color.WHITE;

    public AccountCellRenderer() {
        setLayout(new BorderLayout());
        setOpaque(false); 
        // Insets: top, left, bottom, right. Controls spacing between cards.
        setBorder(new EmptyBorder(8, 20, 8, 20)); 
        
        label.setFont(new Font("Verdana", Font.BOLD, 22));
        add(label, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        label.setText("Account ID: " + value);
        
        if (isSelected) {
            label.setForeground(Color.WHITE);
            setBackground(selectionColor);
        } else {
            label.setForeground(Color.BLACK);
            setBackground(panelColor);
        }
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw shadow or border if desired, here we just draw the main rounded body
        g2.setColor(getBackground());
        // Adjust the x, y, width, height to leave room for the "EmptyBorder" spacing
        g2.fillRoundRect(10, 5, getWidth() - 20, getHeight() - 10, 25, 25);
        
        g2.dispose();
    }
}