package App.Model.Entities.OperationEntities;

import java.util.Date;

public abstract class Transaction {
    private String transactionId;
    private String senderID;
    private String recieverID;
    private float ammount;
    private Date date;
    private String description;

    ////////IDK WHATS DIS??////////
    private String type;

    public Transaction(String transactionId, String senderID, String recieverID, float ammount, Date date,
            String description) {
        this.transactionId = transactionId;
        this.senderID = senderID;
        this.recieverID = recieverID;
        this.ammount = ammount;
        this.date = date;
        this.description = description;
    }








    

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }

    public float getAmmount() {
        return ammount;
    }

    public void setAmmount(float ammount) {
        this.ammount = ammount;
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
