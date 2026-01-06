
package App.Controller.ScreenControllers;

import java.time.LocalDate;
import java.time.LocalTime;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.TransactionDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.DashboardScreen;
import App.View.Screens.WithdrawScreen;


public class WithdrawCon implements Controller_t {
    private WithdrawScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;

    public WithdrawCon(WithdrawScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        
        view.getConfirmBtn().addActionListener(e -> handleWithdraw());
        

    }

    private void handleWithdraw(){
        try {
            Account user = Session.getInstance().getActiveAccount();
            String amountText = view.getAmountField().getText().trim();
            
            // 1. Validate Input
            if (amountText.isEmpty()) {
                view.showInputError("Please enter an amount");
                return;
            }

            float amount = Float.parseFloat(amountText);
            float currentBalance = Float.parseFloat(user.getBalance());

            // 2. Check Sufficient Funds
            if (currentBalance < amount) {
                view.showInputError("Ανεπαρκές Υπόλοιπο"); // "Insufficient Balance"
                return;
            }

            // 3. Update Balance
            user.setBalance(Float.toString(currentBalance - amount));
            model.saveChangesToUDB_conv();

            // 4. Create Transaction Record
            String transID = String.valueOf(System.currentTimeMillis());
            
            Transaction t = new Transaction(
                transID, 
                user.getAccountId(),     // Sender: User
                "Ανάλυψη",            // Receiver: Cash/Wallet
                amount, 
                LocalDate.now().toString(),
                LocalTime.now().toString(), 
                "Withdraw", 
                "Withdrawal"
            );
            
            user.addTransaction(t);

            // 5. Update Transaction Database 
            // Using updateUserRecord to prevent duplicate accounts in JSON
            TransactionDB tDB = model.get_tDB();
            tDB.updateUserRecord(model.getConverter().convertAcctTransactionsToMap(user));

            // 6. Navigate
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            next.refresh(user);
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);

        } catch (NumberFormatException e) {
            view.showInputError("Invalid Amount");
        }
    }

    
}   

