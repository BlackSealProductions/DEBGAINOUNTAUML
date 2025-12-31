package App.Controller.ScreenControllers;

import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.DashboardScreen;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;

import java.util.*;

public class AccountSelectionCon implements Controller_t{
    private AccountSelectionScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public AccountSelectionCon(AccountSelectionScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model=model;
        this.viewHandler=viewHandler;
    }

    public void init() {

        this.view.selectBtn.addActionListener(e -> handleSelection());

    }

    // public void getUserAccounts(){

    //     view.listModel.clear();
    //     // Get user data directly from the Session
    //     Map<String, Object> userData = (Map<String, Object>) Session.getInstance().getUserData();
    //     List<Map<String, String>> accounts = (List<Map<String, String>>) userData.get("accounts");

    //     if (accounts != null) {
    //         for (Map<String, String> acc : accounts) {
    //             view.listModel.addElement(acc.get("accountId") + " - " + acc.get("iban") + " (Balance: " + acc.get("balance") + ")");
    //         }
    //     }
    // }

    private void handleSelection() {
        int index = view.accountList.getSelectedIndex();
        if (index != -1) {
            Map<String, Object> userData = (Map<String, Object>) Session.getInstance().getUserData();
            List<Map<String, String>> accounts = (List<Map<String, String>>) userData.get("accounts");
            Map<String, String> rawAccount = accounts.get(index);
            
            // 2. Instantiate the formal Account Object
            Account selectedAccount = new Account(
                rawAccount.get("accountId"),
                rawAccount.get("ownerName"),
                rawAccount.get("iban"),
                rawAccount.get("balance"),
                rawAccount.get("interestRate"),
                rawAccount.get("secondaryOwner")
            );
        
            // 3. Store the Object in the Session
            Session.getInstance().setActiveAccount(selectedAccount);
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            next.setAccountDetails(selectedAccount.getOwnerName(), selectedAccount.getBalance(), selectedAccount.getCitizenId());
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        }
    }

    public void show() { view.show(); }
}