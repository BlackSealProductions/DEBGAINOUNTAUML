package App.Controller.ScreenControllers;

import App.Controller.ControllerHandler;
import App.Controller.Controller_t; 
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.TypeSelectionScreen;
import App.View.Screens.AccountCreationScreen;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.DashboardScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterIndividualScreen;
import App.View.ViewHandler; // IMPORT THIS
import App.View.ViewSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class LoginCon implements Controller_t {

    private LoginScreen view;
    private ModelHandler model; 
    
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
        view.getLoginBtn().addActionListener(e -> handleLogin());


        // Register Button Logic
        view.getRegisterBtn().addActionListener(e -> handleRegister());

    }

    private void handleRegister(){
        view.hide();
        TypeSelectionScreen next = viewHandler.getChooseRegisterType();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    private void handleLogin() {
        String inputUser = view.getUsername();
        String inputPass = view.getPassword();
    
        List<Map<String, Object>> records = JsonDatabase.getAllRecords();
        Map<String, Object> foundUser = null;
    
        for (Map<String, Object> wrapper : records) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            if (user.get("username").equals(inputUser) && user.get("password").equals(inputPass)) {
                foundUser = user;
                break;
            }
        }
    
        if (foundUser != null) {
            // Store the whole user object in the session
            Session.getInstance().login((String)foundUser.get("username"), (String)foundUser.get("taxId"), foundUser);
            
            List<Map<String, String>> accountsList = (List<Map<String, String>>) foundUser.get("accounts");
            // boolean trulyEmpty = (accountsList == null || accountsList.isEmpty());

            // if (!trulyEmpty) {
            //     // Check if the first entry is just a "ghost" map with no real ID
            //     String firstId = accountsList.get(0).get("accountId");
            //     if (firstId == null || firstId.trim().isEmpty()) {
            //         trulyEmpty = true;
            //     }
            // }
            if ((accountsList == null || accountsList.isEmpty())){
                view.hide();
                AccountCreationScreen next = viewHandler.getAccountCreationScreen();
                next.setHelloMessage((String)foundUser.get("name"));
                next.setPrimaryOwnerLabel((String)foundUser.get("username"));
                next.show();
                ViewSession.getInstance().updateScreenHistory(next);
                ViewSession.getInstance().clearHistory();
            }
            else{
                view.hide();
                AccountSelectionScreen next = viewHandler.getAccountSelectionScreen();
                next.populateAccounts(accountsList);
                next.show();
                ViewSession.getInstance().updateScreenHistory(next);
                ViewSession.getInstance().clearHistory();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Credentials");
        }
    }
}