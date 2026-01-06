package App.Model.Entities.OperationEntities;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import App.Model.Entities.UserEntities.Account;

public class StandingOrder {
    private String accountIban;
    private Transaction transaction; // Note: This field seems unused in the constructor, but kept for your structure
    private String orderId;
    private double amount;
    private String presentDay;      // The "Start Date"
    private String nextIssueDay;    // The "Actual Next Due Date"
    private String paymentFrequency;
    private boolean isNotPaidOnTime;
    private String name;

    private List<Double> pastCharges = new ArrayList<Double>();
    
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
        
        // KEEP THIS COMMENTED OUT! 
        // We do NOT want to calculate the next date automatically here.
        // The loader (OrderAssesor or Simulator) will set the correct next date from the DB.
        // this.nextIssueDay = calcNextDate(paymentFrequency,presentDay); 
        
        this.isNotPaidOnTime = false;
    }

    // --- GETTERS & SETTERS ---

    public String getAccountIban() { return accountIban; }
    public Transaction getTransaction() { return transaction; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getPresentDay() { return presentDay; }
    public String getNextIssueDay() { return nextIssueDay; }
    public String getPaymentFrequency() { return paymentFrequency; }
    public String getName() { return name; }

    public void setPresentDay(String presentDay) {
        this.presentDay = presentDay;
    }

    public void setNextIssueDay(String nextIssueDay) {
        this.nextIssueDay = nextIssueDay;
    }

    public boolean isNotPaidOnTime() { return isNotPaidOnTime; }
    public void setNotPaidOnTime(boolean value) { this.isNotPaidOnTime = value; }

    // --- LOGIC ---

    /**
     * Calculates the next due date based on the PROVIDED baseDate.
     * Updates the internal 'nextIssueDay' field.
     */
    public void calcNextDate(String freq, String baseDate){
        if (baseDate == null || freq == null) return;

        LocalDate nextDate = null;
        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate date = LocalDate.parse(baseDate, form);

            // Added support for both GREEK and ENGLISH frequency keys
            if(freq.equalsIgnoreCase("ΜΗΝΑ") || freq.equalsIgnoreCase("MONTH")){
                nextDate = date.plusMonths(1);
            }
            else if (freq.equalsIgnoreCase("ΧΡΟΝΟ") || freq.equalsIgnoreCase("YEAR")){
                nextDate = date.plusYears(1);
            }
            else if (freq.equalsIgnoreCase("DAY") || freq.equalsIgnoreCase("ΜΕΡΑ")) { // Optional support
                nextDate = date.plusDays(1);
            }

            if (nextDate != null) {
                setNextIssueDay(nextDate.format(form));
            }
        } catch (Exception e) {
            System.out.println("Error parsing date in StandingOrder: " + baseDate);
        }
    }

    public void completePayment(){
        // Logic for completion if needed
    }

    public void addCharge(Double t) {
        if (t != null) {
            this.pastCharges.add(t);
        }
    }

    public List<Double> getPastCharges(){
        return this.pastCharges;
    }

    public void setPastCharges(List<Double> charges){
        this.pastCharges = charges;
    }
}