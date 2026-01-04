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
    private View_t registerindiv;
    private View_t registercompany;
    private View_t statements;
    private View_t standingorders;
    private View_t accountmgmt;
    private View_t firstpage;
    private View_t dashboard;
    private View_t deposit;
    private View_t typeselection;
    private View_t acctselection;
    private View_t acctcreation;
    private View_t metafora;
    private View_t withdraw;
    
    
    
    public void init(){

        this.mainframe = new MainFrame();

        this.login = new LoginScreen();
        login.init();
        mainframe.addPanel(login.getMainPanel());

        this.registerindiv = new RegisterIndividualScreen();
        this.registerindiv.init(); 
        mainframe.addPanel(registerindiv.getMainPanel());

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

        this.metafora = new MetaforaScreen();
        this.metafora.init();
        mainframe.addPanel(metafora.getMainPanel());

        this.registercompany = new RegisterCompanyScreen();
        this.registercompany.init();
        mainframe.addPanel(registercompany.getMainPanel());

        this.typeselection = new TypeSelectionScreen();
        this.typeselection.init();
        mainframe.addPanel(typeselection.getMainPanel());

        this.acctselection = new AccountSelectionScreen();
        this.acctselection.init();
        mainframe.addPanel(acctselection.getMainPanel());

        this.acctcreation = new AccountCreationScreen();
        this.acctcreation.init();
        mainframe.addPanel(acctcreation.getMainPanel());
        
        this.deposit = new DepositScreen();
        this.deposit.init();
        mainframe.addPanel(deposit.getMainPanel());
        
        this.withdraw = new WithdrawScreen();
        this.withdraw.init();
        mainframe.addPanel(withdraw.getMainPanel());


        
    }

    
    public void initStartScreen(){
        mainframe.init();
        setupInitialScreen(firstpage);
    }
    
    
    public void showDashboard() {
        
        dashboard.show();
    }
    
    public void showRegisterScreen() {
        
        registerindiv.show();
    }
    
    public void showLoginScreen() {
        
        login.show();
        
    }
    
    
    public LoginScreen getLoginScreen() {
        return (LoginScreen) this.login; 
    }
    public RegisterIndividualScreen getRegisterScreen() {
        return (RegisterIndividualScreen) this.registerindiv;
    }
    public RegisterCompanyScreen getRegisterCompanyScreen() {
        return (RegisterCompanyScreen) this.registercompany;
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
    
    public MetaforaScreen getMetaforaScreen() {
        return (MetaforaScreen) this.metafora;
    }
    public TypeSelectionScreen getChooseRegisterType(){
        return (TypeSelectionScreen) this.typeselection;
    }
    
    public AccountSelectionScreen getAccountSelectionScreen() {
        return (AccountSelectionScreen) this.acctselection;
    }

    public AccountCreationScreen getAccountCreationScreen() {
        return (AccountCreationScreen) this.acctcreation;
    }
    public DepositScreen getDepositScreen() {
        return (DepositScreen) this.deposit;
    }

    public WithdrawScreen getWithdrawScreen() {
        return (WithdrawScreen) this.withdraw;
    }

    public MainFrame getMainframe() {
        return mainframe;
    }
    
    private void setupInitialScreen(View_t screen){
        screen.show();
        ViewSession.getInstance().setCurrentScreen(screen);
        ViewSession.getInstance().clearHistory();
    }

    public ViewSession getViewSessionInst(){
        return ViewSession.getInstance();
    }
    

    
}