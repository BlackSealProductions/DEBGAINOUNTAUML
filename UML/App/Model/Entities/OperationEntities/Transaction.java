package App.Model.Entities.OperationEntities;

public class Transaction {
    private String transactionId;
    private String senderId;
    private String recieverId; 
    private double amount;     // Changed to double to match Controller
    private String date;       // Changed to String to match JSON storage
    private String time;      
    private String description;
    private String type;      

    // Updated Constructor
    public Transaction(String transactionId, String senderId, String recieverId, double amount, String date, String time, String description, String type) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.amount = amount;
        this.date = date;
        this.time = time;     
        this.description = description;
        this.type = type;
    }

    // --- Getters and Setters ---
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getRecieverId() { return recieverId; }
    public void setRecieverId(String recieverId) { this.recieverId = recieverId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}