package App.Model.Entities.OperationEntities;

public class Bill {
    String rfCode;
    String targetIban;
    Double amount;
    String issueDate;
    String dueDate;

    
    public Bill(String rfCode, Double amount, String issueDate, String dueDate, String targetIban) {
        this.rfCode = rfCode;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.targetIban = targetIban;
    }


    public String getRfCode() {
        return rfCode;
    }


    public void setRfCode(String rfCode) {
        this.rfCode = rfCode;
    }


    public String getTargetIban() {
        return targetIban;
    }


    public void setTargetIban(String targetIban) {
        this.targetIban = targetIban;
    }


    public Double getAmount() {
        return amount;
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public String getIssueDate() {
        return issueDate;
    }


    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }


    public String getDueDate() {
        return dueDate;
    }


    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }



}
