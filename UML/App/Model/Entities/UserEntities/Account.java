package App.Model.Entities.UserEntities;

public class Account {
    private String accountId;
    private String ownerName;
    private String iban;
    private String balance;
    private String interestRate;
    private String secondaryOwner;

    public Account(String accountId, String ownerName, String iban, 
                   String balance, String interestRate, String secondaryOwner) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
        this.interestRate = interestRate;
        this.secondaryOwner = secondaryOwner;
    }

    // --- GETTERS (Fixes "undefined" errors) ---
    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public String getIban()      { return iban; }
    public String getBalance()   { return balance; }
    public String getInterestRate() { return interestRate; }
    public String getSecondaryOwner() { return secondaryOwner; }

    // --- LOGIC (Fixes "deposit undefined") ---
    public void deposit(double amount) {
        double current = Double.parseDouble(this.balance);
        current += amount;
        this.balance = String.valueOf(current);
    }
}