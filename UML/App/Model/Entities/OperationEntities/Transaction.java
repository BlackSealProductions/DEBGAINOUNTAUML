package App.Model.Entities.OperationEntities;

import java.util.Date;

public abstract class Transaction {
    private String transactionId;
    private String senderId;
    private String recieverId;
    private float amount;
    private Date date;
    private String description;
    private String type;




public Transaction(String id, String sender, String receiver, float amount, String date, String desc, String type) {
    this.transactionId = id;
    this.senderId = sender;
    this.recieverId = receiver;
    this.amount = amount;
    this.description = desc;
    this.type = type;
}







    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }


    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public float getAmmount() {
        return amount;
    }


    public void setAmmount(float ammount) {
        this.amount = ammount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    
}
