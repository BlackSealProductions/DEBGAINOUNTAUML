package App.Model;

import App.Model.Entities.UserEntities.User;
import App.Model.Entities.UserEntities.Account;

public class Session {
    private static Session instance;

    // --- CHANGED: Now we store the Object, not just the map ---
    private User currentUser;
    private Account activeAccount;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // --- CHANGED: Login now takes the full User Object ---
    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
        this.activeAccount = null;
    }

    // --- NEW GETTER: This fixes "The method getCurrentUser() is undefined" ---
    public User getCurrentUser() {
        return currentUser;
    }

    // --- ACCOUNT HANDLING ---
    public void setActiveAccount(Account account) {
        this.activeAccount = account;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }
}