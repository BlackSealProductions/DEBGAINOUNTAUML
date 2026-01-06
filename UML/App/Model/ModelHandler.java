package App.Model;

import java.util.List;
import java.util.Map;

import App.Model.Database.BillDB;
import App.Model.Database.OrderDB;
import App.Model.Database.TransactionDB;
import App.Model.Database.UserDB;
import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Customer;

public class ModelHandler {

    private UserDB uDB;
    private TransactionDB tDB;
    private OrderDB oDB;
    private BillDB bDB;
    private DatabaseObjectConverter conv;
    public OrderAssesor ass;
    private boolean isSimulationActive = false;

    public void init(){
        this.uDB = new UserDB();
        this.tDB = new TransactionDB();
        this.oDB = new OrderDB();
        this.bDB = new BillDB();
        this.conv = new DatabaseObjectConverter();
        this.ass = new OrderAssesor(this);
    }

    public void saveChanges(){
        saveChangesToUDB_conv();
        saveChangesToTDB_conv();
        saveChangesToODB_conv();
        saveChangesToBDB_conv();
    }


    public void saveChangesToUDB_conv(){
        Customer user = Session.getInstance().getActiveCustomer();
        this.uDB.updateUserRecord(conv.convertUserToMap(user, user.getAccounts()));
    }

    public void saveChangesToTDB_conv(){
        Account acct = Session.getInstance().getActiveAccount();
        if(!acct.getTransactions().isEmpty()){
            this.tDB.updateUserRecord(conv.convertAcctTransactionsToMap(acct));
        }
    }

    public void saveChangesToODB_conv(){
        Account acct = Session.getInstance().getActiveAccount();
        if(!acct.getStandingorders().isEmpty()){
            this.oDB.updateUserRecord(conv.convertAcctOrdersToMap(acct));
        }
    }

    public void saveChangesToBDB_conv(){
        Account acct = Session.getInstance().getActiveAccount();
        this.bDB.updateUserRecord(conv.convertAcctBillsToMap(acct));
        // if(!acct.getBills().isEmpty()){
        // }
    }


    public void saveChangesToODB_conv_Id(List<StandingOrder> orders, String accountId){
        this.oDB.updateUserRecord(conv.convertAcctOrdersToMap_Id(orders, accountId));
    }


    // public void saveChangesToDB_sess(){
    //     this.db.updateUserRecord(Session.getInstance().convertActiveUserToMap());
    // }

    public UserDB get_uDB(){
        return this.uDB;
    } 

    public TransactionDB get_tDB(){
        return this.tDB;
    } 

    public OrderDB get_oDB(){
        return this.oDB;
    }
    
    public BillDB get_bDB(){
        return this.bDB;
    }

    public Session getSessionInst(){
        return Session.getInstance();
    }

    public DatabaseObjectConverter getConverter(){
        return this.conv;
    }
    public void setSimulationActive(boolean active) {
        this.isSimulationActive = active;
    }

    public boolean isSimulationActive() {
        return this.isSimulationActive;
    }

}

