package App.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;

public class DatabaseObjectConverter {


    public DatabaseObjectConverter(){

    }
    
    public Map<String, Object> convertUserToMap(Customer activeCustomer, List<Account> customerAccounts){

        // Customer activeCustomer = Session.getInstance().getActiveCustomer();
        // List<Account> customerAccounts = Session.getInstance().getCustomerAccounts();

        String username = activeCustomer.getUsername();
        String password = activeCustomer.getPassword();
        String type = activeCustomer.getUserTypeString();
        String taxId = activeCustomer.getTaxId();
        String phone = activeCustomer.getPhone();
        String email = activeCustomer.getEmail();


        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
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
                    acc.put("accountId", account.getCitizenId());
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


}
