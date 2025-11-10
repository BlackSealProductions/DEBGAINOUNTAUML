package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public class User {

    String userID;
    String username;
    String password;
    String email;
    String phone;
    GlobalConsts.userType userType;


    public String getUserID() {
        return userID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public GlobalConsts.userType getUserType() {
        return userType;
    }


    
} 
