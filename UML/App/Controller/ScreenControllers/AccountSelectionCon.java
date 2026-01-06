package App.Controller.ScreenControllers;

import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.DashboardScreen;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Individual;

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

            String type = Session.getInstance().getActiveCustomer().getUserTypeString();
            String name;
            if(type.equals("Company")){
                name = ((Company)Session.getInstance().getActiveCustomer()).getCompanyName();
            }
            else if (type.equals("Individual")){
                name = ((Individual)Session.getInstance().getActiveCustomer()).getFirstName();
            }
            else{
                name = Session.getInstance().getActiveCustomer().getUsername();
            }
            next.setAccountDetails(name, selectedAccount.getBalance(), selectedAccount.getAccountId(), type);
            Session.getInstance().activateAccount(model, selectedAccount);
            // next.checkForCompany();
            next.show();
            
            // System.out.println("\n"+Session.getInstance().getActiveAccount().getTransactions()+"\n");
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        }
    }

}