package App.Controller.ScreenControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Database.JsonDatabase;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterCompanyScreen;
import App.View.Screens.RegisterScreen;

public class RegisterCompanyCon implements Controller_t{
    
    private RegisterCompanyScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public RegisterCompanyCon(RegisterCompanyScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;

        // 2. REGISTER BUTTON
        view.getRegisterBtn().addActionListener(e -> handleRegister());

    }


   private void handleRegister() {
    
        // 1. Collect data from view
        String user = view.getUsername();
        String pass = view.getPassword();
        String cname = view.getCompanyName();
        String email = view.getEmail();
        String phone = view.getPhone();
        String taxId = view.getCitizenId(); // Now mapping to taxId

        // Basic validation
        if (user.isEmpty() || pass.isEmpty() || taxId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
            return;
        }

        // 2. Build the User Map (Matching the JSON structure)
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("username", user);
        newUser.put("password", pass);
        newUser.put("companyName", cname);
        newUser.put("email", email);
        newUser.put("phone", phone);
        newUser.put("taxId", taxId);
        newUser.put("type", "Company");
        
        // Add default empty accounts list
        newUser.put("accounts", new ArrayList<Map<String, String>>());

        // 3. Save to Database
        JsonDatabase.saveRecord(newUser);

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
