package App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String type = activeCustomer.getUserTypeString();
        String taxId = activeCustomer.getTaxId();
        String phone = activeCustomer.getPhone();
        String email = activeCustomer.getEmail();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("type", type);
        
        if("Company".equalsIgnoreCase(type)){
            String cname = ((Company)activeCustomer).getCompanyName();
            userData.put("companyName", cname);
        }
        else{
            String name = ((Individual)activeCustomer).getFirstName();
            String surname = ((Individual)activeCustomer).getLastName();
            userData.put("name", name);
            userData.put("surname", surname);
        }
        userData.put("taxId", taxId);
        userData.put("phone", phone);
        userData.put("email", email);

        // Handle nested accounts array
        List<Map<String, String>> accounts = new ArrayList<>();
    
        for (Account account : customerAccounts) {
            
            Map<String, String> acc = new HashMap<>();
            acc.put("accountId", account.getAccountId());
            acc.put("iban", account.getIban());
            acc.put("ownerName", account.getOwnerName());
            acc.put("secondaryOwner", account.getSecondaryOwner());
            acc.put("balance", account.getBalance());
            acc.put("interestRate", account.getInterestRate());
            
            // --- CRITICAL FIX: ADD THIS LINE ---
            // Without this, your RF codes will be deleted when you save!
            if("Company".equalsIgnoreCase(type)){

                String rf = (account.getRfCode() == null) ? "" : account.getRfCode();
                acc.put("rfCode", rf);
            }
            // -----------------------------------

            accounts.add(acc);
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
       
        // Handle nested accounts array
        List<Map<String, String>> transactions = new ArrayList<>();
    
                for (Transaction transaction : activeAccount.getTransactions()) {
                    
                    Map<String, String> tr = new HashMap<>();
                    tr.put("transactionId", transaction.getTransactionId());
                    tr.put("senderId", transaction.getSenderId());
                    tr.put("recieverId", transaction.getRecieverId());
                    tr.put("amount", String.valueOf(transaction.getAmmount()));
                    tr.put("date", String.valueOf(transaction.getDate()));
                    tr.put("time", transaction.getTime());
                    tr.put("description", transaction.getDescription());
                    tr.put("type", transaction.getType());
                    transactions.add(tr);
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
        List<Map<String, String>> orders = new ArrayList<>();

        for(StandingOrder order : activeAccount.getStandingorders()){

            Map<String, String> so = new HashMap<>();
            so.put("name", order.getName());
            so.put("targetIban", order.getAccountIban());
            //so.put("transactionId", order.getTransaction().getTransactionId());
            so.put("orderId", order.getOrderId());
            so.put("amount", String.valueOf(order.getAmount()));
            so.put("day", order.getPresentDay());
            so.put("dueDate", order.getNextIssueDay());
            so.put("frequency", order.getPaymentFrequency());
            orders.add(so);
        }

        userData.put("orders", orders);
                
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", userData);
        return wrapper;
    }



}