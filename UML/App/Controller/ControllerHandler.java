package App.Controller;

import App.View.ViewHandler;
import App.Controller.ScreenControllers.*;
import App.Model.ModelHandler;

public class ControllerHandler {

    private ViewHandler v;
    private ModelHandler m;
    private Controller_t register_con;
    private Controller_t login_con;
    private Controller_t firstpage_con;
    private Controller_t mainframe_con;
    private Controller_t dashboard_con;
    

    public ControllerHandler(ViewHandler v, ModelHandler m){
        this.v=v;
        this.m=m;
    }


   public void initControllers() {

        mainframe_con = new MainFrameCon(v, m);
        mainframe_con.init();
        
        login_con = new LoginCon(v.getLoginScreen(), m, v);
        login_con.init();
        
        register_con = new RegisterCon(v.getRegisterScreen(), m, v);
        register_con.init();

        firstpage_con = new FirstPageCon(v.getFirstPageScreen(), m, v);
        firstpage_con.init();

        dashboard_con = new DashboardCon(v.getDashboardScreen(), m, v);
        dashboard_con.init();

        




    }


    public void initViews(){

        v.init();
    }

    public void initStartScreen(){
        
        v.initStartScreen();
    }


    
}
