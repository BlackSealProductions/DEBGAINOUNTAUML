package App.Model;

import java.util.List;
import java.util.Map;
import App.Model.Database.JsonDatabase;

public class ModelHandler {

    private JsonDatabase db;
    private DatabaseObjectConverter conv;

    public void init(){
        this.db = new JsonDatabase();
        this.conv = new DatabaseObjectConverter();
    }

    public void saveChangesToDB_conv(){
        this.db.updateUserRecord(conv.convertUserToMap(Session.getInstance().getActiveCustomer(), Session.getInstance().getCustomerAccounts()));
    }

    public void saveChangesToDB_sess(){
        this.db.updateUserRecord(Session.getInstance().convertActiveUserToMap());
    }

    public JsonDatabase getDB(){
        return this.db;
    } 

    public Session getSessionInst(){
        return Session.getInstance();
    }

    public DatabaseObjectConverter getConverter(){
        return this.conv;
    }

}

