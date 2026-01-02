package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.BillPaymentScreen; 
import App.View.ViewHandler;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class PayBillCon implements Controller_t {

    private BillPaymentScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public PayBillCon(BillPaymentScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        // MATCHING YOUR STYLE: Lambda expression pointing to a private method
        view.getCompleteBtn().addActionListener(e -> handlePayment());
    }

    private void handlePayment() {
        // 1. Get Inputs
        String inputRf = view.getRFCode().trim(); 
        String amountText = view.getAmount().trim();

        if (inputRf.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            double amountToPay = Double.parseDouble(amountText);
            
            // 2. Get User Account
            Account myAccount = Session.getInstance().getActiveAccount();
            if (myAccount == null) {
                if (!Session.getInstance().getCustomerAccounts().isEmpty()) {
                    myAccount = Session.getInstance().getCustomerAccounts().get(0);
                } else {
                    JOptionPane.showMessageDialog(null, "No accounts found.");
                    return;
                }
            }

            double myBalance = Double.parseDouble(myAccount.getBalance());

            if (myBalance < amountToPay) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance!");
                return;
            }

            // 3. Database Logic
            JsonDatabase db = new JsonDatabase();
            List<Map<String, Object>> allRecords = db.getAllRecords();
            
            boolean foundTarget = false;
            boolean foundSender = false;

            // Search and Update
            for (Map<String, Object> userWrapper : allRecords) {
                Map<String, Object> user = (Map<String, Object>) userWrapper.get("user");
                List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");

                if (accounts != null) {
                    for (Map<String, String> acc : accounts) {
                        // Find Receiver
                        if (acc.containsKey("rfCode") && inputRf.equals(acc.get("rfCode"))) {
                            double targetBal = Double.parseDouble(acc.get("balance"));
                            acc.put("balance", String.valueOf(targetBal + amountToPay));
                            foundTarget = true;
                        }
                        // Find Sender
                        if (acc.get("accountId").equals(myAccount.getAccountId())) {
                            acc.put("balance", String.valueOf(myBalance - amountToPay));
                            foundSender = true;
                        }
                    }
                }
            }

            if (!foundTarget) {
                JOptionPane.showMessageDialog(null, "Invalid RF Code.");
                return;
            }

            // 4. Save Changes
            db.saveAllRecords(allRecords);

            // 5. Transaction History
            String inputTime = view.getPaymentTime().trim();
            if(inputTime.isEmpty()) inputTime = "00:00";
            String dateNow = java.time.LocalDate.now().toString();
            String transID = String.valueOf(System.currentTimeMillis());

            // Create Transaction Object
            App.Model.Entities.OperationEntities.Transaction t = new App.Model.Entities.OperationEntities.Transaction(
             transID, myAccount.getAccountId(), inputRf, amountToPay, dateNow, inputTime, "Bill Payment", "send"
            );
            
            db.saveTransaction(myAccount.getAccountId(), t);

            // 6. Update GUI
            String newBal = String.valueOf(myBalance - amountToPay);
            myAccount.setBalance(newBal);
            view.setBalance(newBal);

            JOptionPane.showMessageDialog(null, "Payment Successful!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Amount.");
        }
    }
}