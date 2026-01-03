package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class Company extends Customer {


    String companyName;

    public Company(String taxId, String username, String password, String companyName, String email, String phone, GlobalConsts.userType customerType) {
        
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.taxId = taxId;
        this.customerType = customerType; 
    }

    public String getCompanyName() {
        return companyName;
    }

    
    
}
