package App.View.helper_classes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;


    /**
     * Custom JButton that overrides painting to draw a rounded shape.
     */
    public class RoundedButton extends JButton {
        // Defines the radius of the corners in pixels
        private int cornerRadius = 15;

        public RoundedButton(String text,int cornerRadius) {
            super(text);
            this.cornerRadius = cornerRadius;
            // Ensures the button only paints what's inside its bounds
            setContentAreaFilled(false);
            // Makes the background transparent so the custom shape shows
            setOpaque(false);
            // Remove the default system border
            setBorderPainted(false);
        }

        // Overrides the method to accurately determine the button's click area
        @Override
        public boolean contains(int x, int y) {
            Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            return shape.contains(x, y);
        }

        // Overrides the painting method to draw the rounded shape
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            // Use antialiasing for smooth edges
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color buttonColor = getBackground();

            // Optional: Provide visual feedback on press/hover
            if (getModel().isArmed() || getModel().isRollover()) {
                buttonColor = buttonColor.darker();
            }

            // Fill the rounded rectangle shape
            g2.setColor(buttonColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            // Call the superclass method to draw the text and icon over the shape
            super.paintComponent(g2);
            
            g2.dispose();
        }
    }