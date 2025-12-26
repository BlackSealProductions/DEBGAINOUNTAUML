package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonDatabase {

    private static final String DB_FILE = "accounts.json";

    // ===========================
    //      PUBLIC METHODS
    // ===========================

    // --- Save Data (Used for Registering NEW users) ---
    public static void saveRecord(Map<String, String> newRecord) {
        // 1. Load existing data
        List<Map<String, String>> allRecords = getAllRecords();

        // 2. Add the new record
        allRecords.add(newRecord);

        // 3. Save everything back to the file
        saveAllRecords(allRecords);
    }

    // --- Get All Records (Used for Login) ---
    public static List<Map<String, String>> getAllRecords() {
        return parseJsonFile(DB_FILE);
    }

    // --- Get Specific Record (Useful for checking duplicates/updates) ---
    public static Map<String, String> getRecord(String citizenId) {
        List<Map<String, String>> allData = getAllRecords();
        for (Map<String, String> record : allData) {
            if (record.containsKey("citizenId") && record.get("citizenId").equals(citizenId)) {
                return record;
            }
        }
        return null; 
    }

    // ===========================
    //   PRIVATE HELPERS (Manual JSON Logic)
    // ===========================

    // --- PARSER: Reads the file and converts string -> List<Map> ---
    private static List<Map<String, String>> parseJsonFile(String filename) {
        List<Map<String, String>> dataList = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists()) return dataList;

        try {
            // Read entire file content
            String content = new String(Files.readAllBytes(file.toPath()));
            content = content.trim();

            // Handle empty file or empty JSON array
            if (content.isEmpty() || content.equals("[]")) return dataList;

            // Remove the outer [ and ]
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1);
            }

            // Split by "}, {" to separate objects
            // This regex handles the comma between objects
            String[] rawObjects = content.split("(?<=\\}),\\s*(?=\\{)");

            for (String rawObj : rawObjects) {
                Map<String, String> map = new HashMap<>();
                
                // Clean up braces
                rawObj = rawObj.replace("{", "").replace("}", "").trim();
                
                // Split by comma (assuming no commas inside values for now)
                // Note: This is a simple parser. If a user types a comma in their address, 
                // this manual parser might break. Libraries like Jackson avoid this.
                String[] fields = rawObj.split(",\n"); 
                // Fallback: if not split by newline, try split by comma alone
                if (fields.length == 1) fields = rawObj.split(",");

                for (String field : fields) {
                    if (field.contains(":")) {
                        String[] parts = field.split(":", 2);
                        String key = parts[0].replaceAll("[\"\\s]", ""); // Remove quotes and spaces
                        String value = parts[1].replaceAll("[\"\\s]", ""); // Remove quotes and spaces
                        
                        // If you want to keep spaces inside values (like Name), change regex above:
                        // String value = parts[1].replace("\"", "").trim();
                        
                        map.put(key, cleanValue(parts[1]));
                    }
                }
                dataList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // --- WRITER: Converts List<Map> -> String and writes to file ---
    private static void saveAllRecords(List<Map<String, String>> records) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < records.size(); i++) {
            Map<String, String> record = records.get(i);
            jsonBuilder.append("  {\n");

            int fieldCount = 0;
            for (Map.Entry<String, String> entry : record.entrySet()) {
                jsonBuilder.append(String.format("    \"%s\": \"%s\"", entry.getKey(), entry.getValue()));
                
                // Add comma if not the last field
                if (fieldCount < record.size() - 1) {
                    jsonBuilder.append(",\n");
                } else {
                    jsonBuilder.append("\n");
                }
                fieldCount++;
            }

            jsonBuilder.append("  }");
            
            // Add comma between objects if not the last object
            if (i < records.size() - 1) {
                jsonBuilder.append(",\n");
            } else {
                jsonBuilder.append("\n");
            }
        }
        jsonBuilder.append("]");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            writer.write(jsonBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper to clean up quotes and extra spaces from values
    private static String cleanValue(String raw) {
        return raw.replace("\"", "").trim();
    }
}