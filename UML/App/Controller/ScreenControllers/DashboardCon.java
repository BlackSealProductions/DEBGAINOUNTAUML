package App.Controller.ScreenControllers;

import App.Controller.ControllerHandler;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardCon implements Controller_t{

    private DashboardScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;
    private StatementCon statement_con;
  
    // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public DashboardCon(DashboardScreen view, ModelHandler model, ViewHandler viewHandler, StatementCon statement_con) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
        this.statement_con = statement_con;
    }

    @Override
    public void init() {
        if (view == null)return;
        
        
        view.getLogoutBtn().addActionListener(e -> {
            model.saveChanges();
            Session.getInstance().logout();
            view.hide();
            FirstPageScreen next = viewHandler.getFirstPageScreen();
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        });

        view.getSwitchBtn().addActionListener(e -> {
            model.saveChanges();
            view.hide();
            AccountSelectionScreen next = viewHandler.getAccountSelectionScreen();
            next.populateAccounts((ArrayList<Account>)Session.getInstance().getCustomerAccounts());
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
        });

        view.getIssueBtn().addActionListener(e -> {
            Map<String, String> billData = view.issueBill();
            
            // Check if the user didn't just close the dialog
            if (billData != null && !billData.isEmpty()) {
                handleBillIssuance(billData);
            }
        });


        view.plhrwmhBtn.addActionListener(e -> handlePlhrwmh());
        view.kinhseisBtn.addActionListener(e -> handleKinhseis());
        view.createAccBtn.addActionListener(e -> handleCreateAcc());
        view.metaforesBtn.addActionListener(e -> handleMetEktos());
        view.pagiesBtn.addActionListener(e -> handlePagies());
        view.diaxeirisiBtn.addActionListener(e -> handleDiaxeirisi());
        view.depositBtn.addActionListener(e -> handleDeposit());
        view.withdrawBtn.addActionListener(e -> handleWithdraw());


    }

        

// Helper method in DashboardCon
    private void handleBillIssuance(Map<String, String> billData) {
        String targetIban = billData.get("iban");
        String amountStr = billData.get("amount");
        
        // 1. Validation
        if (targetIban.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        // 2. Find the target account ID using UserDB (since bills are filed by AccountId in BillDB)
        // We search across all users for the account that has this IBAN
        Map<String, Object> targetAccountMap = model.get_uDB().findAccountWithIban(targetIban);
        
        if (targetAccountMap == null) {
            JOptionPane.showMessageDialog(null, "Target IBAN not found in the system.");
            return;
        }

        String targetAccountId = (String) targetAccountMap.get("accountId");

        // 3. Prepare the Bill Record
        // Generate a unique RF code (Format: RF + Timestamp)
        String rfCode = "RF" + System.currentTimeMillis(); 
        String dateNow = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dueDate = java.time.LocalDate.now().plusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Map<String, String> finalBill = new HashMap<>();
        finalBill.put("rfCode", rfCode);
        finalBill.put("iban", Session.getInstance().getActiveAccount().getIban()); // The Company's IBAN
        finalBill.put("amount", amountStr);
        finalBill.put("issue", dateNow);
        finalBill.put("due", dueDate);

        // 4. Save to BillDB
        // We add the bill to the specific account record in bills.json
        model.get_bDB().addBillToWrapper(targetAccountId, finalBill);

        JOptionPane.showMessageDialog(null, "Bill Issued Successfully!\nRF Code: " + rfCode);

        
    }




    private void handlePlhrwmh(){
        view.hide();
        BillPaymentScreen plhrwmh = viewHandler.getBillPaymentScreen();
        Account user = Session.getInstance().getActiveAccount();
        plhrwmh.setBalance(user.getBalance());
        refresh(user);
        plhrwmh.show();
        ViewSession.getInstance().updateScreenHistory(plhrwmh);

    }

    private void handleKinhseis(){
        view.hide();
        StatementsScreen kinhseis = viewHandler.getStatementsScreen();
        Account user = Session.getInstance().getActiveAccount();
        kinhseis.setBalance(user.getBalance());
        statement_con.onEnter(user);
        refresh(user);
        kinhseis.show();
        ViewSession.getInstance().updateScreenHistory(kinhseis);
    }

    private void handlePagies(){
        view.hide();
        StandingOrdersScreen pagies = viewHandler.getStandingOrdersScreen();
        Account user = Session.getInstance().getActiveAccount();
        pagies.setBalance(user.getBalance());
        refresh(user);
        List<StandingOrder> orders = user.getStandingorders();
        if(orders!=null){
            pagies.clearListContainer();
            for(StandingOrder order : orders){
                String[] dataForList = {order.getName(), order.getAccountIban(), order.getPresentDay(), order.getPaymentFrequency(), String.valueOf(order.getAmount()), order.getNextIssueDay()};
                viewHandler.getStandingOrdersScreen().addListRow2(dataForList);
            }
        }
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
        refresh(user);
        actmgmt.show();
        ViewSession.getInstance().updateScreenHistory(actmgmt);

    }

    private void handleMetEktos(){
        view.hide();
        MetaforaScreen metEktos = viewHandler.getMetaforaScreen();
        Account user = Session.getInstance().getActiveAccount();
        metEktos.setBalance(user.getBalance());
        refresh(user);
        metEktos.setFromAccountLabel(user.getIban());
        metEktos.show();
        ViewSession.getInstance().updateScreenHistory(metEktos);

    }

    private void handleCreateAcc(){
        view.hide();
        AccountCreationScreen next = viewHandler.getAccountCreationScreen();
        next.setTitle1("Φτιάξτε επιπλέον");
        next.setTitle2("λογαριασμό");
        next.setHelloMessage(Session.getInstance().getActiveCustomer().getUsername());
        next.setPrimaryOwnerLabel(Session.getInstance().getActiveCustomer().getUsername());
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
        
    }

    private void handleDeposit(){
        view.hide();
        DepositScreen next = viewHandler.getDepositScreen();
        Account user = Session.getInstance().getActiveAccount();
        next.setCurrentBalance(user.getBalance());
        refresh(user);
        next.show();
        
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleWithdraw(){
        view.hide();
        WithdrawScreen next = viewHandler.getWithdrawScreen();
        Account user = Session.getInstance().getActiveAccount();
        next.setCurrentBalance(user.getBalance());
        refresh(user);
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }


    public void refresh(Account account){
        view.setAccountDetails(account.getOwnerName(), account.getBalance(), account.getAccountId(), Session.getInstance().getActiveCustomer().getUserTypeString());

    }

        
        
    
}
