package App.View;

import java.awt.*;
import java.beans.Statement;

import javax.swing.JButton;
import javax.swing.JPanel;

import App.Model.Entities.OperationEntities.Bill;
import App.View.Screens.*;
import App.View.helper_classes.RoundedButton;

public class ViewHandler {
    
    private MainFrame mainframe;
    private View_t login;
    private View_t billpayment;
    private View_t register;
    private View_t statements;
    private View_t standingorders;
    private View_t accountmgmt;
    private View_t firstpage;
    private View_t dashboard;
    private View_t deposit;
    

    JPanel wh = new JPanel();
    
    public void init(){

        this.mainframe = new MainFrame();

        this.login = new LoginScreen();
        login.init();
        mainframe.addPanel(login.getMainPanel());

        this.register = new RegisterScreen();
        this.register.init(); 
        mainframe.addPanel(register.getMainPanel());

        this.billpayment = new BillPaymentScreen();
        this.billpayment.init(); 
        mainframe.addPanel(billpayment.getMainPanel());

        this.statements = new StatementsScreen();
        this.statements.init(); 
        mainframe.addPanel(statements.getMainPanel());

        this.standingorders = new StandingOrdersScreen();
        this.standingorders.init(); 
        mainframe.addPanel(standingorders.getMainPanel());

        this.accountmgmt = new AccountManagementScreen();
        this.accountmgmt.init();
        mainframe.addPanel(accountmgmt.getMainPanel());
        
        this.firstpage = new FirstPageScreen();
        this.firstpage.init();
        mainframe.addPanel(firstpage.getMainPanel());
        
        
        this.dashboard = new DashboardScreen();
        this.dashboard.init();
        mainframe.addPanel(dashboard.getMainPanel());

        this.deposit = new DepositScreen();
        this.deposit.init();
        mainframe.addPanel(((DepositScreen)deposit).getMainPanel());
        
        // registerscreen.init();
        // mainframe.addPanel(((RegisterUserScreen)registerscreen).getMainPanel());

        
    }


    
    
    public void initStartScreen(){
        mainframe.init();
        firstpage.show(); 
        ViewSession.getInstance().setCurrentScreen(firstpage);

    }

    
    public void showDashboard() {
        
        dashboard.show();
    }
    
    public void showRegisterScreen() {

        register.show();
    }
    
    public void showLoginScreen() {

        login.show();

    }
    
    
    public LoginScreen getLoginScreen() {
        return (LoginScreen) this.login; 
    }
    public RegisterScreen getRegisterScreen() {
        return (RegisterScreen) this.register;
    }
    public FirstPageScreen getFirstPageScreen() {
        return (FirstPageScreen) this.firstpage;
    }
    public DashboardScreen getDashboardScreen() {
        return (DashboardScreen) this.dashboard;
    }
    public BillPaymentScreen getBillPaymentScreen() {
        return (BillPaymentScreen) this.billpayment;
    }

    public StatementsScreen getStatementsScreen() {
        return (StatementsScreen) this.statements;
    }

    public StandingOrdersScreen getStandingOrdersScreen() {
        return (StandingOrdersScreen) this.standingorders;
    }
    public AccountManagementScreen getAccountManagementScreen() {
        return (AccountManagementScreen) this.accountmgmt;
    }

    public DepositScreen getDepositScreen() {
        return (DepositScreen) this.deposit;
    }


    public MainFrame getMainframe() {
        return mainframe;
    }
    
}