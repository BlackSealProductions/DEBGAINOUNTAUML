package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class Individual extends User{
    
    String firstName;
    String lastName;
    String dateOfBirth;
    String taxId;

    public Individual(String firstName, String lastName, String dateOfBirth, String taxId, String userID, String username, String password, String email, String phone, GlobalConsts.userType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.taxId = taxId;
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.userType = userType; 
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

    public String getTaxId() {
        return taxId;
    }


    
}
