package App;

import App.Model.Entities.UserEntities.Individual;
import Utils.GlobalConsts;

public class MainCLI {
    
    public static void main(String[] args){

        Individual normal_user = new Individual("Christos", "Kadas", "Kapote", "0", "`1", "ckadas", "poutsa", "ckadas@tuc.gr", "123", GlobalConsts.userType.INDIVIDUAL);
    
        System.out.println(normal_user.getUsername());
    
    
    }
}
