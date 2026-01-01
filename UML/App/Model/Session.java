package App.Model;

import App.Model.Entities.UserEntities.User;

import java.util.List;

import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;

public class Session {
    private static Session instance;

    private String username;
    private String taxId;
    private List<Account> accounts=null;
    // private Map<String, Object> userData;    // Stores the full nested User Map
    // --- CHANGED: Now we store the Object, not just the map ---
    private User currentUser;


    private Account activeAccount=null;
    private Customer activeCustomer=null;
    
                                                                                                                   
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

    public List<Account> getCustomerAccounts(){
        return this.customerAccounts;
    }

    public Customer getActiveCustomer(){
        return this.activeCustomer;
    }
}