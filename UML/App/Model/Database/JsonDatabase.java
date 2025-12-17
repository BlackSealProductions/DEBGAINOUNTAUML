package App.Model.Database;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class JsonDatabase {

    private static final String DB_FILE = "users.json";

    // --- WRITE: Save a User Profile ---
    public static void saveUser(Map<String, String> userProfile) {
        try {
            File file = new File(DB_FILE);
            List<String> lines = new ArrayList<>();
            
            // 1. Read existing file
            if (file.exists()) {
                lines = Files.readAllLines(file.toPath());
            }

            // 2. Prepare the new JSON Object string
            String jsonEntry = mapToJson(userProfile);

            // 3. Write Mode:
            // If file is empty, create new array.
            // If file exists, remove the last "]" and append the new object.
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            
            if (lines.isEmpty() || lines.size() <= 1) {
                writer.write("[\n");
                writer.write(jsonEntry);
                writer.write("\n]");
            } else {
                // Rewrite everything except the last line (the closing bracket)
                for (int i = 0; i < lines.size() - 1; i++) {
                    writer.write(lines.get(i));
                    writer.newLine();
                }
                writer.write("  ,\n"); // Add comma
                writer.write(jsonEntry);
                writer.write("\n]"); // Add closing bracket back
            }
            writer.close();
            System.out.println("Profile saved for: " + userProfile.get("name"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- READ: Get a User Profile by Citizen ID ---
    // This proves it is "easy to get afterwards"
    public static Map<String, String> getUserProfile(String citizenId) {
        List<Map<String, String>> allUsers = getAllUsers();
        
        for (Map<String, String> user : allUsers) {
            if (user.get("citizenId").equals(citizenId)) {
                return user; // Found the profile!
            }
        }
        return null; // Not found
    }

    // --- READ HELPER: Parse the JSON text file manually ---
    public static List<Map<String, String>> getAllUsers() {
        List<Map<String, String>> users = new ArrayList<>();
        try {
            File file = new File(DB_FILE);
            if (!file.exists()) return users;

            String content = new String(Files.readAllBytes(file.toPath()));
            
            // SUPER SIMPLE PARSER (Splits by curly braces "}")
            // Note: This relies on the specific formatting we used in saveUser
            String[] rawObjects = content.split("\\},"); 

            for (String rawObj : rawObjects) {
                Map<String, String> userMap = new HashMap<>();
                
                // Clean up the string to find "key": "value" pairs
                String[] lines = rawObj.split("\n");
                for (String line : lines) {
                    if (line.contains(":")) {
                        String[] parts = line.split(":", 2);
                        String key = parts[0].replaceAll("[\"{}, ]", "");
                        String value = parts[1].replaceAll("[\"{},]", "").trim();
                        userMap.put(key, value);
                    }
                }
                if (!userMap.isEmpty()) {
                    users.add(userMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // --- HELPER: Formats Map to JSON String ---
    private static String mapToJson(Map<String, String> data) {
        StringBuilder json = new StringBuilder();
        json.append("  {\n");
        int size = data.size();
        int count = 0;
        
        for (Map.Entry<String, String> entry : data.entrySet()) {
            json.append(String.format("    \"%s\": \"%s\"", entry.getKey(), entry.getValue()));
            if (++count < size) {
                json.append(",\n");
            } else {
                json.append("\n");
            }
        }
        json.append("  }");
        return json.toString();
    }
}