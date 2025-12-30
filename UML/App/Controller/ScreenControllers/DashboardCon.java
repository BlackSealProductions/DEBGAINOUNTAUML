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

        // PLhrwmh Logarismoy
        view.plhrwmhBtn.addActionListener(e -> handlePlhrwmh());

        // Kinhseis Logariasmou

        view.kinhseisBtn.addActionListener(e -> handleKinhseis());

        // // metafores entos
        // view.metEntosBtn.addActionListener(e -> handleMetEntos());

        // metafore ektos
        
        view.metEktosBtn.addActionListener(e -> handleMetEktos());
        
        // pagies 
        
        view.pagiesBtn.addActionListener(e -> handlePagies());
        
        // diaxeirisi logarismou
        view.diaxeirisiBtn.addActionListener(e -> handleDiaxeirisi());


    }




    private void handlePlhrwmh(){
        view.hide();
        BillPaymentScreen plhrwmh = viewHandler.getBillPaymentScreen();
        Account user = Session.getInstance().getAccount();
        plhrwmh.setBalance(user.getBalance());
        plhrwmh.show();
        ViewSession.getInstance().updateScreenHistory(plhrwmh);

    }

    private void handleKinhseis(){
        view.hide();
        StatementsScreen kinhseis = viewHandler.getStatementsScreen();
        Account user = Session.getInstance().getAccount();
        kinhseis.setBalance(user.getBalance());
        kinhseis.show();
        ViewSession.getInstance().updateScreenHistory(kinhseis);

    }

    private void handlePagies(){
        view.hide();
        StandingOrdersScreen pagies = viewHandler.getStandingOrdersScreen();
        Account user = Session.getInstance().getAccount();
        pagies.setBalance(user.getBalance());
        pagies.show();
        ViewSession.getInstance().updateScreenHistory(pagies);

    }

    private void handleDiaxeirisi(){
        view.hide();
        AccountManagementScreen actmgmt = viewHandler.getAccountManagementScreen();
        Account user = Session.getInstance().getAccount();
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
        DepositScreen metEktos = viewHandler.getDepositScreen();
        Account user = Session.getInstance().getAccount();
        metEktos.setBalance(user.getBalance());
        metEktos.show();
        ViewSession.getInstance().updateScreenHistory(metEktos);

    }

        
        
    
}
