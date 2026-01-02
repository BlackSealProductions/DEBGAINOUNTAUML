package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.View.Screens.MetaforaScreen;
import App.View.ViewHandler;
import App.Model.DatabaseObjectConverter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaforaCon implements Controller_t {

    private MetaforaScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public MetaforaCon(MetaforaScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        App.Model.Entities.UserEntities.Account myAcc = Session.getInstance().getActiveAccount();
        if (myAcc != null) {
            view.getFromAccountField().setText(myAcc.getIban());
            view.setBalance(myAcc.getBalance());
        }
        view.getConfirmButton().addActionListener(e -> handleTransfer());
    }

    private void handleTransfer() {
        String targetIban = view.getToAccountField().getText().trim();
        String amountText = view.getAmountField().getText().trim();
        boolean isInBank = view.isInBankSelected(); // Check user choice
        

        if (targetIban.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        try {
            double amountToSend = Double.parseDouble(amountText);
            App.Model.Entities.UserEntities.Account myAccount = Session.getInstance().getActiveAccount();
            if (myAccount == null) return;

            // Prevent self-transfer
            if (targetIban.equals(myAccount.getIban())) {
                JOptionPane.showMessageDialog(null, "Cannot transfer to self.");
                return;
            }

            double currentBalance = Double.parseDouble(myAccount.getBalance());
            double fee = isInBank ? 0.0 : (amountToSend * 0.02); // 2% fee for external
            double totalDeduction = amountToSend + fee;

            if (currentBalance < totalDeduction) {
                JOptionPane.showMessageDialog(null, "Insufficient Balance! (Amount + 2% Fee required: " + totalDeduction + "€)");
                return;
            }
            view.setFromAccountLabel(myAccount.getIban());

            // --- DATABASE LOGIC ---
            UserDB uDB = model.get_uDB();
            List<Map<String, Object>> userRecords = uDB.getAllRecords();

            boolean targetFoundInDb = false;
            boolean foundSender = false;
            // String myUsername = Session.getInstance().getActiveCustomer().getUsername();
            Map<String, Object> receivingUser = null;

            // 1. Scan Database to find Target IBAN
            for (Map<String, Object> userWrapper : userRecords) {
                Map<String, Object> user = (Map<String, Object>) userWrapper.get("user");
                List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");

                if (accounts != null) {
                    for (Map<String, String> acc : accounts) {
                        if (acc.get("iban").equals(targetIban)) {
                            receivingUser = user;
                            targetFoundInDb = true;
                            // If we are in "In-Bank" mode, update the receiver's balance
                            if (isInBank) {
                                Float targetBal = Float.parseFloat(acc.get("balance"));
                                acc.put("balance", String.valueOf(targetBal + amountToSend));
                                uDB.updateUserRecord(userWrapper);
                            }
                        }
                    }
                }
            }

            // 2. Validate Modes
            if (isInBank && !targetFoundInDb) {
                JOptionPane.showMessageDialog(null, "Error: This IBAN does not exist in Bank of TUC.");
                return;
            }
            if (!isInBank && targetFoundInDb) {
                JOptionPane.showMessageDialog(null, "Error: This IBAN belongs to Bank of TUC. Please select 'Bank of TUC' transfer.");
                return;
            }

            // 3. Update Sender (ME)
            // Update memory object
            myAccount.setBalance(String.valueOf(currentBalance - totalDeduction));
            model.saveChanges();
            
            // // Update Database Map
            // for (int i = 0; i < userRecords.size(); i++) {
            //     Map<String, Object> userWrapper = userRecords.get(i);
            //     Map<String, Object> user = (Map<String, Object>) userWrapper.get("user");
            //     if (user.get("username").equals(myUsername)) {
            //         // Update the converter map with new balance
            //         // (Re-convert to capture the new balance set in memory above)
            //         myUpdatedMapWrapper = converter.convertUserToMap(
            //             Session.getInstance().getActiveCustomer(), 
            //             Session.getInstance().getCustomerAccounts()
            //         );
            //         userRecords.set(i, myUpdatedMapWrapper);
            //     }
            // }

            // Save Accounts
            // model.get_uDB().saveAllRecords(userRecords);


            // 4. Save History (TransactionDB)
            // if (!targetFoundInDb){
                


            // }


                if(!targetFoundInDb && !isInBank){
                    saveTransactionHistory(myAccount,"Unknown", amountToSend, fee, isInBank);
                }
                else if(receivingUser.containsKey("companyName")){
                    saveTransactionHistory(myAccount,(String)receivingUser.get("companyName"), amountToSend, fee, isInBank);
                }else{    
                    saveTransactionHistory(myAccount, (String)receivingUser.get("name")+" "+(String)receivingUser.get("surname"), amountToSend, fee, isInBank);
                }
            
            // 5. Update UI
            view.setBalance(myAccount.getBalance());
            view.getAmountField().setText("");
            view.getToAccountField().setText("");
            String msg = isInBank ? "Transfer Successful!" : "External Transfer Successful! (Fee: " + fee + "€)";
            JOptionPane.showMessageDialog(null, msg);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Amount.");
        }
    }

    private void saveTransactionHistory(App.Model.Entities.UserEntities.Account myAccount, String targetIban, double amount, double fee, boolean isInBank) {
        List<Map<String, Object>> transRecords = model.get_tDB().getAllRecords();
        String dateNow = java.time.LocalDate.now().toString();
        String timeNow = java.time.LocalTime.now().toString().substring(0, 5);
        String transID = String.valueOf(System.currentTimeMillis());
        String desc = isInBank ? "Transfer to TUC" : "External Transfer (Fee: " + fee + ")";

        Transaction tr = new Transaction(transID, myAccount.getAccountId(), targetIban, amount, dateNow, timeNow, desc, "send");

        Session.getInstance().getActiveAccount().addTransaction(tr);
        
        model.saveChanges();
    }
}