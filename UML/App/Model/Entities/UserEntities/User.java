package App.Model.Entities.UserEntities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String type;
    private String taxId;
    private List<Account> accounts;

    public User(String username, String password, String name, String surname, 
                String phone, String email, String type, String taxId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.taxId = taxId;
        this.accounts = new ArrayList<>();
    }

    // --- GETTERS ---
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName()     { return name; }
    public String getSurname()  { return surname; }
    public String getPhone()    { return phone; }
    public String getEmail()    { return email; }
    public String getType()     { return type; }
    public String getTaxId()    { return taxId; }
    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account acc) {
        this.accounts.add(acc);
    }
}