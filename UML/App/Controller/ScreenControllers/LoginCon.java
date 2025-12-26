package App.Controller.ScreenControllers;

import App.Controller.Controller_t; 
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.LoginScreen;
import App.View.ViewHandler; // IMPORT THIS

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class LoginCon extends Controller_t {

    private LoginScreen view;
    private ModelHandler model; 
    
    // --- 1. DECLARE VIEW HANDLER HERE ---
    private ViewHandler viewHandler;

    // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public LoginCon(LoginScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
    }

    @Override
    public void init() {
        if (view == null) return;

        // Login Button Logic
        view.getLoginBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Register Button Logic
        view.getRegisterBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Now this works because viewHandler exists!
                viewHandler.showRegisterScreen();
            }
        });
    }

    private void handleLogin() {
        String inputUser = view.getUsername();
        String inputPass = view.getPassword();

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        List<Map<String, String>> records = JsonDatabase.getAllRecords();
        boolean found = false;
        Map<String, String> userRecord = null;

        for (Map<String, String> record : records) {
            if (record.get("username").equals(inputUser) && record.get("password").equals(inputPass)) {
                found = true;
                userRecord = record;
                break;
            }
        }

        if (found && userRecord != null) {
            Account activeAccount = new Account(
                userRecord.get("citizenId"),
                userRecord.get("name") + " " + userRecord.get("surname"),
                userRecord.get("iban"),
                userRecord.get("balance"),
                userRecord.get("interestRate"),
                userRecord.get("secondaryOwner")
            );

            Session.getInstance().login(inputUser, userRecord.get("citizenId"), activeAccount);
            
            JOptionPane.showMessageDialog(null, "Login Successful! Welcome " + inputUser);
            
            // Switch to Dashboard
            viewHandler.showDashboard(); 

        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password.");
        }
    }
}