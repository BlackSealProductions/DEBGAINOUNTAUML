package App.Controller;

import App.View.ViewHandler;
import App.Controller.ScreenControllers.LoginCon;
import App.Controller.ScreenControllers.MainFrameCon;
import App.Model.ModelHandler;

public class ControllerHandler {

    private ViewHandler v;
    private ModelHandler m;

    private Controller_t login_con;

    public ControllerHandler(ViewHandler v, ModelHandler m){
        this.v=v;
        this.m=m;
    }


    public void initControllers(){

        // mainframe_con = new MainFrameCon(v.getMainframe(), m);
        login_con = new LoginCon(v.getLoginscreen(), m);

    }


    public void initViews(){

        v.init();
    }

    public void initStartScreen(){
        
        v.initStartScreen();
    }


    
}
