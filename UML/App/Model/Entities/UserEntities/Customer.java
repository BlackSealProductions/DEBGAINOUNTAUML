package App.Model.Entities.UserEntities;

import java.util.List;

import Utils.GlobalConsts;

public abstract class Customer extends User{


    String email;
    String phone;
    GlobalConsts.userType customerType;
    String taxId;
    List<Account> accounts;

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
    public List<Account> getAccounts(){
        return this.accounts;
    }
    public void setAccounts(List<Account> accs){
        this.accounts = accs;
    }
    
}
