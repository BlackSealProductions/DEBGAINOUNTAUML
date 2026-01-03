package App.Controller.ScreenControllers;

import java.awt.event.ActionListener;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.View.MainFrame;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.View_t;
import App.View.Screens.FirstPageScreen;

public class MainFrameCon implements Controller_t{
    
    private ViewHandler view;
    private ModelHandler model;

    public MainFrameCon(ViewHandler view, ModelHandler model){

        this.view=view;
        this.model=model;
    }

    public void init(){
        // for (ActionListener al : view.getMainframe().getBackBtn().getActionListeners()) {
        //     System.out.println("\nhere\n");
        //     // view.getMainframe().getBackBtn().removeActionListener(al);
        // }
        view.getMainframe().getBackBtn().addActionListener(e -> goBackOne());
    }

    private void goBackOne(){

        Boolean noHistory = ViewSession.getInstance().isHistoryEmpty();
        Boolean isLoginWithNoHistory = (noHistory && ViewSession.getInstance().getCurrentScreen().equals(view.getLoginScreen()));

        if(!noHistory){
            ViewSession.getInstance().getCurrentScreen().hide();
            ViewSession.getInstance().goBack();
            ViewSession.getInstance().getCurrentScreen().show();
        }
        else if (isLoginWithNoHistory){
            FirstPageScreen firstpage = view.getFirstPageScreen();
            ViewSession.getInstance().getCurrentScreen().hide();
            firstpage.show();
            ViewSession.getInstance().updateScreenHistory(firstpage);
            ViewSession.getInstance().clearHistory();
        }
    }

}
