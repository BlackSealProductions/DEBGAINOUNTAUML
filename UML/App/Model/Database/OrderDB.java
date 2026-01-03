package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import App.Model.Session;


public class OrderDB {
    
    
        private static final String DB_FILE = "orders.json";
    
    
        public OrderDB(){
    
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
                System.out.println("\nadded new\n");
                // If for some reason the user wasn't there, treat it as a new record
                saveRecord((Map<String, Object>) updatedWrapper);
            }
        }
    
    
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
                sb.append("      \"orders\": [\n");
                List<Map<String, String>> orders = (List<Map<String, String>>) acct.get("orders");
                if (orders != null) {
                    for (int j = 0; j < orders.size(); j++) {
                        Map<String, String> so = orders.get(j);
                        // Skip ghost accounts as discussed previously
                        if (so.get("orderId") == null || so.get("orderId").isEmpty()) continue;
        
                        sb.append("        {\n");
                        sb.append(String.format("          \"name\": \"%s\",\n", so.get("name")));
                        sb.append(String.format("          \"targetIban\": \"%s\",\n", so.get("targetIban")));
                        sb.append(String.format("          \"orderId\": \"%s\",\n", so.get("orderId")));
                        sb.append(String.format("          \"amount\": \"%s\",\n", so.get("amount")));
                        sb.append(String.format("          \"day\": \"%s\",\n", so.get("day")));
                        sb.append(String.format("          \"dueDate\": \"%s\",\n", so.get("dueDate")));
                        sb.append(String.format("          \"frequency\": \"%s\"\n", so.get("frequency")));
                        sb.append("        }");
                        if (j < orders.size() - 1) sb.append(",");
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
            try (java.io.PrintWriter out = new java.io.PrintWriter("orders.json")) {
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
                    List<Map<String, String>> orders = new ArrayList<>();
                    int soStart = block.indexOf("\"orders\": [");
                    if (soStart != -1) {
                        int trEnd = block.lastIndexOf("]");
                        String soSection = block.substring(soStart + 12, trEnd).trim();
                        
                        // NEW CHECK: Only proceed if the section contains at least one object opening '{'
                        if (soSection.contains("{")) {
                            // Split accounts by looking for closing braces of account objects
                            String[] soParts = soSection.split("\\},");
                            for (String part : soParts) {
                                if (part.trim().isEmpty()) continue;
                                
                                // Further safety: ensure this specific part has data
                                String id = extractValue(part, "orderId");
                                if (id.isEmpty()) continue; 
    
                                Map<String, String> so = new HashMap<>();
                                so.put("name", extractValue(part, "name"));
                                so.put("orderId", id);
                                so.put("targetIban", extractValue(part, "targetIban"));
                                so.put("transactionId", extractValue(part, "transactionId"));
                                so.put("amount", extractValue(part, "amount"));
                                so.put("day", extractValue(part, "day"));
                                so.put("dueDate", extractValue(part, "dueDate"));
                                so.put("frequency", extractValue(part, "frequency"));
                                orders.add(so);
                            }
                        }
                    }
                    userData.put("orders", orders);
                    
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



