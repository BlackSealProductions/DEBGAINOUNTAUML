package App.View;

import App.View.Screens.*;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t login;           // <--- Variable is named "login"
    private View_t billpayment;
    private View_t register;
    private View_t statements;
    private View_t standingorders;
    private View_t accountmgmt;
    private View_t mainpage;

    
    public void init(){

        this.mainframe = new MainFrame();

        this.login = new LoginScreen();
        login.init();
        mainframe.addPanel(((LoginScreen)login).getMainPanel());

        this.register = new RegisterScreen();
        this.register.init(); 
        mainframe.addPanel(((RegisterScreen)register).getMainPanel());

        this.billpayment = new BillPaymentScreen();
        this.billpayment.init(); 
        mainframe.addPanel(((BillPaymentScreen)billpayment).getMainPanel());

        this.statements = new StatementsScreen();
        this.statements.init(); 
        mainframe.addPanel(((StatementsScreen)statements).getMainPanel());

        this.standingorders = new StandingOrdersScreen();
        this.standingorders.init(); 
        mainframe.addPanel(((StandingOrdersScreen)standingorders).getMainPanel());

        this.accountmgmt = new AccountManagementScreen();
        this.accountmgmt.init();
        mainframe.addPanel(((AccountManagementScreen)accountmgmt).getMainPanel());
        
        this.mainpage = new MainPage();
        this.mainpage.init();
        mainframe.addPanel(((MainPage)mainpage).getMainPanel());

        

    }


    public void initStartScreen(){
        mainframe.init();
        // You probably want to show login first, not accountmgmt?
        // accountmgmt.show(); 
        login.show(); 
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    // This returns it as a generic View_t (Optional, you can keep or remove)
    public View_t getLoginscreen() {
        return login;
    }
    
    // FIX IS HERE: Cast 'login' to 'LoginScreen'
    public App.View.Screens.LoginScreen getLoginScreen() {
        return (App.View.Screens.LoginScreen) this.login; 
    }
    
    // Add this too so we can switch screens later!
    public void showDashboard() {
        if (login != null) login.hide();
        // Show whichever screen is your main dashboard
        if (statements != null) accountmgmt.show(); 
    }

    public void showRegisterScreen() {
        // 1. Hide Login
        if (login != null) ((App.View.View_t) login).hide();
        
        // 2. Show Register
        if (register != null) {
            ((App.View.View_t) register).show();
        } else {
            System.out.println("Error: Register Screen is null.");
        }
    }

    public App.View.Screens.RegisterScreen getRegisterScreen() {
        return (App.View.Screens.RegisterScreen) this.register;
        }
    
    public void showLoginScreen() {
        if (register != null) ((App.View.View_t) register).hide();
        if (login != null) ((App.View.View_t) login).show();
    }

}