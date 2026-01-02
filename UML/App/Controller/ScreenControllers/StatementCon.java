package App.Controller.ScreenControllers;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
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
        acc = Session.getInstance().getActiveAccount();
        accTransactions = acc.getTransactions();
        System.out.println(accTransactions);
        accStatements = genStatements(accTransactions, acc);
        
        System.out.println(accStatements);
        view.giveAccStatements(accStatements);
    }
    //
    public List<Statement> genStatements(List<Transaction> trans, Account acc){


        List<Statement> finalStatements = new ArrayList<Statement>();

        for(Transaction transaction : trans){
        
            finalStatements.add(new Statement(generatePin(6),acc,LocalDate.now(),transaction));

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