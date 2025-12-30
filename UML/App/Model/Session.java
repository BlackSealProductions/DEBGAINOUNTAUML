package App.Model;

import App.Model.Entities.UserEntities.Account;

public class Session {

    private static Session instance;

    // Data to keep in memory
    private String username;
    private String citizenId;
    private Account activeAccount; 

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void login(String username, String citizenId, Account account) {
        this.username = username;
        this.citizenId = citizenId;
        this.activeAccount = account;
        System.out.println("SESSION START: " + username);
    }

    public void logout() {
        this.username = null;
        this.citizenId = null;
        this.activeAccount = null;
    }

    public boolean isLoggedIn() { return username != null; }
    public Account getAccount() { return activeAccount; }
    public String getCitizenId() { return citizenId; }
}