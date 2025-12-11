package App.View;

import App.View.Screens.*;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t loginscreen;
    private View_t mainpage;;


    public void init(){

        this.mainframe = new MainFrame();

        this.loginscreen = new LoginScreen();
        this.mainpage = new MainPage();

        // loginscreen.init();
        // mainframe.addPanel(((LoginScreen)loginscreen).getMainPanel());

        mainpage.init();
        mainframe.addPanel(((MainPage)mainpage).getMainPanel());

        

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
