package App.Model.Entities.OperationEntities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import App.Model.Entities.UserEntities.Account;



public class StandingOrder {
    private Account account;
    private Transaction transaction;
    private String orderId;
    private float amount;
    private LocalDate presentDay;
    private LocalDate nextIssueDay;
    private Frequency paymentFrequency;
    private boolean isNotPaidOnTime;

    public enum Frequency {
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
    
    


    public StandingOrder(Account account, Transaction transaction, String orderId, float amount, LocalDate presentDay,
            Frequency paymentFrequency) {
        this.account = account;
        this.transaction = transaction;
        this.orderId = orderId;
        this.amount = amount;
        this.presentDay = presentDay;
        this.paymentFrequency = paymentFrequency;
        isNotPaidOnTime = false;
    }




    

    public Account getAccount() {
        return account;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public String getOrderId() {
        return orderId;
    }
    public float getAmount() {
        return amount;
    }
    public LocalDate getPresentDay() {
        return presentDay;
    }
    public LocalDate getNextIssueDay() {
        return nextIssueDay;
    }
    public Frequency getPaymentFrequency() {
        return paymentFrequency;
    }

    public boolean isNotPaidOnTime() {
        return isNotPaidOnTime;
    }

    public void setNotPaidOnTime(boolean isNotPaidOnTime) {
        this.isNotPaidOnTime = isNotPaidOnTime;
    }


    public LocalDate calcNextDate(Frequency freq){
        LocalDate nextDate=null;

        


        return nextDate;
    }


    public void completePayment(){


    }

    
    
}
