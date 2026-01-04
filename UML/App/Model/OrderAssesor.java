package App.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import App.Model.Database.OrderDB;
import App.Model.Database.TransactionDB;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.OperationEntities.Transaction;

public class OrderAssesor {

    ModelHandler m;
    OrderDB odb;
    TransactionDB tdb;
    
    

    public OrderAssesor(ModelHandler m){
        this.m = m;
        odb = m.get_oDB();
        tdb = m.get_tDB();

    }



    public void assess(){
        fillOrderList();
    }






    public void fillOrderList(){
        List<Map<String,Object>> records = odb.getAllRecords();
        List<StandingOrder> orders = new ArrayList<StandingOrder>();

        for(Map<String,Object> rec: records){

            Map<String,Object> acc = (Map<String,Object>)rec.get("account");
            orders = loadOrders(acc);
            getAllTheMoneys(orders, (String)acc.get("accountId"));
            
        }
    }

    private List<StandingOrder> loadOrders(Map<String, Object> accData){

        List<Map<String, String>> ordersList = (List<Map<String, String>>) accData.get("orders");
        List<StandingOrder> orders = new ArrayList<StandingOrder>();


        
        for (Map<String, String> so : ordersList) {

            String name = so.get("name");
            String acc = so.get("targetIban");
            TransactionDB tDB = m.get_tDB();
            Map<String, Object> trMap = tDB.findTransactionWithId(so.get("transactionId"));
            // Transaction tr = genTransactionObject(trMap);
            String orderId = so.get("orderId");
            Double amount = Double.parseDouble(so.get("amount"));
            String day = so.get("day");
            String freq = so.get("frequency");

            StandingOrder or = new StandingOrder(name, acc, orderId, amount, day, freq);
            // or.calcNextDate(freq, day);
            or.setNextIssueDay(so.get("dueDate"));
            orders.add(or);

            // foundAcc=true;
       }

       return orders;
    }

    public void getAllTheMoneys(List<StandingOrder> orders, String accId){

        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate presentDay = LocalDate.now();
        presentDay.format(form);
        LocalDate issueDate = null;
        Map<String,Object> targetAcc;


        for(StandingOrder order : orders){
            issueDate = LocalDate.parse(order.getNextIssueDay(),form);
            System.out.println(presentDay.format(form));
            System.out.println("\n");
            System.out.println(issueDate.format(form));
            // if today is after or == to issueDay
            if(issueDate.isBefore(presentDay) || issueDate.isEqual(presentDay)){
                System.out.println(true);
                invadeVenezuela(accId, order.getAmount());
            }

                


            else{System.out.println(false);}
        }   

    
        // --- DATABASE LOGIC ---
        
    }
    
    private boolean invadeVenezuela(String USAcitisenNumber, double TrumpaneanDept){
        
        boolean isPoorVenesualeanBoy = true;
        boolean surprisinglyNo = false;
        UserDB uDB = m.get_uDB();
        List<Map<String, Object>> userRecords = uDB.getAllRecords();
    
        boolean foundOpp = false;
        // String myUsername = Session.getInstance().getActiveCustomer().getUsername();
        Map<String, Object> OppAccs = null;
    
        // 1. Scan Database to find Target 
        for (Map<String, Object> userWrapper : userRecords) {
            Map<String, Object> user = (Map<String, Object>) userWrapper.get("user");
            List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
    
            if (accounts != null) {
                for (Map<String, String> acc : accounts) {
                    if (acc.get("accountId").equals(USAcitisenNumber)) {
                        // enough oil mony
                        float currentBalance = Float.parseFloat(acc.get("balance"));
                        if(currentBalance >= TrumpaneanDept){
                            acc.put("balance", String.valueOf(currentBalance-TrumpaneanDept));
                            uDB.updateUserRecord(userWrapper);
                            Transaction trans = new Transaction(String.valueOf(System.currentTimeMillis()),acc.get("accountId"),"opp",TrumpaneanDept,LocalDate.now().toString(),LocalTime.now().toString(),"Πάγια Χρέωση","send");
                            tdb.addTransactionToWrapper(USAcitisenNumber,m.getConverter().convertTransactionToMap(trans));

                            return true;
                        }
                        else{return false;}
                    }
                }
            }
        }
        return false;
    }

    
    
    
// ()ccAtegraTteg tcejbO ,gnirtS><paM cilbu
// ????????????????????????????????????????

}
