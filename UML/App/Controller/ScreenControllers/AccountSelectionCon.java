package App.Controller.ScreenControllers;

import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.DashboardScreen;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.User;     // Import User Object
import App.Model.Entities.UserEntities.Account;  // Import Account Object

import javax.swing.*;
import java.util.List;

public class AccountSelectionCon implements Controller_t {
    private AccountSelectionScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public AccountSelectionCon(AccountSelectionScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;
        
        // Setup the listener for the big red button
        view.selectBtn.addActionListener(e -> handleSelection());
    }

    private void handleSelection() {
        // 1. Get the selected index from the list on screen
        int index = view.accountList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(null, "Please select an account first.");
            return;
        }

        // 2. Get the Active User Object from Session (The New Way)
        User currentUser = Session.getInstance().getCurrentUser();
        
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Error: No user logged in.");
            return;
        }

        // 3. Get their list of Account Objects
        List<Account> accounts = currentUser.getAccounts();

        if (index < accounts.size()) {
            // 4. Pick the specific Account Object based on the list index
            Account selectedAccount = accounts.get(index);

            // 5. Store this OBJECT in the Session as the "Active Account"
            Session.getInstance().setActiveAccount(selectedAccount);

            // 6. Navigate to Dashboard
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            
            // Pass the data cleanly to the Dashboard
            next.setAccountDetails(
                selectedAccount.getOwnerName(), 
                selectedAccount.getBalance(), 
                selectedAccount.getAccountId()
            );
            
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory(); // Clear back stack so you can't go back to selection
        } else {
            JOptionPane.showMessageDialog(null, "Error: Account selection mismatch.");
        }
    }
}