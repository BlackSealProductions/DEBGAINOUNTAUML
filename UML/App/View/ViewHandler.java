package App.View;

import java.awt.List;

import App.View.Screens.*;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t login;
    private View_t billpayment;
    private View_t register;
    private View_t statements;
    private View_t standingorders;
    private View_t accountmgmt;

    
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


        
    }


    public void initStartScreen(){
        mainframe.init();
        accountmgmt.show();
    }

    public MainFrame getMainframe() {
        return mainframe;
    }

    public View_t getLoginscreen() {
        return login;
    }
    
}
