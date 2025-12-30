package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.Database.JsonDatabase; // Import Database
import App.Model.ModelHandler;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterScreen;
import App.View.ViewHandler;
import App.View.ViewSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterCon implements Controller_t {

    private RegisterScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;

    public RegisterCon(RegisterScreen view, ModelHandler model, ViewHandler viewHandler) {
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
        view.getRegisterBtn().addActionListener(e -> handleRegistration());

    }


    private void handleRegistration() {
        // A. GET DATA FROM VIEW
        String username = view.getUsername();
        String password = view.getPassword();
        String name = view.getName();
        String surname = view.getSurname();
        String citizenId = view.getCitizenId();
        String email = view.getEmail(); // Ensure View has getEmail()
        String phone = view.getPhone(); // Ensure View has getPhone()

        // B. VALIDATION (Basic)
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || citizenId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields (Name, ID, User, Pass).");
            return;
        }

        // C. PREPARE DATA FOR DATABASE
        Map<String, String> newUser = new HashMap<>();
        
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("name", name);
        newUser.put("surname", surname);
        newUser.put("citizenId", citizenId);
        newUser.put("email", email);
        newUser.put("phone", phone);
        
        // Auto-generate Bank details
        newUser.put("balance", "0.0"); // Start with 0 money
        newUser.put("interestRate", "0.5");
        newUser.put("iban", generateFakeIBAN()); 
        newUser.put("secondaryOwner", "None");

        // D. SAVE TO FILE
        JsonDatabase.saveRecord(newUser);

        // E. SUCCESS MESSAGE & NAVIGATE
        JOptionPane.showMessageDialog(null, "Registration Successful! Please Login.");
        view.hide();
        LoginScreen next = viewHandler.getLoginScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
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