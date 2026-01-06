package App.Model.Entities.OperationEntities;

public class Deposit extends Transaction{

    public Deposit(String id, String sender, String receiver, Double amount, String date, String time,String desc, String type) {
        super(id, sender, receiver, amount, date, time, desc, type);
        //TODO Auto-generated constructor stub
    }
    
}
