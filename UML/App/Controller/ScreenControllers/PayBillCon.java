package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Entities.OperationEntities.Bill;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.BillPaymentScreen;
import App.View.Screens.DashboardScreen;
import App.View.ViewHandler;
import javax.swing.*;
import java.util.Iterator;

public class PayBillCon implements Controller_t {

    private BillPaymentScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;
    private Bill foundBill = null; 

    public PayBillCon(BillPaymentScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        view.getSearchBtn().addActionListener(e -> handleSearch());
        view.getCompleteBtn().addActionListener(e -> handlePayment());
    }

    private void handleSearch() {
        String inputRf = view.getRFCode().trim();
        Account myAccount = Session.getInstance().getActiveAccount();
        foundBill = null;

        if (myAccount.getBills() != null) {
            for (Bill b : myAccount.getBills()) {
                if (b.getRfCode().equals(inputRf)) {
                    foundBill = b;
                    break;
                }
            }
        }

        if (foundBill != null) {
            view.setAmountField(String.format("%.2f", foundBill.getAmount()));
            JOptionPane.showMessageDialog(null, "Ο λογαριασμός βρέθηκε!");
        } else {
            view.setAmountField("");
            JOptionPane.showMessageDialog(null, "Δεν βρέθηκε λογαριασμός με αυτόν τον κωδικό RF.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePayment() {
        if (foundBill == null) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ αναζητήστε έναν έγκυρο λογαριασμό πρώτα.");
            return;
        }

        Account myAccount = Session.getInstance().getActiveAccount();
        double amountToPay = foundBill.getAmount();
        double myBalance = Double.parseDouble(myAccount.getBalance());

        if (myBalance < amountToPay) {
            JOptionPane.showMessageDialog(null, "Ανεπαρκές υπόλοιπο!");
            return;
        }

        // 1. Update Objects
        double newBal = myBalance - amountToPay;
        myAccount.setBalance(String.valueOf(newBal));

        // 2. Remove bill from list
        if (myAccount.getBills() != null) {
            Iterator<Bill> it = myAccount.getBills().iterator();
            while (it.hasNext()) {
                if (it.next().getRfCode().equals(foundBill.getRfCode())) {
                    it.remove();
                    break;
                }
            }
        }

        // 3. Create Transaction
        Transaction t = new Transaction(
            String.valueOf(System.currentTimeMillis()), 
            myAccount.getAccountId(), 
            foundBill.getTargetIban(), 
            (float)amountToPay, 
            java.time.LocalDate.now().toString(), 
            java.time.LocalTime.now().toString().substring(0, 5), 
            "Πληρωμή RF: " + foundBill.getRfCode(), 
            "send"
        );
        myAccount.addTransaction(t);

        // 4. Persistence
        model.saveChanges(); // Balance & Transactions
        // model.saveBills();   // Critical: Syncs bills.json

        // 5. UI Update
        view.setBalance(String.valueOf(newBal));
        view.clearFields();
        // view.hide();
        // DashboardScreen dash = viewHandler.getDashboardScreen();
        // dash.refresh(myAccount);
        // dash.show();
        
        JOptionPane.showMessageDialog(null, "Η πληρωμή ολοκληρώθηκε επιτυχώς!");
        foundBill = null; 
    }
}