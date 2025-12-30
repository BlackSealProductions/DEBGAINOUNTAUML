package App.Model.Entities.UserEntities;
public class Company extends Customer {


    String companyName;

    public Company(String companyName){

        this.companyName=companyName;

    }

    public String getCompanyName() {
        return companyName;
    }

    
    
}
