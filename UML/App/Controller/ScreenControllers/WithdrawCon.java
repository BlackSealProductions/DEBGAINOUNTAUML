
package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
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
        

        ////
        ///// Withdraw LOGIC.../////
        ///
        /// 
        view.hide();
        DashboardScreen next = viewHandler.getDashboardScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);

    }


    
}   

