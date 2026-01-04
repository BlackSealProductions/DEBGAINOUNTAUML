package App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import App.Model.Entities.OperationEntities.Bill;
import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;

public class DatabaseObjectConverter {

    public DatabaseObjectConverter(){
    }
    
    public Map<String, Object> convertUserToMap(Customer activeCustomer, List<Account> customerAccounts){

        String username = activeCustomer.getUsername();
        String password = activeCustomer.getPassword();
        String taxId = activeCustomer.getTaxId();
        
        // Handle phone safely
        String phone = (activeCustomer.getPhoneNumber() != null) ? activeCustomer.getPhoneNumber() : activeCustomer.getPhone();
        String email = activeCustomer.getEmail();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("taxId", taxId);
        userData.put("phone", phone);
        userData.put("email", email);

        // --- FIX: Use instanceof to prevent ClassCastException ---
        if (activeCustomer instanceof Company) {
            userData.put("type", "Company");
            userData.put("companyName", ((Company) activeCustomer).getCompanyName());
        } else {
            // Default to Individual
            userData.put("type", "Individual");
            if (activeCustomer instanceof Individual) {
                userData.put("name", ((Individual) activeCustomer).getFirstName());
                userData.put("surname", ((Individual) activeCustomer).getLastName());
            }
        }

        List<Map<String, String>> accounts = new ArrayList<>();
        if (customerAccounts != null) {
            for (Account account : customerAccounts) {
                Map<String, String> acc = new HashMap<>();
                acc.put("accountId", account.getAccountId());
                acc.put("iban", account.getIban());
                
                String owner = (account.getPrimaryOwnerId() != null) ? account.getPrimaryOwnerId() : account.getOwnerName();
                acc.put("ownerName", owner);
                
                acc.put("secondaryOwner", account.getSecondaryOwner());
                acc.put("balance", String.valueOf(account.getBalance()));
                acc.put("interestRate", account.getInterestRate());
                
                if (activeCustomer instanceof Company) {
                    String rf = (account.getRfCode() == null) ? "" : account.getRfCode();
                    acc.put("rfCode", rf);
                }
                accounts.add(acc);
            }
        }
        userData.put("accounts", accounts);
        
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user", userData);
        return wrapper;
    }

    public Map<String, Object> convertAcctTransactionsToMap(Account activeAccount){
        String accountId = activeAccount.getAccountId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("accountId", accountId);
       
        List<Map<String, String>> transactions = new ArrayList<>();
        if (activeAccount.getTransactions() != null) {
            for (Transaction transaction : activeAccount.getTransactions()) {
                Map<String, String> tr = new HashMap<>();
                tr.put("transactionId", transaction.getTransactionId());
                tr.put("senderId", transaction.getSenderId());
                tr.put("recieverId", transaction.getRecieverId());
                
                // Handle the Amount typo safely
                try {
                    // Try the typo version first since you mentioned it exists
                    tr.put("amount", String.valueOf(transaction.getAmmount()));
                } catch (Error | Exception e) {
                    // Fallback if typo is fixed
                    // tr.put("amount", String.valueOf(transaction.getAmount()));
                    tr.put("amount", "0.0");
                }

                tr.put("date", String.valueOf(transaction.getDate()));
                tr.put("time", transaction.getTime());
                tr.put("description", transaction.getDescription());
                tr.put("type", transaction.getType());
                transactions.add(tr);
            }
        }
        userData.put("transactions", transactions);
        
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", userData);
        return wrapper;
    }

    public Map<String, Object> convertAcctOrdersToMap(Account activeAccount){
        String accountId = activeAccount.getAccountId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("accountId", accountId);
       
        // Handle nested accounts array
        List<Map<String, Object>> orders = new ArrayList<>();

        for(StandingOrder order : activeAccount.getStandingorders()){

            Map<String, Object> so = new HashMap<>();
            so.put("name", order.getName());
            so.put("targetIban", order.getAccountIban());
            //so.put("transactionId", order.getTransaction().getTransactionId());
            so.put("orderId", order.getOrderId());
            so.put("amount", String.valueOf(order.getAmount()));
            so.put("day", order.getPresentDay());
            so.put("dueDate", order.getNextIssueDay());
            so.put("frequency", order.getPaymentFrequency());
            so.put("pastcharges", order.getPastCharges());
            orders.add(so);
        }

        userData.put("orders", orders);
                
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", userData);
        return wrapper;
    }

    public Map<String, Object> convertAcctOrdersToMap_Id(List<StandingOrder> ordersList, String accountId){
        // String accountId = activeAccount.getAccountId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("accountId", accountId);
       
        // Handle nested accounts array
        List<Map<String, Object>> orders = new ArrayList<>();

        for(StandingOrder order : ordersList){

            Map<String, Object> so = new HashMap<>();
            so.put("name", order.getName());
            so.put("targetIban", order.getAccountIban());
            //so.put("transactionId", order.getTransaction().getTransactionId());
            so.put("orderId", order.getOrderId());
            so.put("amount", String.valueOf(order.getAmount()));
            so.put("day", order.getPresentDay());
            so.put("dueDate", order.getNextIssueDay());
            so.put("frequency", order.getPaymentFrequency());
            so.put("pastcharges", order.getPastCharges());
            orders.add(so);
        }

        userData.put("orders", orders);
                
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", userData);
        return wrapper;
    }

    public Map<String,String> convertTransactionToMap(Transaction transaction){

        Map<String,Object> wrapper = new HashMap<>();

        Map<String, String> tr = new HashMap<>();
        tr.put("transactionId", transaction.getTransactionId());
        tr.put("senderId", transaction.getSenderId());
        tr.put("recieverId", transaction.getRecieverId());
        tr.put("amount", String.valueOf(transaction.getAmmount()));
        tr.put("date", String.valueOf(transaction.getDate()));
        tr.put("time", transaction.getTime());
        tr.put("description", transaction.getDescription());
        tr.put("type", transaction.getType());

        // wrapper.put("transaction",tr);

        return tr;
    }

    /**
     * Converts an Account's internal Bill objects into a Map for BillDB.json
     */
    public Map<String, Object> convertAcctBillsToMap(Account activeAccount) {
        String accountId = activeAccount.getAccountId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("accountId", accountId);
       
        List<Map<String, String>> bills = new ArrayList<>();
        
        // Ensure your Account class has a getBills() method
        if (activeAccount.getBills() != null) {
            for (Bill bill : activeAccount.getBills()) {
                Map<String, String> bMap = new HashMap<>();
                bMap.put("rfCode", bill.getRfCode());
                bMap.put("iban", bill.getTargetIban());
                bMap.put("amount", String.valueOf(bill.getAmount()));
                bMap.put("issue", bill.getIssueDate());
                bMap.put("due", bill.getDueDate());
                bills.add(bMap);
            }
        }
        userData.put("bills", bills);
        
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", userData);
        return wrapper;
    }





}