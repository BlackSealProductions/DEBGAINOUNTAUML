package App.Model.Entities.OperationEntities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Transaction {
    private String transactionId;
    private String senderId;
    private String recieverId;
    private float amount;
    private String date;
    private String time;
    private String description;
    private String type;      




public Transaction(String id, String sender, String receiver, float amount, String date, String time, String desc, String type) {
    this.transactionId = id;
    this.senderId = sender;
    this.recieverId = receiver;
    this.amount = amount;
    this.date = date;
    this.time = time;
    this.description = desc;
    this.type = type;
}


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        // this.senderId = senderId;
        // this.recieverId = recieverId;
    }

    public float getAmmount() {
        return amount;
    }


    public void setAmmount(float ammount) {
        this.amount = ammount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        // this.time = time;     
        // this.description = description;
        // this.type = type;
    }

    
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getRecieverId() { return recieverId; }
    public void setRecieverId(String recieverId) { this.recieverId = recieverId; }

    public double getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}