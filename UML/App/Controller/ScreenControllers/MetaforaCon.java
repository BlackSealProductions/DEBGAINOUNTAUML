package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.User;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.MetaforaScreen;
import App.View.ViewHandler;
import Utils.AppUtils;
import Utils.ValidationUtils;

import javax.swing.*;

public class MetaforaCon implements Controller_t {

    private MetaforaScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public MetaforaCon(MetaforaScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;

        // Listener for the "EXECUTE" (Εκτέλεση) button
        view.getConfirmBtn().addActionListener(e -> handleTransfer());
        
        // Note: The "Back" button logic is handled by the Universal Button (MainFrame).
    }

    private void handleTransfer() {
        User activeUser = Session.getInstance().getCurrentUser();
        Account activeAccount = Session.getInstance().getActiveAccount();

        String targetIban = view.getToIban();
        String amountStr = view.getAmount();

        // 1. Validations
        if (!ValidationUtils.isValidGreekIBAN(targetIban)) {
            JOptionPane.showMessageDialog(null, "Invalid Target IBAN");
            return;
        }
        
        if (!ValidationUtils.isValidTransactionAmount(amountStr)) {
            JOptionPane.showMessageDialog(null, "Invalid Amount");
            return;
        }

        if (!ValidationUtils.hasSufficientBalance(activeAccount.getBalance(), amountStr)) {
            AppUtils.showError("Insufficient Funds!");
            return;
        }

        // 2. Logic
        double amount = Double.parseDouble(amountStr);
        activeAccount.deposit(-amount); // Deduct from sender

        // 3. Save
        JsonDatabase.updateUser(activeUser);
        
        // 4. Success UI
        AppUtils.showSuccess("Transfer of " + AppUtils.formatCurrency(amount) + " sent!");
        view.clearFields();
        view.hide();
        
        // Return to Dashboard
        var dash = viewHandler.getDashboardScreen();
        dash.setAccountDetails(activeUser.getUsername(), activeAccount.getBalance(), activeAccount.getAccountId());
        dash.show();
    }
}