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
    UserDB udb;
    
    

    public OrderAssesor(ModelHandler m){
        this.m = m;
        odb = m.get_oDB();
        tdb = m.get_tDB();
        udb = m.get_uDB();

    }



    public void assess(){
        if (m.isSimulationActive()) {
            return;
        }
        fillOrderList();
    }






    public void fillOrderList(){
        List<Map<String,Object>> records = odb.getAllRecords();
        List<StandingOrder> orders = new ArrayList<StandingOrder>();

        for(Map<String,Object> rec: records){

            Map<String,Object> acc = (Map<String,Object>)rec.get("account");
            orders = loadOrders(acc);
            getAllTheMoneys(orders, (String)acc.get("accountId"));
            m.saveChangesToODB_conv_Id(orders, (String)acc.get("accountId"));
        }
    }

    private List<StandingOrder> loadOrders(Map<String, Object> accData){

        List<Map<String, Object>> ordersList = (List<Map<String, Object>>) accData.get("orders");
        List<StandingOrder> orders = new ArrayList<StandingOrder>();
        
        for (Map<String, Object> so : ordersList) {
            String name = (String) so.get("name");
            String acc = (String) so.get("targetIban");
            String orderId = (String) so.get("orderId");
            
            // Ensure amount is handled correctly as a String from JSON before parsing
            Double amount = Double.parseDouble((String) so.get("amount"));
            String day = (String) so.get("day");
            String dueDate = (String) so.get("dueDate");
            String freq = (String) so.get("frequency");
    
            // 2. Extract the new pastcharges list
            List<Double> pastCharges = (List<Double>) so.get("pastcharges");
            if (pastCharges == null) {
                pastCharges = new ArrayList<>();
            }
            // System.out.println("\n\n"+name+acc+orderId+String.valueOf(amount)+day+dueDate+freq+pastCharges+"\n\n");
    
            // 3. Pass the pastCharges list to the constructor
            // Make sure your StandingOrder constructor is updated to accept this list
            StandingOrder newOrder = new StandingOrder(name, acc, orderId, amount, day, freq);
            newOrder.setPastCharges(pastCharges);
            newOrder.setNextIssueDay(dueDate);
            orders.add(newOrder);
        }

       return orders;
    }

    public void getAllTheMoneys(List<StandingOrder> orders, String accId){

        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate presentDay = LocalDate.now();
        presentDay.format(form);
        LocalDate issueDate = null;
        boolean haveCharges=false;
        boolean haveMoney=true;



        for(StandingOrder order : orders){
            issueDate = LocalDate.parse(order.getNextIssueDay(),form);
            
            // Prwta dokimazoume na apoxrewsoume tin lista me ta palia xrwstoumena
            int chargePaidCount = 0;
            List<Double> charges = order.getPastCharges();
            for(Double charge : charges){
                
                Boolean hadEnoughMoney = invadeVenezuela(accId, charge, order);
                if(hadEnoughMoney){
                    chargePaidCount++;
                    // charges.remove(charge);
                    System.out.println("Took "+charge+" from "+accId+"\n");
                }
                else{
                    break;
                }
            }
            for(int i=0; i<chargePaidCount; i++){
                charges.removeFirst();
            }
                           
            // if today is after or == to issueDay
            if(issueDate.isBefore(presentDay) || issueDate.isEqual(presentDay)){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


                order.calcNextDate(order.getPaymentFrequency(),issueDate.format(formatter));

                // try to charge for the currect order
                Boolean hadEnoughMoney = invadeVenezuela(accId, order.getAmount(), order);
                if(!hadEnoughMoney){
                    order.addCharge(order.getAmount());
                }
            }
            
        }   

    }
    
    private Boolean invadeVenezuela(String USAcitizenNumber, double TrumpaneanDept, StandingOrder order){
        
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

            float oppMoney=0f;
            Map<String, Object> opUser = null;
            Map<String, String> receivingAcc = null;
            boolean targetFoundInDb = false;

    
            if (accounts != null) {
                for (Map<String, String> acc : accounts) {
                    if (acc.get("accountId").equals(USAcitizenNumber)) {
                        // enough oil mony
                        float currentBalance = Float.parseFloat(acc.get("balance"));
                        if(currentBalance >= TrumpaneanDept){
                            acc.put("balance", String.valueOf(currentBalance-TrumpaneanDept));
                            uDB.updateUserRecord(userWrapper);
                            Map<String, Object> targetacc = uDB.findAccountWithIban(order.getAccountIban());
                            Transaction trans = new Transaction(String.valueOf(System.currentTimeMillis()),acc.get("accountId"),targetacc != null ? (String)targetacc.get("accountId") : "unknown", TrumpaneanDept,LocalDate.now().toString(),LocalTime.now().toString(),"Πάγια Χρέωση","send");
                            tdb.addTransactionToWrapper(USAcitizenNumber,m.getConverter().convertTransactionToMap(trans));

                            
                            
                            for (Map<String, Object> opWrapper : userRecords) {
                                Map<String, Object> op = (Map<String, Object>) opWrapper.get("user");
                                List<Map<String, String>> opAccs = (List<Map<String, String>>) op.get("accounts");
                                
                                if (opAccs != null) {
                                    for (Map<String, String> account : opAccs) {
                                        if (account.get("iban").equals(order.getAccountIban())) {
                                            System.out.println("Name: "+account.get("ownerName"));
                                            opUser = user;
                                            receivingAcc = account;
                                            targetFoundInDb = true;
                                            // If we are in "In-Bank" mode, update the receiver's balance
                                            if (targetFoundInDb) {
                                                oppMoney = Float.parseFloat(receivingAcc.get("balance")); 
                                                receivingAcc.put("balance", String.valueOf(oppMoney + TrumpaneanDept));
                                                uDB.updateUserRecord(opWrapper);
                                            }
                                        }
                                    }
                                }
                            }
                            // add money to Oppressor



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
