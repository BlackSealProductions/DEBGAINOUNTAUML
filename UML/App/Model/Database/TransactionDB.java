package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import App.Model.Entities.OperationEntities.Transaction;

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
                break;
            }
        }
        return foundAccount;
    }

    public Map<String, Object> addTransactionToWrapper(String id,Map<String,String> transaction){

        List<Map<String, Object>> records = getAllRecords();
        Map<String, Object> foundAccount = null;

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            if (acct.get("accountId").equals(id)) {
                List<Map<String,String>> transactions = (List<Map<String,String>>)acct.get("transactions");
                transactions.add(transaction);
                updateUserRecord(wrapper);
            }
        }
        return foundAccount;
    }

    public Map<String, Object> findTransactionWithId(String id){

        List<Map<String, Object>> records = getAllRecords();
        Map<String, Object> foundTr = null;

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            List<Map<String, Object>> trs = (List<Map<String, Object>>) acct.get("transactions");
            for(Map<String, Object> tr : trs){
                if( tr.get("transactionId").equals(id)){
                    return tr;
                }
            }
        }
        return foundTr;
    }

    public List<Map<String, Object>> findTransactionsWithRecId(String id){

        List<Map<String, Object>> records = getAllRecords();
        List<Map<String, Object>> foundTrs = new ArrayList<>();

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            List<Map<String, Object>> trs = (List<Map<String, Object>>) acct.get("transactions");
            for(Map<String, Object> tr : trs){
                if( tr.get("recieverId").equals(id) && !acct.get("accountId").equals(id)){
                    foundTrs.add(tr);
                    // return tr;
                }
            }
        }
        return foundTrs;
    }

    /**
     * Loads all records from the JSON file.
     */
    public List<Map<String, Object>> getAllRecords() {
        return parseJsonNested(DB_FILE);
    }

    /**
     * Saves a NEW account record (used when a new account is created or found by Simulator).
     */
    public void saveRecord(Map<String, Object> userWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        allRecords.add(userWrapper);
        saveAllRecords(allRecords);
    }

    /**
     * Updates an existing account's transaction list.
     * Used by the Simulator/Converter to save history.
     */
    public void updateUserRecord(Map<String, Object> updatedWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        
        // Extract the updated account ID to find the match
        Map<String, Object> updatedAcct = (Map<String, Object>) updatedWrapper.get("account");
        String targetId = (String) updatedAcct.get("accountId");
    
        boolean found = false;
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> currentWrapper = allRecords.get(i);
            Map<String, Object> currentAcct = (Map<String, Object>) currentWrapper.get("account");
    
            if (currentAcct.get("accountId").equals(targetId)) {
                // Replace the old data with the new updated version
                allRecords.set(i, updatedWrapper);
                found = true;
                break;
            }
        }
    
        if (found) {
            saveAllRecords(allRecords); 
        } else {
            // If the account wasn't in transactions.json yet, add it now
            saveRecord(updatedWrapper);
        }
    }

    // ========================================================
    //   Get All Transactions (Convert Maps to Objects)
    // ========================================================
    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> allTransactions = new ArrayList<>();
        
        List<Map<String, Object>> allRecords = getAllRecords();

        for (Map<String, Object> wrapper : allRecords) {
            Map<String, Object> accountMap = (Map<String, Object>) wrapper.get("account");
            
            if (accountMap != null && accountMap.containsKey("transactions")) {
                List<Map<String, String>> transList = (List<Map<String, String>>) accountMap.get("transactions");
                
                if (transList != null) {
                    for (Map<String, String> tMap : transList) {
                        try {
                            String id = tMap.get("transactionId");
                            String sender = tMap.get("senderId");
                            String receiver = tMap.get("recieverId");
                            // Safety replace for currency format
                            double amount = Double.parseDouble(tMap.get("amount").replace(",", ".")); 
                            String date = tMap.get("date");
                            String time = tMap.get("time");
                            String desc = tMap.get("description");
                            String type = tMap.get("type");

                            Transaction t = new Transaction(id, sender, receiver, amount, date, time, desc, type);
                            allTransactions.add(t);
                            
                        } catch (Exception e) {
                            System.err.println("Skipping corrupted transaction: " + tMap);
                        }
                    }
                }
            }
        }
        return allTransactions; 
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
        
            sb.append("      \"transactions\": [\n");
            List<Map<String, String>> transactions = (List<Map<String, String>>) acct.get("transactions");
            if (transactions != null) {
                for (int j = 0; j < transactions.size(); j++) {
                    Map<String, String> tr = transactions.get(j);
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

            content = content.substring(1, content.length() - 1).trim();

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
                    continue; 
                }

                Map<String, Object> userData = new HashMap<>();
                userData.put("accountId", extractValue(block, "accountId"));
                
                List<Map<String, String>> transactions = new ArrayList<>();
                int trStart = block.indexOf("\"transactions\": [");
                if (trStart != -1) {
                    int trEnd = block.lastIndexOf("]");
                    String trsSection = block.substring(trStart + 12, trEnd).trim();
                    
                    if (trsSection.contains("{")) {
                        String[] trParts = trsSection.split("\\},");
                        for (String part : trParts) {
                            if (part.trim().isEmpty()) continue;
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
        
        int start = block.indexOf("\"", index + search.length()) + 1;
        if (start == 0) return ""; 
        
        int end = block.indexOf("\"", start);
        if (end == -1) return "";
        
        return block.substring(start, end);
    }


    public void appendTransactionRecord(Map<String, Object> newWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        
        Map<String, Object> newAcctMap = (Map<String, Object>) newWrapper.get("account");
        String targetId = (String) newAcctMap.get("accountId");
        List<Map<String, String>> newTransactions = (List<Map<String, String>>) newAcctMap.get("transactions");
    
        boolean found = false;
        for (Map<String, Object> record : allRecords) {
            Map<String, Object> existingAcct = (Map<String, Object>) record.get("account");
            
            if (existingAcct.get("accountId").equals(targetId)) {
                // Found the account, let's append only the new transactions
                List<Map<String, String>> existingTransactions = (List<Map<String, String>>) existingAcct.get("transactions");
                if (existingTransactions == null) {
                    existingTransactions = new ArrayList<>();
                    existingAcct.put("transactions", existingTransactions);
                }

                if (newTransactions != null) {
                    for (Map<String, String> newTr : newTransactions) {
                        if(!existingTransactions.contains(newTr)){
                            existingTransactions.add(newTr);
                        }
                    }
                }
                found = true;
                break;
            }
        }
    
        if (found) {
            saveAllRecords(allRecords); 
        } else {
            // Account invalid or new, save normally
            saveRecord(newWrapper);
        }
    }

    
}