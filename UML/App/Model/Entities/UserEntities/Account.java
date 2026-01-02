package App.Model.Entities.UserEntities;

import java.util.ArrayList;
import java.util.List;

import App.Model.Entities.OperationEntities.Transaction;

public class Account {
    private String accountId; 
    private String ownerName;
    private String iban;
    private String balance;
    private String interestRate;
    private String secondaryOwner;

    private List<Transaction> transactions;

    public Account(String citizenId, String ownerName, String iban, String balance, String interestRate, String secondaryOwner) {
        this.accountId = citizenId;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
        this.interestRate = interestRate;
        this.secondaryOwner = secondaryOwner;
    }

    // --- Getters ---
    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public String getIban() { return iban; }
    public String getBalance() { return balance; }
    public String getInterestRate() { return interestRate; }
    public String getSecondaryOwner() { return secondaryOwner; }
    
    // Setters
    public void setBalance(String balance) { this.balance = balance; }

    public void addTransaction(Transaction t) {
        if (t != null) {
            this.transactions.add(t);
        }
    }

    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}