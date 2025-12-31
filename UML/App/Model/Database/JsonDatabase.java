package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Import your Entity classes so we can use them
import App.Model.Entities.UserEntities.User;
import App.Model.Entities.UserEntities.Account;

public class JsonDatabase {
    private static final String DB_FILE = "accounts.json";

    // ===========================
    //      PUBLIC METHODS
    // ===========================

    /**
     * Loads all records from the JSON file.
     */
    public static List<Map<String, Object>> getAllRecords() {
        return parseJsonNested(DB_FILE);
    }

    /**
     * Saves a NEW user during registration.
     */
    public static void saveRecord(Map<String, Object> userData) {
        List<Map<String, Object>> allRecords = getAllRecords();
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user", userData);
        allRecords.add(wrapper);
        saveAllRecords(allRecords);
    }

    /**
     * Adds a new account to an existing user identified by username.
     */
    public static void addAccountToUser(String username, Map<String, String> newAccount) {
        List<Map<String, Object>> allRecords = getAllRecords();
        for (Map<String, Object> wrapper : allRecords) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            if (user.get("username").equals(username)) {
                List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
                if (accounts == null) {
                    accounts = new ArrayList<>();
                    user.put("accounts", accounts);
                }
                accounts.add(newAccount);
                break;
            }
        }
        saveAllRecords(allRecords);
    }

    // --- NEW METHOD: This fixes "updateUser undefined" ---
    /**
     * Updates an existing user (and their accounts) in the database.
     * Use this when you have modified a User object (e.g. changed balance).
     */
    @SuppressWarnings("unchecked")
    public static void updateUser(User updatedUser) {
        List<Map<String, Object>> allRecords = getAllRecords();
        boolean found = false;

        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> wrapper = allRecords.get(i);
            Map<String, Object> userMap = (Map<String, Object>) wrapper.get("user");
            
            // Find the user by username
            if (userMap.get("username").equals(updatedUser.getUsername())) {
                // Replace the old Map with a new Map created from our Object
                allRecords.set(i, convertUserToMap(updatedUser));
                found = true;
                break;
            }
        }

        if (found) {
            saveAllRecords(allRecords);
        }
    }

    // ===========================
    //   PRIVATE HELPERS (Writer)
    // ===========================

    // --- NEW HELPER: Converts Object -> Map so the Writer can save it ---
    private static Map<String, Object> convertUserToMap(User u) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", u.getUsername());
        map.put("password", u.getPassword());
        
        // Handle name fields logic
        if ("Company".equalsIgnoreCase(u.getType())) {
            map.put("companyName", u.getName());
            // companies might leave surname empty or use it differently
            map.put("name", u.getName()); 
            map.put("surname", ""); 
        } else {
            map.put("name", u.getName());
            map.put("surname", u.getSurname());
        }

        map.put("phone", u.getPhone());
        map.put("email", u.getEmail());
        map.put("type", u.getType());
        map.put("taxId", u.getTaxId());

        List<Map<String, String>> accList = new ArrayList<>();
        if (u.getAccounts() != null) {
            for (Account a : u.getAccounts()) {
                Map<String, String> am = new HashMap<>();
                am.put("accountId", a.getAccountId());
                am.put("iban", a.getIban());
                am.put("ownerName", a.getOwnerName());
                am.put("secondaryOwner", a.getSecondaryOwner());
                am.put("balance", a.getBalance());
                am.put("interestRate", a.getInterestRate());
                accList.add(am);
            }
        }
        map.put("accounts", accList);
        
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user", map);
        return wrapper; // returning just the inner user map wrapped is tricky, 
                        // actually saveAllRecords expects List<Wrapper>, but here we return Wrapper
                        // Wait, saveAllRecords expects the List.
                        // updateUser sets the element to this return value.
                        // So this return value must be the Wrapper map containing "user".
    }


    public static void saveAllRecords(List<Map<String, Object>> allRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
    
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> wrapper = allRecords.get(i);
            // Safety check: if wrapper has "user" key, get it. If not (rare), try using wrapper itself
            Map<String, Object> user = wrapper.containsKey("user") ? (Map<String, Object>) wrapper.get("user") : wrapper;
            
            // If we still don't have a user map, skip
            if (user == null) continue;

            String type = (String) user.get("type");
    
            sb.append("  {\n");
            sb.append("    \"user\": {\n");
            sb.append(String.format("      \"username\": \"%s\",\n", user.get("username")));
            sb.append(String.format("      \"password\": \"%s\",\n", user.get("password")));
    
            if ("Company".equalsIgnoreCase(type)) {
                String cName = user.containsKey("companyName") ? (String)user.get("companyName") : (String)user.get("name");
                sb.append(String.format("      \"companyName\": \"%s\",\n", cName));
            } else {
                sb.append(String.format("      \"name\": \"%s\",\n", user.get("name")));
                sb.append(String.format("      \"surname\": \"%s\",\n", user.get("surname")));
            }
    
            sb.append(String.format("      \"phone\": \"%s\",\n", user.get("phone")));
            sb.append(String.format("      \"email\": \"%s\",\n", user.get("email")));
            sb.append(String.format("      \"type\": \"%s\",\n", type));
            sb.append(String.format("      \"taxId\": \"%s\",\n", user.get("taxId")));
    
            sb.append("      \"accounts\": [\n");
            List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
            if (accounts != null) {
                for (int j = 0; j < accounts.size(); j++) {
                    Map<String, String> acc = accounts.get(j);
                    if (acc.get("accountId") == null || acc.get("accountId").isEmpty()) continue;
    
                    sb.append("        {\n");
                    sb.append(String.format("          \"accountId\": \"%s\",\n", acc.get("accountId")));
                    sb.append(String.format("          \"iban\": \"%s\",\n", acc.get("iban")));
                    sb.append(String.format("          \"ownerName\": \"%s\",\n", acc.get("ownerName")));
                    sb.append(String.format("          \"secondaryOwner\": \"%s\",\n", acc.get("secondaryOwner")));
                    sb.append(String.format("          \"balance\": \"%s\",\n", acc.get("balance")));
                    sb.append(String.format("          \"interestRate\": \"%s\"\n", acc.get("interestRate")));
                    sb.append("        }");
                    if (j < accounts.size() - 1) sb.append(",");
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
    
        try (PrintWriter out = new PrintWriter(DB_FILE)) {
            out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================
    //   PRIVATE HELPERS (Parser)
    // ===========================
    // (This is your existing parser, untouched)

    private static List<Map<String, Object>> parseJsonNested(String filename) {
        List<Map<String, Object>> records = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) return records;

            String content = new String(Files.readAllBytes(Paths.get(filename))).trim();
            if (content.length() < 3) return records; 

            content = content.substring(1, content.length() - 1).trim();

            List<String> userBlocks = new ArrayList<>();
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
                    userBlocks.add(block);
                    sb.setLength(0);
                }
            }

            for (String block : userBlocks) {
                String username = extractValue(block, "username");
                if (username == null || username.trim().isEmpty()) continue; 
                
                Map<String, Object> userData = new HashMap<>();
                userData.put("username", extractValue(block, "username"));
                userData.put("password", extractValue(block, "password"));
                userData.put("name", extractValue(block, "name"));
                userData.put("surname", extractValue(block, "surname"));
                userData.put("taxId", extractValue(block, "taxId"));
                userData.put("type", extractValue(block, "type"));
                userData.put("phone", extractValue(block, "phone"));
                userData.put("email", extractValue(block, "email"));
                
                // Company handling
                if (block.contains("companyName")) {
                     userData.put("companyName", extractValue(block, "companyName"));
                }

                List<Map<String, String>> accounts = new ArrayList<>();
                int accStart = block.indexOf("\"accounts\": [");
                if (accStart != -1) {
                    int accEnd = block.lastIndexOf("]");
                    String accsSection = block.substring(accStart + 12, accEnd).trim();
                    if (accsSection.contains("{")) {
                        String[] accParts = accsSection.split("\\},");
                        for (String part : accParts) {
                            if (part.trim().isEmpty()) continue;
                            String id = extractValue(part, "accountId");
                            if (id.isEmpty()) continue; 
                            Map<String, String> acc = new HashMap<>();
                            acc.put("accountId", id);
                            acc.put("iban", extractValue(part, "iban"));
                            acc.put("ownerName", extractValue(part, "ownerName"));
                            acc.put("secondaryOwner", extractValue(part, "secondaryOwner"));
                            acc.put("balance", extractValue(part, "balance"));
                            acc.put("interestRate", extractValue(part, "interestRate"));
                            accounts.add(acc);
                        }
                    }
                }
                userData.put("accounts", accounts);
                Map<String, Object> wrapper = new HashMap<>();
                wrapper.put("user", userData);
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
        int start = block.indexOf("\"", index + search.length()) + 1;
        if (start == 0) return ""; 
        int end = block.indexOf("\"", start);
        if (end == -1) return "";
        return block.substring(start, end);
    }
}