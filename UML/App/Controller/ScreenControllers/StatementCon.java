package App.Controller.ScreenControllers;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.TransactionDB;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.*;
import App.Model.Entities.OperationEntities.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StatementCon  implements Controller_t{

    private StatementsScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;

    List<Transaction> accTransactions;
    List<Statement> accStatements;

    Account acc;

  
    // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public StatementCon(StatementsScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
     
    }

    @Override
    public void init() {
        if (view == null)return;
    }
    
    
    public List<Transaction> getReceivedTransactions(String recId){

        TransactionDB tDB = model.get_tDB();
        List<Map<String, Object>> trMaps = tDB.findTransactionsWithRecId(recId);
        List<Transaction> trList = new ArrayList<>();

        for(Map<String, Object> trMap : trMaps){

            if(((String)trMap.get("description")).equals("Deposit") || ((String)trMap.get("description")).equals("Withdrawal")){
                continue;
            }

            String id = (String)trMap.get("transactionId");
            String sender = (String)trMap.get("senderId");
            String receiver = (String)trMap.get("recieverId");
            Double amount = Double.parseDouble((String)trMap.get("amount"));
            String date = (String)trMap.get("date");
            String time = (String)trMap.get("time");
            String desc = (String)trMap.get("description");
            String type = "receive";

            Transaction tr = new Transaction(id, sender, receiver, amount, date, time, desc, type);
            trList.add(tr);
        }

        return trList;

    }
    
    public void onEnter(Account acc){
        List<Map<String, Object>> accTrans = new ArrayList();
        List<Transaction> receivedTrans = null;
        accTransactions = null;
        
        acc = Session.getInstance().getActiveAccount();
        accTransactions = acc.getTransactions();
        receivedTrans = getReceivedTransactions(acc.getAccountId());
        accTransactions.addAll(receivedTrans);

        System.out.println(accTransactions);
        // accStatements = genStatements(accTransactions, acc);
        accTrans = genStatements2(accTransactions, acc);
        view.giveAccStatements2(accTrans);
        
        // System.out.println(accStatements);
        // view.giveAccStatements(accStatements);
    }
    //
    public List<Map<String, Object>> genStatements2(List<Transaction> trans, Account acc){

        UserDB uDB = model.get_uDB();
        List<Statement> finalStatements = new ArrayList<Statement>();
        List<Map<String, Object>> finalStates = new ArrayList<Map<String, Object>>();

        if(trans !=null){

            for(Transaction transaction : trans){
            
                Map<String,Object> senderData = uDB.findAccountWithId(transaction.getSenderId());
                Map<String,Object> recieverData = uDB.findAccountWithId(transaction.getRecieverId());
                String transactorName = null;

                Map<String, Object> entryMap =new HashMap<>();


                if(transaction.getDescription().equals("Withdrawal")){
                    transactorName=transaction.getRecieverId();
                }
                else if(transaction.getDescription().equals("Deposit")){
                    transactorName=transaction.getSenderId();
                }
                else if(transaction.getType().equals("receive")){

                    transactorName=resolveName(senderData, transaction);
                }
                else if(transaction.getType().equals("send")){

                    transactorName=resolveName(recieverData, transaction);     
                }
                
                Statement state = new Statement(generatePin(6),acc,LocalDate.now(),transaction);
                entryMap.put("state", state);
                entryMap.put("trName", transactorName);

                finalStates.add(entryMap);


                // finalStatements.add();

            }
        }

        return finalStates;
    }

    private String resolveName(Map<String, Object> userdata, Transaction tr){

        String transactorName="";
        if(userdata==null){
            transactorName=tr.getRecieverId();
        }
        else if(userdata.containsKey("name")){
            transactorName = (String)userdata.get("name")+" "+(String)userdata.get("surname");
        }
        else if(userdata.containsKey("companyName")){
            transactorName = (String)userdata.get("companyName");
        }        
        else{
            transactorName = (String)userdata.get("username");
        }  
        return transactorName;
    }

    public List<Statement> genStatements(List<Transaction> trans, Account acc){

        List<Statement> finalStatements = new ArrayList<Statement>();

        if(trans !=null){

            for(Transaction transaction : trans){
            
                finalStatements.add(new Statement(generatePin(6),acc,LocalDate.now(),transaction));

            }
        }

        return finalStatements;
    }

    public String generatePin(int digits) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(digits);
    
    for (int i = 0; i < digits; i++) {
        // Append a random digit (0-9) for every slot
        sb.append(random.nextInt(10));
    }
    
    return sb.toString();
}
    
}