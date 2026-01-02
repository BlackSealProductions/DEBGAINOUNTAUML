package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import App.Model.Session;

public class TransactionDB {
    private static final String DB_FILE = "transactions.json";


    public TransactionDB(){

    }

    // ===========================
    //      PUBLIC METHODS
    // ===========================

    public Map<String, Object> findAccountWithId(String id){

        List<Map<String, Object>> records = getAllRecords();
        Map<String, Object> foundAccount = null;

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            if (acct.get("accountId").equals(id)) {
                foundAccount = acct;
                // System.out.println(user.get("username"));
                break;
            }
        }
        return foundAccount;
    }

    /**
     * Loads all records from the JSON file.
     */
    public List<Map<String, Object>> getAllRecords() {
        return parseJsonNested(DB_FILE);
    }


    /**
     * Saves a NEW user during registration.
     */
    public void saveRecord(Map<String, Object> userWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        // Map<String, Object> wrapper = new HashMap<>();
        // wrapper.put("user", userData);
        allRecords.add(userWrapper);
        saveAllRecords(allRecords);
    }

    public void updateUserRecord(Map<String, Object> updatedWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        
        // Extract the updated username to find the match
        Map<String, Object> updatedAcct = (Map<String, Object>) updatedWrapper.get("account");
        String targetId = (String) updatedAcct.get("accountId");
    
        boolean found = false;
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> currentWrapper = allRecords.get(i);
            Map<String, Object> currentAcct = (Map<String, Object>) currentWrapper.get("account");
    
            if (currentAcct.get("accountId").equals(targetId)) {
                // Replace the old user data with the new updated version
                allRecords.set(i, updatedWrapper);
                found = true;
                break;
            }
        }
    
        if (found) {
            saveAllRecords(allRecords); // Overwrites the file with the updated list
        } else {
            // If for some reason the user wasn't there, treat it as a new record
            saveRecord((Map<String, Object>) updatedWrapper);
        }
    }

    /**
     * Adds a new account to an existing user identified by username.
     */
    // public void addAccountToUser(String username, Map<String, String> newAccount) {
    //     List<Map<String, Object>> allRecords = getAllRecords();
    //     for (Map<String, Object> wrapper : allRecords) {
    //         Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
    //         if (user.get("username").equals(username)) {
    //             List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
    //             if (accounts == null) {
    //                 accounts = new ArrayList<>();
    //                 user.put("accounts", accounts);
    //             }
    //             accounts.add(newAccount);
    //             break;
    //         }
    //     }
    //     saveAllRecords(allRecords);
    // }


    // ===========================
    //   PRIVATE HELPERS (Writer)
    // ===========================

    public void saveAllRecords(List<Map<String, Object>> allRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
    
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> wrapper = allRecords.get(i);
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
    
            sb.append("  {\n");
            sb.append("    \"account\": {\n");
            sb.append(String.format("      \"accountId\": \"%s\",\n", acct.get("accountId")));
        
            // --- Transactions Section ---
            sb.append("      \"transactions\": [\n");
            List<Map<String, String>> transactions = (List<Map<String, String>>) acct.get("transactions");
            if (transactions != null) {
                for (int j = 0; j < transactions.size(); j++) {
                    Map<String, String> tr = transactions.get(j);
                    // Skip ghost accounts as discussed previously
                    if (tr.get("transactionId") == null || tr.get("transactionId").isEmpty()) continue;
    
                    sb.append("        {\n");
                    sb.append(String.format("          \"transactionId\": \"%s\",\n", tr.get("transactionId")));
                    sb.append(String.format("          \"senderId\": \"%s\",\n", tr.get("senderId")));
                    sb.append(String.format("          \"recieverId\": \"%s\",\n", tr.get("recieverId")));
                    sb.append(String.format("          \"amount\": \"%s\",\n", tr.get("amount")));
                    sb.append(String.format("          \"date\": \"%s\",\n", tr.get("date")));
                    sb.append(String.format("          \"time\": \"%s\",\n", tr.get("time")));
                    sb.append(String.format("          \"description\": \"%s\",\n", tr.get("description")));
                    sb.append(String.format("          \"type\": \"%s\"\n", tr.get("type")));
                    sb.append("        }");
                    if (j < transactions.size() - 1) sb.append(",");
                    sb.append("\n");
                }
            }
            sb.append("      ]\n");
            sb.append("    }\n");
            sb.append("  }");
            if (i < allRecords.size() - 1) sb.append(",");
            sb.append("\n");
        }
    
        sb.append("]");
    
        // Write to file
        try (java.io.PrintWriter out = new java.io.PrintWriter("transactions.json")) {
            out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // ===========================
    //   PRIVATE HELPERS (Parser)
    // ===========================

    private static List<Map<String, Object>> parseJsonNested(String filename) {
        List<Map<String, Object>> records = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) return records;

            String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
            if (content.length() < 3) return records; 

            // Remove outer brackets []
            content = content.substring(1, content.length() - 1).trim();

            // Split into "User" blocks by counting curly braces
            List<String> acctBlocks = new ArrayList<>();
            int braceCount = 0;
            StringBuilder sb = new StringBuilder();
            boolean insideQuotes = false;

            for (char c : content.toCharArray()) {
                if (c == '\"') insideQuotes = !insideQuotes;
                if (!insideQuotes) {
                    if (c == '{') braceCount++;
                    if (c == '}') braceCount--;
                }
                sb.append(c);
                if (braceCount == 0 && sb.toString().trim().length() > 0 && !insideQuotes) {
                    String block = sb.toString().trim();
                    if (block.startsWith(",")) block = block.substring(1).trim();
                    acctBlocks.add(block);
                    sb.setLength(0);
                }
            }

            for (String block : acctBlocks) {

                String acctountId = extractValue(block, "accountId");
                if (acctountId == null || acctountId.trim().isEmpty()) {
                    continue; // Skip this ghost block
                }

                // String type = extractValue(block, "type");

                Map<String, Object> userData = new HashMap<>();
                userData.put("accountId", extractValue(block, "accountId"));
                

                // Handle nested accounts array
                List<Map<String, String>> transactions = new ArrayList<>();
                int trStart = block.indexOf("\"transactions\": [");
                if (trStart != -1) {
                    int trEnd = block.lastIndexOf("]");
                    String trsSection = block.substring(trStart + 12, trEnd).trim();
                    
                    // NEW CHECK: Only proceed if the section contains at least one object opening '{'
                    if (trsSection.contains("{")) {
                        // Split accounts by looking for closing braces of account objects
                        String[] trParts = trsSection.split("\\},");
                        for (String part : trParts) {
                            if (part.trim().isEmpty()) continue;
                            
                            // Further safety: ensure this specific part has data
                            String id = extractValue(part, "transactionId");
                            if (id.isEmpty()) continue; 

                            Map<String, String> tr = new HashMap<>();
                            tr.put("transactionId", id);
                            tr.put("senderId", extractValue(part, "senderId"));
                            tr.put("recieverId", extractValue(part, "recieverId"));
                            tr.put("amount", extractValue(part, "amount"));
                            tr.put("date", extractValue(part, "date"));
                            tr.put("time", extractValue(part, "time"));
                            tr.put("description", extractValue(part, "description"));
                            tr.put("type", extractValue(part, "type"));
                            transactions.add(tr);
                        }
                    }
                }
                userData.put("transactions", transactions);
                
                Map<String, Object> wrapper = new HashMap<>();
                wrapper.put("account", userData);
                records.add(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    private static String extractValue(String block, String key) {
        String search = "\"" + key + "\":";
        int index = block.indexOf(search);
        if (index == -1) return "";
        
        // Find the opening quote after the colon
        int start = block.indexOf("\"", index + search.length()) + 1;
        if (start == 0) return ""; // Not found
        
        // Find the closing quote
        int end = block.indexOf("\"", start);
        if (end == -1) return "";
        
        return block.substring(start, end);
    }
}


