package App.Model.Entities.UserEntities;

import java.util.ArrayList;
import java.util.List;

import Utils.GlobalConsts;

public abstract class Customer extends User {

    String email;
    String phone;
    GlobalConsts.userType customerType;
    String taxId;
    
    // Initialize to prevent crash
    List<Account> accounts = new ArrayList<>();

    // --- Getters ---
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    // This is the specific method the Converter was looking for
    public String getPhoneNumber() { return phone; }

    public GlobalConsts.userType getUserType() { return customerType; }
    
    public String getUserTypeString() {
        return customerType == Utils.GlobalConsts.userType.BUSINESS ? "Company" : 
               customerType == Utils.GlobalConsts.userType.INDIVIDUAL ? "Individual" : null;
    }

    public String getTaxId(){ return taxId; }
    
    public List<Account> getAccounts(){ return this.accounts; }

    // --- Setters ---
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    
    // Standard setter
    public void setPhoneNumber(String phone) { this.phone = phone; } 

    public void setAccounts(List<Account> accs){
        this.accounts = accs;
    }

    public void addAccount(Account account) {
        if (this.accounts == null) {
            this.accounts = new ArrayList<>();
        }
        this.accounts.add(account);
    }
}