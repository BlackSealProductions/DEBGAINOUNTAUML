package App.Model.Entities.UserEntities;

import java.util.ArrayList;
import java.util.List;

import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.OperationEntities.Transaction;

public class Account {
    private String accountId; 
    private String ownerName;
    private String iban;
    private String balance;
    private String interestRate;
    private String secondaryOwner;
    private String rfCode;

    private List<Transaction> transactions;
    private List<StandingOrder> standingOrders = new ArrayList<StandingOrder>();

    public Account(String citizenId, String ownerName, String iban, String balance, String interestRate, String secondaryOwner) {
        this.accountId = citizenId;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
        this.interestRate = interestRate;
        this.secondaryOwner = secondaryOwner;
    }
    public Account(String citizenId, String ownerName, String iban, String balance, String interestRate, String secondaryOwner, String rfCode) {
        this.accountId = citizenId;
        this.ownerName = ownerName;
        this.iban = iban;
        this.balance = balance;
        this.interestRate = interestRate;
        this.secondaryOwner = secondaryOwner;
        this.rfCode = rfCode;
    }

    // --- Getters ---
    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public String getIban() { return iban; }
    public String getBalance() { return balance; }
    public String getInterestRate() { return interestRate; }
    public String getSecondaryOwner() { return secondaryOwner; }
    public String getRfCode() { return rfCode; }

    
    // Setters
    public void setBalance(String balance) { this.balance = balance; }
    public void setRfCode(String rfCode) { this.rfCode = rfCode; }
    
    public void setSecondaryOwner(String secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }
    
    public void addTransaction(Transaction t) {
        if (t != null) {
            if(this.transactions==null){
                this.transactions= new ArrayList<Transaction>();
            }
       
            this.transactions.add(t);
        }
    }
    
    
    public void setTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }
    
    
    public void addOrder(StandingOrder s) {
       
            this.standingOrders.add(s);
    }

    public void setStandingorders(List<StandingOrder> standingorders) {
        this.standingOrders = standingorders;
    }

    public List<StandingOrder> getStandingorders() {
        return standingOrders;
    }





    
}