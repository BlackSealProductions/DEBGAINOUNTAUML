package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.User;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.*;
import Utils.AppUtils; // Use your new helper!

import javax.swing.*;

public class DashboardCon implements Controller_t {

    private DashboardScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public DashboardCon(DashboardScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;

        // 1. UPDATE DASHBOARD DATA (The "Active Object" Check)
        updateDashboardInfo();

        // 2. LOGOUT LOGIC
        view.getLogoutBtn().addActionListener(e -> {
            model.saveChangesToDB_sess();
            Session.getInstance().logout();
            view.hide();
            viewHandler.showLoginScreen(); // Use handler helper
            ViewSession.getInstance().clearHistory();
        });

        // 3. NAVIGATION LISTENERS (Using the new Getters/Buttons)

        // Row 1
        view.plhrwmhBtn.addActionListener(e -> handlePlhrwmh());
        view.kinhseisBtn.addActionListener(e -> handleKinhseis());
        view.metaforaBtn.addActionListener(e -> handleMetEktos()); // Fixed name
        view.getCreateAccountBtn().addActionListener(e -> handleCreateAcc()); // Fixed name

        // Row 2
        view.pagiesBtn.addActionListener(e -> handlePagies());
        view.diaxeirisiBtn.addActionListener(e -> handleDiaxeirisi());
        view.getDepositBtn().addActionListener(e -> handleDeposit());   // Fixed name
        view.getWithdrawBtn().addActionListener(e -> handleWithdraw()); // Fixed name
    }

    /**
     * Pushes secure data from Session -> View
     */
    private void updateDashboardInfo() {
        User user = Session.getInstance().getCurrentUser();
        Account account = Session.getInstance().getActiveAccount();

        if (user != null && account != null) {
            view.setAccountDetails(
                user.getUsername(), 
                account.getBalance(), 
                account.getAccountId()
            );
        }
    }

    // --- HANDLERS ---

    private void handlePlhrwmh(){
        view.hide();
        BillPaymentScreen next = viewHandler.getBillPaymentScreen();
        Account user = Session.getInstance().getActiveAccount();
        if (user != null) next.setBalance(user.getBalance());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleKinhseis(){
        view.hide();
        StatementsScreen next = viewHandler.getStatementsScreen();
        Account user = Session.getInstance().getActiveAccount();
        if (user != null) next.setBalance(user.getBalance());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handlePagies(){
        view.hide();
        StandingOrdersScreen next = viewHandler.getStandingOrdersScreen();
        Account user = Session.getInstance().getActiveAccount();
        if (user != null) next.setBalance(user.getBalance());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleDiaxeirisi(){
        view.hide();
        AccountManagementScreen next = viewHandler.getAccountManagementScreen();
        Account user = Session.getInstance().getActiveAccount();
        if (user != null) {
            next.setBalance(user.getBalance());
            next.setIBAN(user.getIban());
            next.setName(user.getOwnerName());
            next.setEpitokio(user.getInterestRate());
            next.setSecOwner(user.getSecondaryOwner());
        }
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleMetEktos() {
        view.hide();
        MetaforaScreen next = viewHandler.getMetaforaScreen();
        
        Account user = Session.getInstance().getActiveAccount();
        if (user != null) {
            next.setBalance(user.getBalance());
            next.setFromIban(user.getIban()); // Auto-fill Sender IBAN
        }
        
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleCreateAcc(){
        view.hide();
        AccountCreationScreen next = viewHandler.getAccountCreationScreen();
        User user = Session.getInstance().getCurrentUser(); // Use Object, not string
        if (user != null) {
            next.setHelloMessage(user.getName()); // Use getName() from object
            next.setPrimaryOwnerLabel(user.getUsername());
        }
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleDeposit(){
        view.hide();
        DepositScreen next = viewHandler.getDepositScreen();
        Account user = Session.getInstance().getActiveAccount();
        // Use the new helper to look professional
        if (user != null) {
            next.setCurrentBalanceLabel(AppUtils.formatCurrency(user.getBalance()));
        }
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleWithdraw(){
     view.hide();
     WithdrawScreen next = viewHandler.getWithdrawScreen();
     Account user = Session.getInstance().getActiveAccount();

     if(user != null) {
         // Set the current balance label when screen opens
         next.setCurrentBalanceLabel(AppUtils.formatCurrency(user.getBalance()));
     }

     next.show();
     ViewSession.getInstance().updateScreenHistory(next);
}
}