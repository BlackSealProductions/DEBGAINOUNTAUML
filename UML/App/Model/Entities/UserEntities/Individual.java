package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class Individual extends Customer{
    
    String firstName;
    String lastName;
    String dateOfBirth;

    public Individual(String firstName, String lastName, String dateOfBirth, String taxId, String username, String password, String email, String phone, GlobalConsts.userType customerType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.taxId = taxId;
        this.customerType = customerType; 
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }


    
}
