package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.User;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.DashboardScreen;
import App.View.Screens.WithdrawScreen;
import App.View.ViewHandler;
import App.View.ViewSession;
import Utils.AppUtils;
import Utils.ValidationUtils;

import javax.swing.*;

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
        if (view == null) return;

        // 1. Calculate Preview (Subtraction)
        view.getCalculateBtn().addActionListener(e -> handleCalculate());

        // 2. Confirm Withdrawal
        view.getConfirmBtn().addActionListener(e -> handleWithdraw());
        
        // 3. Back Button
        view.getBackBtn().addActionListener(e -> {
            view.clearInput();
            view.hide();
            viewHandler.getDashboardScreen().show();
        });
    }

    private void handleCalculate() {
        String input = view.getAmountInput();
        Account activeAccount = Session.getInstance().getActiveAccount();

        if (!ValidationUtils.isValidTransactionAmount(input)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid positive number.");
            return;
        }

        double current = Double.parseDouble(activeAccount.getBalance());
        double withdrawAmount = Double.parseDouble(input);

        // Check if result would be negative
        if (withdrawAmount > current) {
            view.setNewBalanceLabel("Insufficient Funds!");
            return;
        }

        double predicted = current - withdrawAmount;
        view.setNewBalanceLabel(AppUtils.formatCurrency(predicted));
    }

    private void handleWithdraw() {
        // 1. Get Objects
        User activeUser = Session.getInstance().getCurrentUser();
        Account activeAccount = Session.getInstance().getActiveAccount();

        // 2. Validate
        String input = view.getAmountInput();
        if (!ValidationUtils.isValidTransactionAmount(input)) {
            JOptionPane.showMessageDialog(null, "Invalid Amount!");
            return;
        }

        // Check actual funds
        if (!ValidationUtils.hasSufficientBalance(activeAccount.getBalance(), input)) {
            AppUtils.showError("Transaction Failed: No money broski");
            return;
        }

        // 3. EXECUTE (Subtract Money)
        double amount = Double.parseDouble(input);
        // We use deposit with negative amount to subtract
        activeAccount.deposit(-amount); 

        // 4. SAVE (Database)
        JsonDatabase.updateUser(activeUser);

        // 5. SUCCESS
        AppUtils.showSuccess("Withdrawal Successful!\nNew Balance: " + activeAccount.getBalance());
        
        view.clearInput();
        view.hide();
        
        // Refresh Dashboard
        DashboardScreen dashboard = viewHandler.getDashboardScreen();
        dashboard.setAccountDetails(
            activeUser.getUsername(), 
            activeAccount.getBalance(), 
            activeAccount.getAccountId()
        );
        dashboard.show();
        ViewSession.getInstance().updateScreenHistory(dashboard);
    }
}