package App.Model.Entities.OperationEntities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    
        public StandingOrder(String name, String accountIban, String orderId, double amount, String presentDay, String paymentFrequency) {
            this.name = name;
        this.accountIban = accountIban;
        this.orderId = orderId;
        this.amount = amount;
        this.presentDay = presentDay;
        this.paymentFrequency = paymentFrequency;
        // this.nextIssueDay = calcNextDate(paymentFrequency,presentDay);
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
    

    public void setNextIssueDay(String nextIssueDay) {
        this.nextIssueDay = nextIssueDay;
    }



    public void calcNextDate(String freq,String present){
        LocalDate nextDate=null;

        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate birthdayOfOrder = LocalDate.parse(present,form);

        if(freq.equals("ΜΗΝΑ")){
            nextDate = birthdayOfOrder.plusMonths(1);
        }
        else if (freq.equals("ΧΡΟΝΟ")){
            nextDate = birthdayOfOrder.plusYears(1);
        }

        setNextIssueDay(nextDate.format(form));
    }


    public void completePayment(){


    }

    public String getName() {
        return name;
    }

    
    
}
