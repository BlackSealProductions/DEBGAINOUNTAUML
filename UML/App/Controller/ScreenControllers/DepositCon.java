package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
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

    private void handleDeposit(){
        
        Account user = Session.getInstance().getActiveAccount();
        String amount = view.getAmountField().getText();
        user.setBalance(Float.toString(Float.parseFloat(user.getBalance())+Float.parseFloat(amount)));

        // newDb = 
        view.hide();
        DashboardScreen next = viewHandler.getDashboardScreen();
        dashcon.refresh(user);
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);

    }


    
}