package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class DashboardCon implements Controller_t{

    private DashboardScreen view;
    private ModelHandler model; 
    
    private ViewHandler viewHandler;

    // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public DashboardCon(DashboardScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
    }

    @Override
    public void init() {
        if (view == null)return;

        view.getLogoutBtn().addActionListener(e -> {
            Session.getInstance().logout();
            view.hide();
            FirstPageScreen next = viewHandler.getFirstPageScreen();
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        });

        // PLhrwmh Logarismoy
        view.plhrwmhBtn.addActionListener(e -> handlePlhrwmh());

        // Kinhseis Logariasmou

        view.kinhseisBtn.addActionListener(e -> handleKinhseis());

        // anoigma neou logarismou
        view.createAccBtn.addActionListener(e -> handleCreateAcc());

        // metafore ektos
        
        view.metaforesBtn.addActionListener(e -> handleMetEktos());
        
        // pagies 
        
        view.pagiesBtn.addActionListener(e -> handlePagies());
        
        // diaxeirisi logarismou
        view.diaxeirisiBtn.addActionListener(e -> handleDiaxeirisi());

        // deposit

        view.depositBtn.addActionListener(e -> handleDeposit());

        // withdraw

         view.withdrawBtn.addActionListener(e -> handleWithdraw());


    }




    private void handlePlhrwmh(){
        view.hide();
        BillPaymentScreen plhrwmh = viewHandler.getBillPaymentScreen();
        Account user = Session.getInstance().getActiveAccount();
        plhrwmh.setBalance(user.getBalance());
        plhrwmh.show();
        ViewSession.getInstance().updateScreenHistory(plhrwmh);

    }

    private void handleKinhseis(){
        view.hide();
        StatementsScreen kinhseis = viewHandler.getStatementsScreen();
        Account user = Session.getInstance().getActiveAccount();
        kinhseis.setBalance(user.getBalance());
        kinhseis.show();
        ViewSession.getInstance().updateScreenHistory(kinhseis);

    }

    private void handlePagies(){
        view.hide();
        StandingOrdersScreen pagies = viewHandler.getStandingOrdersScreen();
        Account user = Session.getInstance().getActiveAccount();
        pagies.setBalance(user.getBalance());
        pagies.show();
        ViewSession.getInstance().updateScreenHistory(pagies);

    }

    private void handleDiaxeirisi(){
        view.hide();
        AccountManagementScreen actmgmt = viewHandler.getAccountManagementScreen();
        Account user = Session.getInstance().getActiveAccount();
        actmgmt.setBalance(user.getBalance());
        actmgmt.setIBAN(user.getIban());
        actmgmt.setName(user.getOwnerName());
        actmgmt.setEpitokio(user.getInterestRate());
        actmgmt.setSecOwner(user.getSecondaryOwner());
        actmgmt.show();
        ViewSession.getInstance().updateScreenHistory(actmgmt);

    }

    private void handleMetEktos(){
        view.hide();
        MetaforaScreen metEktos = viewHandler.getMetaforaScreen();
        Account user = Session.getInstance().getActiveAccount();
        metEktos.setBalance(user.getBalance());
        metEktos.show();
        ViewSession.getInstance().updateScreenHistory(metEktos);

    }

    private void handleCreateAcc(){
        view.hide();
        AccountCreationScreen next = viewHandler.getAccountCreationScreen();
        next.setHelloMessage(Session.getInstance().getUsername());
        next.setPrimaryOwnerLabel(Session.getInstance().getUsername());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
        
    }

    private void handleDeposit(){
        view.hide();
        DepositScreen next = viewHandler.getDepositScreen();
        Account user = Session.getInstance().getActiveAccount();
        next.setCurrentBalance(user.getBalance());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleWithdraw(){
        view.hide();
        WithdrawScreen next = viewHandler.getWithdrawScreen();
        Account user = Session.getInstance().getActiveAccount();
        next.setCurrentBalance(user.getBalance());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

        
        
    
}
