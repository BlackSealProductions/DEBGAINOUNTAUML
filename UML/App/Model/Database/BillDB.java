package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BillDB {
    private static final String DB_FILE = "bills.json";

    public BillDB(){
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

    public void addBillToWrapper(String id, Map<String, String> bill) {
        List<Map<String, Object>> records = getAllRecords(); // This loads existing or empty list
    
        boolean found = false;
        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            if (acct.get("accountId").equals(id)) {
                List<Map<String, String>> bills = (List<Map<String, String>>) acct.get("bills");
                if (bills == null) {
                    bills = new ArrayList<>();
                    acct.put("bills", bills);
                }
                bills.add(bill);
                found = true;
                break;
            }
        }
    
        // If the account wasn't in bills.json yet, create a new wrapper for it
        if (!found) {
            Map<String, Object> newAccData = new HashMap<>();
            newAccData.put("accountId", id);
            List<Map<String, String>> bills = new ArrayList<>();
            bills.add(bill);
            newAccData.put("bills", bills);
    
            Map<String, Object> wrapper = new HashMap<>();
            wrapper.put("account", newAccData);
            records.add(wrapper);
        }
    
        // CRITICAL: This method MUST call saveAllRecords(records)
        saveAllRecords(records); 
    }

    public Map<String, String> findBillWithRfCode(String rfCode){
        List<Map<String, Object>> records = getAllRecords();

        for (Map<String, Object> wrapper : records) {
            Map<String, Object> acct = (Map<String, Object>) wrapper.get("account");
            List<Map<String, String>> bills = (List<Map<String, String>>) acct.get("bills");
            if (bills != null) {
                for (Map<String, String> bill : bills) {
                    if (bill.get("rfCode").equals(rfCode)) {
                        return bill;
                    }
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> getAllRecords() {
        return parseJsonNested(DB_FILE);
    }

    public void saveRecord(Map<String, Object> wrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        allRecords.add(wrapper);
        saveAllRecords(allRecords);
    }

    public void updateUserRecord(Map<String, Object> updatedWrapper) {
        List<Map<String, Object>> allRecords = getAllRecords();
        Map<String, Object> updatedAcct = (Map<String, Object>) updatedWrapper.get("account");
        String targetId = (String) updatedAcct.get("accountId");
    
        boolean found = false;
        for (int i = 0; i < allRecords.size(); i++) {
            Map<String, Object> currentWrapper = allRecords.get(i);
            Map<String, Object> currentAcct = (Map<String, Object>) currentWrapper.get("account");
    
            if (currentAcct.get("accountId").equals(targetId)) {
                allRecords.set(i, updatedWrapper);
                found = true;
                break;
            }
        }
    
        if (found) {
            saveAllRecords(allRecords); 
        } else {
            saveRecord(updatedWrapper);
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
        
            sb.append("      \"bills\": [\n");
            List<Map<String, String>> bills = (List<Map<String, String>>) acct.get("bills");
            if (bills != null) {
                for (int j = 0; j < bills.size(); j++) {
                    Map<String, String> bill = bills.get(j);
                    if (bill.get("rfCode") == null || bill.get("rfCode").isEmpty()) continue;
    
                    sb.append("        {\n");
                    sb.append(String.format("          \"rfCode\": \"%s\",\n", bill.get("rfCode")));
                    sb.append(String.format("          \"iban\": \"%s\",\n", bill.get("iban")));
                    sb.append(String.format("          \"amount\": \"%s\",\n", bill.get("amount")));
                    sb.append(String.format("          \"due\": \"%s\",\n", bill.get("due")));
                    sb.append(String.format("          \"issue\": \"%s\"\n", bill.get("issue")));
                    sb.append("        }");
                    if (j < bills.size() - 1) sb.append(",");
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
    
        try (java.io.PrintWriter out = new java.io.PrintWriter(DB_FILE)) {
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
                String accountId = extractValue(block, "accountId");
                if (accountId == null || accountId.trim().isEmpty()) continue; 

                Map<String, Object> accountData = new HashMap<>();
                accountData.put("accountId", accountId);
                
                List<Map<String, String>> bills = new ArrayList<>();
                int billStart = block.indexOf("\"bills\": [");
                if (billStart != -1) {
                    int billEnd = block.lastIndexOf("]");
                    String billsSection = block.substring(billStart + 9, billEnd).trim();
                    
                    if (billsSection.contains("{")) {
                        String[] billParts = billsSection.split("\\},");
                        for (String part : billParts) {
                            if (part.trim().isEmpty()) continue;
                            String rf = extractValue(part, "rfCode");
                            if (rf.isEmpty()) continue; 

                            Map<String, String> bill = new HashMap<>();
                            bill.put("rfCode", rf);
                            bill.put("iban", extractValue(part, "iban"));
                            bill.put("amount", extractValue(part, "amount"));
                            bill.put("due", extractValue(part, "due"));
                            bill.put("issue", extractValue(part, "issue"));
                            bills.add(bill);
                        }
                    }
                }
                accountData.put("bills", bills);
                
                Map<String, Object> wrapper = new HashMap<>();
                wrapper.put("account", accountData);
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