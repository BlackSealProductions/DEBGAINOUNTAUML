package App.Model;

import java.util.Map;

import App.Model.Entities.UserEntities.Account;

public class Session {
    private static Session instance;

    private String username;
    private String taxId;
    private Map<String, Object> userData;    // Stores the full nested User Map
    private Account activeAccount; // Stores the currently selected account

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Called during Login
    public void login(String username, String taxId, Map<String, Object> userData) {
        this.username = username;
        this.taxId = taxId;
        this.userData = userData;
    }

    public void logout() {
        this.username = null;
        this.taxId = null;
        this.userData = null;
        this.activeAccount = null;
    }

    // Getters and Setters
    public Map<String, Object> getUserData() { return userData; }
    public String getUsername() { return username; }
    public String getTaxId() { return taxId; }

    // public Map<String, String> getActiveAccount() { return activeAccount; }
    // public void setActiveAccount(Map<String, String> activeAccount) { 
    //     this.activeAccount = activeAccount; 
    // }
    public void setActiveAccount(Account account) {
        this.activeAccount = account;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }
}