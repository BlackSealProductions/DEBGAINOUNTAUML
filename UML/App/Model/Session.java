package App.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import App.Model.Database.TransactionDB;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;
import App.Model.DatabaseObjectConverter; // Ensure this import matches your package structure

public class Session {
    private static Session instance;
    private String username;
    private String taxId;
    // private Map<String, Object> userData;    // Stores the full nested User Map

    
    private Account activeAccount=null;
    private Customer activeCustomer=null;
    
                                                                                                                   
    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    // Called during Login
    public void login(Map<String, Object> userData, ModelHandler model) {

        loadUser(userData);
 
    }

    public void logout() {
        this.username = null;
        this.taxId = null;
        this.activeAccount = null;
        this.activeCustomer = null;
    }

    private void loadUser(Map<String, Object> userData){

        String username = (String)userData.get("username");
        String password = (String)userData.get("password");
        String taxId = (String)userData.get("taxId");
        String type = (String)userData.get("type");
        String phone = (String)userData.get("phone");
        String email = (String)userData.get("email");

        String name = null;
        String surname = null;
        String cname = null;
        
        if("Company".equalsIgnoreCase(type)){
            cname = (String)userData.get("companyName");
        }
        else if("Individual".equalsIgnoreCase(type)){
            name = (String)userData.get("name");
            surname = (String)userData.get("surname");
        }

        Utils.GlobalConsts.userType usertype = null;
        try {
            usertype = Utils.GlobalConsts.getUserTypeByString(type);
        } catch (Exception e) {                                                                                          
            if(cname==null && (name!=null || surname!=null)){
                usertype = Utils.GlobalConsts.userType.INDIVIDUAL;
            }
            else{
                usertype = Utils.GlobalConsts.userType.BUSINESS;
            }
        }
        if("Company".equalsIgnoreCase(type)){
            this.activeCustomer = new Company(taxId, username, password, cname, email, phone, usertype);
        }
        else if("Individual".equalsIgnoreCase(type)){
            this.activeCustomer = new Individual(name, surname, null, taxId, username, password, email, phone, usertype);
        }

        List<Map<String, String>> accountsList = (List<Map<String, String>>) userData.get("accounts");
        List<Account> customerAccounts = new ArrayList<Account>();

        if (accountsList != null) {
            for (Map<String, String> acc : accountsList) {

                String acctId = acc.get("accountId");
                String iban = acc.get("iban");
                String pOwner = acc.get("ownerName");
                String sOwner = acc.get("secondaryOwner");
                String balance = acc.get("balance");
                String rate = acc.get("interestRate");

                // --- FIX: Read RF Code from Database ---
                String rf = acc.containsKey("rfCode") ? acc.get("rfCode") : "";
                // ---------------------------------------

                Account newAccount = new Account(acctId, pOwner, iban, balance, rate, sOwner);
                newAccount.setRfCode(rf); // Load it into the object
                
                customerAccounts.add(newAccount);
            }
        }
        activeCustomer.setAccounts(customerAccounts);

    }
    
    public Account getAccountByIdx(int idx){
        return activeCustomer.getAccounts().get(idx);
    }

    // --- UPDATED: Uses DatabaseObjectConverter ---
    public Map<String, Object> convertActiveUserToMap(){
        DatabaseObjectConverter converter = new DatabaseObjectConverter();
        // Delegate the work to the new class
        return converter.convertUserToMap(this.activeCustomer, this.activeCustomer.getAccounts());
    }
    // ---------------------------------------------
   
    public void loadTransactions(Map<String, Object> accData){

        List<Map<String, String>> transactionsList = (List<Map<String, String>>) accData.get("transactions");
        List<Transaction> accountTransactions = new ArrayList<Transaction>();

        // Boolean foundAcc = false;
        
        for (Map<String, String> tr : transactionsList) {

            String transactionId = tr.get("transactionId");
            String senderId = tr.get("senderId");
            String recieverId = tr.get("recieverId");
            String amount = tr.get("amount");
            String date = tr.get("date");
            String time = tr.get("time");
            String description = tr.get("description");
            String type = tr.get("type");


            accountTransactions.add(new Transaction(transactionId, senderId, recieverId, Float.parseFloat(amount), date, time, description, type));
            // foundAcc=true;
        }
        activeAccount.setTransactions(accountTransactions);
    }


    public void appendCustomerAccounts(Account account){
        if (this.activeCustomer != null) {
            this.activeCustomer.getAccounts().add(account);
        }
    }

    public void setActiveAccount(Account account) {
        this.activeAccount = account;
    }

    public void activateAccount(ModelHandler m, Account acc){
        setActiveAccount(acc);
        TransactionDB tDB = m.get_tDB();
        Map<String, Object> acctMap = tDB.findAccountWithId(activeAccount.getAccountId());
        if (acctMap!=null){
            loadTransactions(acctMap);
        }
    }

    public Account getActiveAccount() {
        return activeAccount;
    }

    public List<Account> getCustomerAccounts(){
        if (this.activeCustomer == null) return new ArrayList<>();
        return this.activeCustomer.getAccounts();
    }

    public Customer getActiveCustomer(){
        return this.activeCustomer;
    }
}