package Utils;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtils {

    // ===========================
    //      FORMATTING DATA
    // ===========================

    // Defines how money should look (e.g., "1,250.50")
    private static final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
    
    // Defines how dates should look (e.g., "31-12-2025 14:30")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    /**
     * Input: 1500.5
     * Output: "1,500.50 €"
     */
    public static String formatCurrency(String amountStr) {
        if (amountStr == null || amountStr.isEmpty()) return "0.00 €";
        try {
            double amount = Double.parseDouble(amountStr);
            return moneyFormat.format(amount) + " €";
        } catch (NumberFormatException e) {
            return amountStr + " €";
        }
    }

    /**
     * Input: 1500.5
     * Output: "1,500.50 €" (Overloaded for double)
     */
    public static String formatCurrency(double amount) {
        return moneyFormat.format(amount) + " €";
    }

    /**
     * Returns the current date and time as a clean String.
     * Useful for saving transaction timestamps.
     */
    public static String getCurrentTimestamp() {
        return dateFormat.format(new Date());
    }

    /**
     * Input: "GR1234567890123456789012345"
     * Output: "GR12 **** **** ... 345"
     * Usage: Show this on the Dashboard instead of the full IBAN for privacy.
     */
    public static String maskIBAN(String iban) {
        if (iban == null || iban.length() < 10) return "Unknown IBAN";
        return iban.substring(0, 4) + " **** " + iban.substring(iban.length() - 4);
    }

    // ===========================
    //      UI HELPERS (Swing)
    // ===========================

    /**
     * Clears all text fields in a given panel.
     * Usage: Call this after a successful Deposit or Registration to reset the form.
     */
    public static void clearForm(JPanel panel) {
        for (Component c : panel.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            } else if (c instanceof JPasswordField) {
                ((JPasswordField) c).setText("");
            }
            // If you have panels inside panels, we can check recursively if needed
        }
    }

    /**
     * Consistent error popup style.
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Consistent success popup style.
     */
    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}