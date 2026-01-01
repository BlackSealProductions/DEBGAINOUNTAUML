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

    private void handleSelection() {

        int index = view.accountList.getSelectedIndex();
        if (index != -1) {

            Account selectedAccount = Session.getInstance().getAccountByIdx(index);
            Session.getInstance().setActiveAccount(selectedAccount);
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            next.setAccountDetails(selectedAccount.getOwnerName(), selectedAccount.getBalance(), selectedAccount.getCitizenId());
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        }
    }

}