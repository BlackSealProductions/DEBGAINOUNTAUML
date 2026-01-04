package App.Controller.ScreenControllers;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
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
    
    
    
    
    public void onEnter(Account acc){
        List<Map<String, Object>> accTrans = new ArrayList();
        acc = Session.getInstance().getActiveAccount();
        accTransactions = acc.getTransactions();
        System.out.println(accTransactions);
        accStatements = genStatements(accTransactions, acc);
        accTrans = genStatements2(accTransactions, acc);
        
        System.out.println(accStatements);
        view.giveAccStatements(accStatements);
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

                if(senderData==null){
                    transactorName=transaction.getSenderId();
                }
                if(recieverData==null){
                    transactorName=transaction.getRecieverId();
                }

                if(transaction.getType().equals("send") && senderData!=null){
                    if(senderData.containsKey("name")){
                        transactorName = (String)senderData.get("name")+" "+(String)senderData.get("surname");
                    }
                    else if(senderData.containsKey("companyName")){
                        transactorName = (String)senderData.get("companyName");
                    }
                    else{
                        transactorName = (String)senderData.get("username");
                    }
                }
                else if(transaction.getType().equals("receive") && recieverData!=null){
                    if(recieverData.containsKey("name")){
                        transactorName = (String)recieverData.get("name")+" "+(String)recieverData.get("surname");
                    }
                    else if(recieverData.containsKey("companyName")){
                        transactorName = (String)recieverData.get("companyName");
                    }        
                    else{
                        transactorName = (String)recieverData.get("username");
                    }        
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