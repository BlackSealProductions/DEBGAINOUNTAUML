package Utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    // ===========================
    //      MONEY & TRANSACTIONS
    // ===========================

    public static boolean isValidTransactionAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return false;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean hasSufficientBalance(String currentBalanceStr, String amountToWithdrawStr) {
        try {
            double balance = Double.parseDouble(currentBalanceStr);
            double amount = Double.parseDouble(amountToWithdrawStr);
            return balance >= amount;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ===========================
    //      REGISTRATION: USER
    // ===========================

    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 5;
    }

    /**
     * UPDATED: Bank-Grade Password Validation
     * Rules: 
     * 1. At least 8 characters long
     * 2. Contains Uppercase letter
     * 3. Contains Lowercase letter
     * 4. Contains Digit
     * 5. Contains Symbol (Special character)
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                // If it's not a letter or a digit, we consider it a symbol/special char
                hasSpecial = true;
            }
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isValidTaxId(String taxId) {
        return taxId != null && taxId.matches("\\d{9}");
    }

    // ===========================
    //      BANKING IDENTIFIERS
    // ===========================

    public static boolean isValidGreekIBAN(String iban) {
        if (iban == null) return false;
        String cleanIban = iban.replace(" ", "");
        return cleanIban.matches("^GR\\d{25}$");
    }
}