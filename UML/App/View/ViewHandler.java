package App.View;

import App.View.Screens.*;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t loginscreen;
    private View_t mainpage;
    private View_t depositscreen;
    private View_t registerscreen;


    public void init(){

        this.mainframe = new MainFrame();

        this.loginscreen = new LoginScreen();
        this.mainpage = new MainPage();
        this.depositscreen = new DepositScreen();
        this.registerscreen = new RegisterUserScreen();

        // loginscreen.init();
        // mainframe.addPanel(((LoginScreen)loginscreen).getMainPanel());

        mainpage.init();
        mainframe.addPanel(((MainPage)mainpage).getMainPanel());

        // depositscreen.init();
        // mainframe.addPanel(((DepositScreen)depositscreen).getMainPanel());
        
        // registerscreen.init();
        // mainframe.addPanel(((RegisterUserScreen)registerscreen).getMainPanel());

        

    }


    public void initStartScreen(){
        mainframe.init();
        mainpage.show();
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    public View_t getLoginscreen() {
        return loginscreen;
    }
    
}
