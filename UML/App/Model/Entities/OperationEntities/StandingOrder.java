package App.Model.Entities.OperationEntities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import App.Model.Entities.UserEntities.Account;
 


public class StandingOrder {
    private String accountIban;
    private Transaction transaction;
    private String orderId;
    private double amount;
    private String presentDay;
    private String nextIssueDay;
    private String paymentFrequency;
    private boolean isNotPaidOnTime;
    private String name;
    
        public enum Frequency {
            DAY,
            WEEK,
            MONTH,
            YEAR
        }
    
        public StandingOrder(String name, String accountIban, Transaction transaction, String orderId, double amount, String presentDay, String paymentFrequency) {
            this.name = name;
        this.accountIban = accountIban;
        this.transaction = transaction;
        this.orderId = orderId;
        this.amount = amount;
        this.presentDay = presentDay;
        this.paymentFrequency = paymentFrequency;
        isNotPaidOnTime = false;
    }

    

    public String getAccountIban() {
        return accountIban;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public String getOrderId() {
        return orderId;
    }
    public double getAmount() {
        return amount;
    }
    public String getPresentDay() {
        return presentDay;
    }
    public String getNextIssueDay() {
        return nextIssueDay;
    }
    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public boolean isNotPaidOnTime() {
        return isNotPaidOnTime;
    }

    public void setNotPaidOnTime(boolean value) {
        this.isNotPaidOnTime = value;
    }


    public LocalDate calcNextDate(Frequency freq){
        LocalDate nextDate=null;

        


        return nextDate;
    }


    public void completePayment(){


    }

    public String getName() {
        return name;
    }

    
    
}
