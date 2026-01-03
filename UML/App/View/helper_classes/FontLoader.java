package App.View.helper_classes;

import java.awt.Font;
import java.io.InputStream;

public class FontLoader {

    public static Font loadCustomFont(String fontPath, float size) {
        try {
            // Load the font file as a stream from the classpath
            InputStream is = FontLoader.class.getResourceAsStream(fontPath);
            
            if (is == null) {
                System.err.println("Font resource not found at: " + fontPath);
                // Return a default font if the custom one is missing
                return new Font("SansSerif", Font.PLAIN, (int) size); 
            }
            
            // Create the font object from the stream
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            
            // Derive a new font object at the desired size
            return baseFont.deriveFont(Font.PLAIN, size);

        } catch (Exception e) {
            System.err.println("Error loading custom font: " + e.getMessage());
            e.printStackTrace();
            // Fallback: return a default font on error
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
}