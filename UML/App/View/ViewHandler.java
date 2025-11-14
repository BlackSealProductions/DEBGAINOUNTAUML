package App.View;

import App.View.Screens.*;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t loginscreen;


    public void init(){

        this.mainframe = new MainFrame();

        this.loginscreen = new LoginScreen();
        loginscreen.init();
        mainframe.addPanel(((LoginScreen)loginscreen).getMainPanel());
    }


    public void initStartScreen(){
        mainframe.init();
        loginscreen.show();
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    public View_t getLoginscreen() {
        return loginscreen;
    }
    
}
