package App.View.helper_classes;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;


    /**
     * Custom JLabel that displays an image with rounded corners.
     */
    public class RoundedImage extends JLabel {
     // Radius for the corners

        private int cornerRadius;
        public RoundedImage(ImageIcon icon, int cornerRadius) {
            super(icon);
            this.cornerRadius = cornerRadius;
            // Ensure the background is handled correctly
            setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            // 1. Enable Anti-Aliasing for smooth corners
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 2. Define the clipping shape (a rounded rectangle)
            // It covers the entire label area
            Shape clip = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            
            // 3. Set the clip. Subsequent drawing operations will only be done within this shape.
            g2.setClip(clip);

            // 4. Draw the actual image (calling super.paintComponent)
            // This is where the image icon is rendered, but it's clipped to the rounded shape.
            super.paintComponent(g2);
            
            // 5. Optionally, draw a border around the rounded shape
            g2.setColor(Color.WHITE); // Color of the border background/area outside the image
            g2.draw(clip); // Draws a thin rounded border line if needed

            g2.dispose();
        }
    }