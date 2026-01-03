package App.Controller.ScreenControllers;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.TransactionDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.DashboardScreen;
import App.View.Screens.DepositScreen;



public class DepositCon implements Controller_t {
    private DepositScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;
    private DashboardCon dashcon;

    public DepositCon(DepositScreen view, ModelHandler model, ViewHandler viewHandler, Controller_t dashcon) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
        this.dashcon = (DashboardCon)dashcon;
       
    }

    @Override
    public void init() {
        
        view.getConfirmBtn().addActionListener(e -> handleDeposit());
        

    }

    private void handleDeposit() {
        try {
            // 1. Get User and Input
            Account user = Session.getInstance().getActiveAccount();
            String amountText = view.getAmountField().getText().trim();
            
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(view.getMainPanel(), "Please enter an amount.");
                return;
            }

            float depositAmount = Float.parseFloat(amountText);
            float currentBalance = Float.parseFloat(user.getBalance());

            // 2. Update Balance in RAM and User Database
            user.setBalance(Float.toString(currentBalance + depositAmount));
            model.saveChangesToUDB_conv();

            // 3. Create Transaction Record
            String transID = String.valueOf(System.currentTimeMillis());
            
            Transaction t = new Transaction(
                transID, 
                "Κατάθεση", 
                user.getAccountId(), 
                depositAmount, 
                LocalDate.now().toString(),
                LocalTime.now().toString(), 
                "Deposit", 
                "receive"
            );
            
            user.addTransaction(t);

            // 4. Update Transaction Database
            // CRITICAL FIX: Use updateUserRecord instead of saveRecord to avoid duplicates
            TransactionDB tDB = model.get_tDB();
            tDB.updateUserRecord(model.getConverter().convertAcctTransactionsToMap(user));

            // 5. Navigate to Dashboard
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            
            // Refresh dashboard with new balance
            dashcon.refresh(user);
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainPanel(), "Invalid amount entered. Please enter a number.");
        
    }


    
    }
}