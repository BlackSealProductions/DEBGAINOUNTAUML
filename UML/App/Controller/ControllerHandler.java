package App.Controller;

import App.View.ViewHandler;
import App.Controller.ScreenControllers.LoginCon;
import App.Controller.ScreenControllers.MainFrameCon;
import App.Model.ModelHandler;

public class ControllerHandler {

    private ViewHandler v;
    private ModelHandler m;
    private Controller_t register_con;
    private Controller_t login_con;

    public ControllerHandler(ViewHandler v, ModelHandler m){
        this.v=v;
        this.m=m;
    }


   public void initControllers() {
        // Pass 'v' as the 3rd argument
        login_con = new LoginCon(v.getLoginScreen(), m, v);
        
        login_con.init();
        register_con = new App.Controller.ScreenControllers.RegisterCon(v.getRegisterScreen(), m, v);
        register_con.init();
    }


    public void initViews(){

        v.init();
    }

    public void initStartScreen(){
        
        v.initStartScreen();
    }


    
}
