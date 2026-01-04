package App.Model.Entities.OperationEntities;

public class Bill {
    String rfCode;
    Double amount;
    String issueDate;
    String dueDate;
    String targetIban;

    
    public Bill(String rfCode, Double amount, String issueDate, String dueDate, String targetIban) {
        this.rfCode = rfCode;
        this.amount = amount;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.targetIban = targetIban;
    }



}
