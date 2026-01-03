package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public abstract class Customer extends User{


    String email;
    String phone;
    GlobalConsts.userType customerType;
    int taxId;


    abstract void ManageAccount();

    void ManageContactInfo(String newEmail,String newPhone,int newTaxId){
        if(newEmail != null){
           this.setEmail(newEmail); 
        }

        if(newPhone != null){
            this.setPhone(newPhone);
        }

        if(newTaxId != -1){
            this.taxId = newTaxId;
        }
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCustomerType(GlobalConsts.userType customerType) {
        this.customerType = customerType;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getPhone() {
        return phone;
    }
    public GlobalConsts.userType getUserType() {
        return customerType;
    }
    public int getTaxId(){
        return taxId;
    }
    
}
