package App.Model.Entities.UserEntities;

import java.util.List;

import Utils.GlobalConsts;

public abstract class Customer extends User{


    String email;
    String phone;
    GlobalConsts.userType customerType;
    String taxId;
    


    // public String getUserID() {
    //     return userID;
    // }
    // public String getUsername() {
    //     return username;
    // }
    // public String getPassword() {
    //     return password;
    // }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public GlobalConsts.userType getUserType() {
        return customerType;
    }
    public String getUserTypeString() {
        return customerType==Utils.GlobalConsts.userType.BUSINESS ? "Company" : customerType==Utils.GlobalConsts.userType.INDIVIDUAL ? "Individual" : null;
    }
    public String getTaxId(){
        return taxId;
    }
    
}
