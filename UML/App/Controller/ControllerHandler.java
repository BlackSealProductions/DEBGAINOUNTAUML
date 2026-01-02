package App.Controller;

import App.View.ViewHandler;
import App.View.Screens.AccountManagementScreen;
import App.View.Screens.RegisterCompanyScreen;
import App.Controller.ScreenControllers.*;
import App.Model.ModelHandler;

public class ControllerHandler {

    private ViewHandler v;
    private ModelHandler m;
    private Controller_t registerindiv_con;
    private Controller_t registercomp_con;
    private Controller_t login_con;
    private Controller_t firstpage_con;
    private Controller_t mainframe_con;
    private Controller_t dashboard_con;
    private Controller_t typeselection_con;
    private Controller_t acctselection_con;
    private Controller_t acctcreation_con;
    private Controller_t deposit_con;
    private Controller_t withdraw_con;
    private Controller_t statement_con;
    private Controller_t accmgmt_con;
    
    // --- NEW: Add the PayBill Controller field ---
    private Controller_t paybill_con; 
    

    public ControllerHandler(ViewHandler v, ModelHandler m){
        this.v=v;
        this.m=m;
    }


   public void initControllers() {

        mainframe_con = new MainFrameCon(v, m);
        mainframe_con.init();
        
        login_con = new LoginCon(v.getLoginScreen(), m, v);
        login_con.init();
        
        registerindiv_con = new RegisterIndividualCon(v.getRegisterScreen(), m, v);
        registerindiv_con.init();

        registercomp_con = new RegisterCompanyCon(v.getRegisterCompanyScreen(),m ,v);
        registercomp_con.init();

        firstpage_con = new FirstPageCon(v.getFirstPageScreen(), m, v);
        firstpage_con.init();

        
        typeselection_con = new TypeSelectionCon(v.getChooseRegisterType(), m, v);
        typeselection_con.init();
        
        acctselection_con = new AccountSelectionCon(v.getAccountSelectionScreen(), m, v);
        acctselection_con.init();
        
        acctcreation_con = new AccountCreationCon(v.getAccountCreationScreen(), m, v);
        acctcreation_con.init();
        
        dashboard_con = new DashboardCon(v.getDashboardScreen(), m, v,statement_con);
        dashboard_con.init();

        deposit_con = new DepositCon(v.getDepositScreen(), m, v, dashboard_con);
        deposit_con.init();
        
        withdraw_con = new WithdrawCon(v.getWithdrawScreen(), m, v, dashboard_con);
        withdraw_con.init();
        
        statement_con = new StatementCon(v.getStatementsScreen(), m, v);
        statement_con.init();
        

        accmgmt_con = new AccountManagementCon(v.getAccountManagementScreen(), m, v);
        accmgmt_con.init();

        // --- NEW: Initialize PayBillCon ---
        // This connects your Controller to the View!
        paybill_con = new PayBillCon(v.getBillPaymentScreen(), m, v);
        paybill_con.init();

    }


    public void initViews(){

        v.init();
    }

    public void initModel(){

        m.init();
    }

    public void initStartScreen(){
        
        v.initStartScreen();
    }

    public AccountSelectionCon getAccountSelectionCon(){
        return (AccountSelectionCon) this.acctselection_con;
    }


    public Controller_t getStatement_con() {
        return statement_con;
    }

}