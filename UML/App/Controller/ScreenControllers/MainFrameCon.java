package App.Controller.ScreenControllers;

import java.awt.event.ActionListener;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.View.MainFrame;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.View_t;

public class MainFrameCon implements Controller_t{
    
    private ViewHandler view;
    private ModelHandler model;

    public MainFrameCon(ViewHandler view, ModelHandler model){

        this.view=view;
        this.model=model;
    }

    public void init(){
        for (ActionListener al : view.getMainframe().getBackBtn().getActionListeners()) {
            System.out.println("\nhere\n");
            // view.getMainframe().getBackBtn().removeActionListener(al);
        }
        view.getMainframe().getBackBtn().addActionListener(e -> goBackOne());
    }

    private void goBackOne(){
        ViewSession inst = ViewSession.getInstance();

        if(inst.isHistoryEmpty()==false){
            inst.getCurrentScreen().hide();
            inst.goBack();
            inst.getCurrentScreen().show();
        }
        else{
            
        }
    }

}
