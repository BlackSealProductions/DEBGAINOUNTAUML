package App.Model.Entities.OperationEntities;

public class Withdrawal extends Transaction{

    public Withdrawal(String id, String sender, String receiver, float amount, String date, String time, String desc, String type) {
        super(id, sender, receiver, amount, date, time, desc, type);
        //TODO Auto-generated constructor stub
    }
    
}
