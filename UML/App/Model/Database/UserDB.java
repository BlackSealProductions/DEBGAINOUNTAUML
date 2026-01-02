package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import App.Model.Entities.OperationEntities.Transaction;

public class UserDB {
    private static final String DB_FILE = "accounts.json";

    public UserDB(){

    }

    // ===========================
    //      PUBLIC METHODS
    // ===========================

    public Map<String, Object> findUserWithPassword(String inputUser, String inputPass){
        List<Map<String, Object>> records = getAllRecords();
        Map<String, Object> foundUser = null;

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            if (user.get("username").equals(inputUser) && user.get("password").equals(inputPass)) {
                foundUser = user;
                System.out.println(user.get("username"));
                break;
            }
        }
        return foundUser;
    }

    public Map<String, Object> findAccountWithId(String acctId){
        List<Map<String, Object>> records = getAllRecords();
        Map<String, Object> foundUser = null;

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            List<Map<String,Object>> accs = (List<Map<String,Object>>) user.get("accounts");
            for (Map<String, Object> acc : accs){

                if (acc.get("accountId").equals(acctId)) {
                    foundUser = user;
                    break;
                }
            }
        }
        return foundUser;  
    }

    public Set<String> getExistingAcctIds(){
        List<Map<String, Object>> records = getAllRecords();
        Set<String> existingIds = new HashSet<>();

        // Collect all IDs currently in the system
        for (Map<String, Object> wrapper : records) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
            if (accounts != null) {
                for (Map<String, String> acc : accounts) {
                    existingIds.add(acc.get("accountId"));
                }
            }
        }
        return existingIds;
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
        allRecords.add(userWrapper);
        saveAllRecords(allRecords);
    }

    public void updateUserRecord(Map<String, Object> updatedWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        
        // Extract the updated username to find the match
        Map<String, Object> updatedUser = (Map<String, Object>) updatedWrapper.get("user");
        String targetUsername = (String) updatedUser.get("username");
    
        boolean found = false;
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> currentWrapper = allRecords.get(i);
            Map<String, Object> currentUser = (Map<String, Object>) currentWrapper.get("user");
    
            if (currentUser.get("username").equals(targetUsername)) {
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
    public void addAccountToUser(String username, Map<String, String> newAccount) {
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


    // ===========================
    //   PRIVATE HELPERS (Writer)
    // ===========================

    public void saveAllRecords(List<Map<String, Object>> allRecords) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
    
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> wrapper = allRecords.get(i);
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            String type = (String) user.get("type");
    
            sb.append("  {\n");
            sb.append("    \"user\": {\n");
            sb.append(String.format("      \"username\": \"%s\",\n", user.get("username")));
            sb.append(String.format("      \"password\": \"%s\",\n", user.get("password")));
    
            // --- Conditional Logic based on User Type ---
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
    
            // --- Accounts Section ---
            sb.append("      \"accounts\": [\n");
            List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
            if (accounts != null) {
                for (int j = 0; j < accounts.size(); j++) {
                    Map<String, String> acc = accounts.get(j);
                    // Skip ghost accounts
                    if (acc.get("accountId") == null || acc.get("accountId").isEmpty()) continue;
    
                    sb.append("        {\n");
                    sb.append(String.format("          \"accountId\": \"%s\",\n", acc.get("accountId")));
                    sb.append(String.format("          \"iban\": \"%s\",\n", acc.get("iban")));
                    sb.append(String.format("          \"ownerName\": \"%s\",\n", acc.get("ownerName")));
                    sb.append(String.format("          \"secondaryOwner\": \"%s\",\n", acc.get("secondaryOwner")));
                    sb.append(String.format("          \"balance\": \"%s\",\n", acc.get("balance")));
                    // Added comma to interestRate line below so we can add rfCode after it
                    if("Company".equalsIgnoreCase(type)){
                        sb.append(String.format("          \"rfCode\": \"%s\",\n", acc.get("rfCode")));
                    }
                    sb.append(String.format("          \"interestRate\": \"%s\"\n", acc.get("interestRate"))); 
                    // Added rfCode line exactly as requested
                    
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
    
        // Write to file
        try (java.io.PrintWriter out = new java.io.PrintWriter("accounts.json")) {
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
                if (username == null || username.trim().isEmpty()) {
                    continue; 
                }

                String type = extractValue(block, "type");

                Map<String, Object> userData = new HashMap<>();
                userData.put("username", extractValue(block, "username"));
                userData.put("password", extractValue(block, "password"));
                if("Company".equalsIgnoreCase(type)){
                    userData.put("companyName", extractValue(block, "companyName"));
                }
                else{
                    userData.put("name", extractValue(block, "name"));
                    userData.put("surname", extractValue(block, "surname"));
                }
                userData.put("taxId", extractValue(block, "taxId"));
                userData.put("type", extractValue(block, "type"));
                userData.put("phone", extractValue(block, "phone"));
                userData.put("email", extractValue(block, "email"));

                // Handle nested accounts array
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
                            if("Company".equalsIgnoreCase(type)){
                                acc.put("rfCode", extractValue(part, "rfCode")); // Already here, this is good
                            }
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