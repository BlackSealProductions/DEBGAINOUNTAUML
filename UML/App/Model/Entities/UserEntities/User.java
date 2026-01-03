package App.Model.Entities.UserEntities;

import Utils.GlobalConsts;

public abstract class User {


    String userID;
    String username;
    String password;
    
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    void ManageInfo(String newName,String newPassword){
        if(newName != null){
            this.setUsername(newName);
        }

        if(newPassword != null){
            this.setPassword(newPassword);
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
} 
