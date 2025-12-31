package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SecurityUtils {

    // ===========================
    //      PASSWORD SECURITY
    // ===========================

    /**
     * Hashes a password using SHA-256. 
     * Input: "123"
     * Output: "a665a45920422f9d417e4867efdc4fb..." (Secure string)
     */
    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(rawPassword.getBytes());
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return rawPassword; // Fallback (should never happen)
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // ===========================
    //      ID GENERATION
    // ===========================

    /**
     * Generates a random Transaction ID
     * Output: "TRX-839201"
     */
    public static String generateTransactionId() {
        Random rand = new Random();
        int num = 100000 + rand.nextInt(900000);
        return "TRX-" + num;
    }

    /**
     * Generates a fake Greek IBAN
     * Output: "GR8239..." (27 chars total)
     */
    public static String generateNewIBAN() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("GR");
        for (int i = 0; i < 25; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
    
    /**
     * Generates a unique Account ID
     * Output: "ID_559283"
     */
    public static String generateAccountId() {
        Random rand = new Random();
        int num = 100000 + rand.nextInt(900000);
        return "ID_" + num;
    }
}