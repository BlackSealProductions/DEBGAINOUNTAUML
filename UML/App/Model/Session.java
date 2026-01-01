package App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;

public class Session {
    private static Session instance;

    private String username;
    private String taxId;
    private List<Account> accounts=null;
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
    public void login(Map<String, Object> userData) {
        // this.username = username;
        // this.taxId = taxId;
        // this.userData = userData;
        loadUser(userData);
    }

    public void logout() {
        this.username = null;
        this.taxId = null;
        // this.userData = null;
        this.activeAccount = null;
    }

    private void loadUser(Map<String,Object> userData){

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
        customerAccounts = new ArrayList<Account>();

        // Boolean foundAcc = false;
        
        for (Map<String, String> acc : accountsList) {

            String acctId = acc.get("accountId");
            // if (acctId.isEmpty()) continue;

            String iban = acc.get("iban");
            String pOwner = acc.get("ownerName");
            String sOwner = acc.get("secondaryOwner");
            String balance = acc.get("balance");
            String rate = acc.get("interestRate");

            customerAccounts.add(new Account(acctId, pOwner, iban, balance, rate, sOwner));
            // foundAcc=true;
        }

    }
    public Account getAccountByIdx(int idx){
        return customerAccounts.get(idx);
    }


    public Map<String, Object> convertActiveUserToMap(){

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
                    accounts.add(acc);
                }
            
     
        userData.put("accounts", accounts);
                
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user", userData);
        return wrapper;
    }


    public void appendCustomerAccounts(Account account){
        this.customerAccounts.add(account);
    }

    public void setActiveAccount(Account account) {
        this.activeAccount = account;
    }

    public Account getActiveAccount() {
        return activeAccount;
    }

    public List<Account> getCustomerAccounts(){
        return this.customerAccounts;
    }

    public Customer getActiveCustomer(){
        return this.activeCustomer;
    }
}