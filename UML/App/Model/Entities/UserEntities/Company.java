package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class Company extends Customer {

    String companyName;

    // Full Constructor
    public Company(String taxId, String username, String password, String companyName, String email, String phone, GlobalConsts.userType customerType) {
        this.username = username;
        this.password = password;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.taxId = taxId;
        this.customerType = customerType; 
    }

    // Empty Constructor for Simulator
    public Company() {
        super(); 
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) { 
        this.companyName = companyName; 
    }
}