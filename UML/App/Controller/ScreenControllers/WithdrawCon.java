
package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.DashboardScreen;
import App.View.Screens.WithdrawScreen;


public class WithdrawCon implements Controller_t {
    private WithdrawScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;
    private DashboardCon dashcon;

    public WithdrawCon(WithdrawScreen view, ModelHandler model, ViewHandler viewHandler, Controller_t dashcon) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
        this.dashcon = (DashboardCon)dashcon;
    }

    @Override
    public void init() {
        
        view.getConfirmBtn().addActionListener(e -> handleWithdraw());
        

    }

    private void handleWithdraw(){
        
        
        Account user = Session.getInstance().getActiveAccount();
        String amount = view.getAmountField().getText();
        if(Float.parseFloat(user.getBalance()) < Float.parseFloat(amount)){
            view.showInputError("Ανεπαρκές Υπόλοιπο");
            return;
        }
        else{
            user.setBalance(Float.toString(Float.parseFloat(user.getBalance())-Float.parseFloat(amount)));
    
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            dashcon.refresh(user);
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
        }

    }


    
}   

