package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class Individual extends Customer {
    
    String firstName;
    String lastName;
    String dateOfBirth;

    // Full Constructor
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

    // Empty Constructor for Simulator
    public Individual() {
        super();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDateOfBirth() { return dateOfBirth; }

    public void setFirstName(String name) { this.firstName = name; }
    public void setLastName(String surname) { this.lastName = surname; }
}