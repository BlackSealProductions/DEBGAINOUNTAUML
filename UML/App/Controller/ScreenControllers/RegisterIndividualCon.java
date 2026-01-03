package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.UserDB; // Import Database
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Individual;
import App.Model.ModelHandler;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterIndividualScreen;
import App.View.ViewHandler;
import App.View.ViewSession;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegisterIndividualCon implements Controller_t {

    private RegisterIndividualScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;


    public RegisterIndividualCon(RegisterIndividualScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;

    }

    @Override
    public void init() {
        if (view == null) return;

        // 1. BACK BUTTON
        // view.getBackBtn().addActionListener(e -> handleBack());

        // 2. REGISTER BUTTON
        view.getRegisterBtn().addActionListener(e -> handleRegister());

    }


   private void handleRegister() {
    
        // 1. Collect data from view
        String user = view.getUsername();
        String pass = view.getPassword();
        String name = view.getName();
        String sname = view.getSurname();
        String email = view.getEmail();
        String phone = view.getPhone();
        String taxId = view.getCitizenId(); // Now mapping to taxId

        // Basic validation
        if (user.isEmpty() || pass.isEmpty() || taxId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
            return;
        }

        Individual newUser = new Individual(name, sname, null, taxId, user, pass, email, phone, Utils.GlobalConsts.userType.INDIVIDUAL);
        List<Account> defaultAccList = new ArrayList<Account>();

        Map<String, Object> newUserWrapper = model.getConverter().convertUserToMap(newUser, defaultAccList);

        // 3. Save to Database
        UserDB db = model.get_uDB();
        db.saveRecord(newUserWrapper);

        // model.addEntryToUDB_conv(newUser);

        JOptionPane.showMessageDialog(null, "Registration Successful!\n Please login");
        
        // Return to Login
        view.hide();
        LoginScreen next = viewHandler.getLoginScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
        ViewSession.getInstance().clearHistory();

}
    // Helper to make a fake GR IBAN
    private String generateFakeIBAN() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("GR");
        for (int i = 0; i < 25; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}