package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.TransactionDB;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Customer;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.BillPaymentScreen;
import App.View.Screens.DashboardScreen;
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
        String receiverAccId="";
        String inputRf = view.getRFCode().trim(); 
        String amountText = view.getAmount().trim();

        if (inputRf.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            Float amountToPay = Float.parseFloat(amountText);
            
            // 2. Get User Account
            Account myAccount = Session.getInstance().getActiveAccount();

            double myBalance = Double.parseDouble(myAccount.getBalance());

            if (myBalance < amountToPay) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance!");
                return;
            }

            // 3. Database Logic
            UserDB uDB = model.get_uDB();
            List<Map<String, Object>> allRecords = uDB.getAllRecords();
            
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
                            Float targetBal = Float.parseFloat(acc.get("balance"));
                            Float newBal = Float.sum(targetBal, amountToPay);
                            receiverAccId = (String)user.get("companyName");
                            acc.put("balance", String.valueOf(newBal));
                            System.out.println("old bal: "+targetBal+"...new bal: "+ newBal);
                            foundTarget = true;
                            uDB.updateUserRecord(userWrapper);

                            Account curr = Session.getInstance().getActiveAccount();
                            Float updatedBal = Float.sum(Float.parseFloat(curr.getBalance()), ((Float)amountToPay*(-1)));
                            curr.setBalance(String.valueOf(updatedBal));
                        }
                        // Find Sender
                        // if (acc.get("accountId").equals(myAccount.getAccountId())) {
                        //     acc.put("balance", String.valueOf(myBalance - amountToPay));
                        //     foundSender = true;
                        // }
                    }


                }
            }

            if (!foundTarget) {
                JOptionPane.showMessageDialog(null, "Invalid RF Code.");
                return;
            }

            // 4. Save Changes
            // uDB.saveAllRecords(allRecords);


            // 5. Transaction History
            String inputTime = view.getPaymentTime().trim();
            if(inputTime.isEmpty()) inputTime = "00:00";
            String dateNow = java.time.LocalDate.now().toString();
            String transID = String.valueOf(System.currentTimeMillis());
            // String recieverId = uDB.findAccountWithId(receiverAccId);

            // Create Transaction Object
            Transaction t = new Transaction(
            transID, myAccount.getAccountId(), receiverAccId, amountToPay, dateNow, inputTime, "Bill Payment", "send");
            
            myAccount.addTransaction(t);

            TransactionDB tDB = model.get_tDB();
            
            tDB.saveRecord(model.getConverter().convertAcctTransactionsToMap(myAccount));

            // 6. Update GUI
            String newBal = String.valueOf(myBalance - amountToPay);
            // myAccount.setBalance(newBal);
            view.setBalance(newBal);

            model.saveChangesToUDB_conv();
            view.hide();
            DashboardScreen next = viewHandler.getDashboardScreen();
            next.show();
            next.refresh(myAccount);
                        
            JOptionPane.showMessageDialog(null, "Payment Successful!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Amount.");
        }
    }
}