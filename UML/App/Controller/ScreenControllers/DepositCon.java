package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.User;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.DashboardScreen;
import App.View.Screens.DepositScreen;
import App.View.ViewHandler;
import App.View.ViewSession;
import Utils.AppUtils;
import Utils.ValidationUtils;

import javax.swing.*;

public class DepositCon implements Controller_t {

    private DepositScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public DepositCon(DepositScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;

        // 1. Show the CURRENT balance immediately when screen loads
        updateCurrentBalanceDisplay();

        // 2. Listener: Calculate Preview ("Υπολογισμός")
        view.getCalculateBtn().addActionListener(e -> handleCalculate());

        // 3. Listener: Confirm Deposit ("Επιβεβαίωση")
        view.getConfirmBtn().addActionListener(e -> handleDeposit());

        // 4. Listener: Back Button ("<-")
        view.getBackBtn().addActionListener(e -> {
            view.clearInput();
            view.hide();
            DashboardScreen dashboard = viewHandler.getDashboardScreen();
            dashboard.show();
            ViewSession.getInstance().updateScreenHistory(dashboard);
        });
    }

    /**
     * Reads the Active Account from Session and updates the "Current Balance" label
     */
    private void updateCurrentBalanceDisplay() {
        Account activeAccount = Session.getInstance().getActiveAccount();
        if (activeAccount != null) {
            // Format: "1,500.50 €"
            String formatted = AppUtils.formatCurrency(activeAccount.getBalance());
            view.setCurrentBalanceLabel(formatted);
        }
    }

    /**
     * Logic for the "Υπολογισμός" button (Preview Only)
     */
    private void handleCalculate() {
        String input = view.getAmountInput();
        Account activeAccount = Session.getInstance().getActiveAccount();

        // VALIDATION
        if (!ValidationUtils.isValidTransactionAmount(input)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid positive number.");
            return;
        }

        // MATH (Preview)
        double current = Double.parseDouble(activeAccount.getBalance());
        double deposit = Double.parseDouble(input);
        double predicted = current + deposit;

        // UPDATE VIEW
        view.setNewBalanceLabel(AppUtils.formatCurrency(predicted));
    }

    /**
     * Logic for "Επιβεβαίωση" (Save to Database)
     */
    private void handleDeposit() {
        // 1. GET ACTIVE OBJECTS
        User activeUser = Session.getInstance().getCurrentUser();
        Account activeAccount = Session.getInstance().getActiveAccount();

        if (activeUser == null || activeAccount == null) {
            JOptionPane.showMessageDialog(null, "Error: No active session.");
            return;
        }

        // 2. VALIDATE INPUT
        String input = view.getAmountInput();
        if (!ValidationUtils.isValidTransactionAmount(input)) {
            JOptionPane.showMessageDialog(null, "Invalid Amount!");
            return;
        }

        // 3. UPDATE THE OBJECT (Memory)
        double amount = Double.parseDouble(input);
        activeAccount.deposit(amount); 

        // 4. UPDATE THE DATABASE (JSON)
        // We update the User, which contains the modified Account
        JsonDatabase.updateUser(activeUser);

        // 5. SUCCESS & NAVIGATION
        AppUtils.showSuccess("Deposit Successful!\nNew Balance: " + activeAccount.getBalance());
        
        view.clearInput();
        view.hide();
        
        // Refresh Dashboard Data before showing it
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