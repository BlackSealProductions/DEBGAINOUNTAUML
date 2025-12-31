package App.Controller.ScreenControllers;

import App.Controller.Controller_t; 
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.User;     
import App.Model.Entities.UserEntities.Account; 
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.AccountCreationScreen;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.TypeSelectionScreen;
import App.View.ViewHandler;
import App.View.ViewSession;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class LoginCon implements Controller_t {

    private LoginScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;

    public LoginCon(LoginScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        if (view == null) return;
        view.getLoginBtn().addActionListener(e -> handleLogin());
        view.getRegisterBtn().addActionListener(e -> handleRegister());
    }

    private void handleRegister(){
        view.hide();
        TypeSelectionScreen next = viewHandler.getChooseRegisterType();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    @SuppressWarnings("unchecked")
    private void handleLogin() {
        try {
            String inputUser = view.getUsername();
            String inputPass = view.getPassword();
        
            // 1. Get raw data from the corrected JsonDatabase
            List<Map<String, Object>> records = JsonDatabase.getAllRecords();
            Map<String, Object> foundUserMap = null;
        
            // 2. Find user (Iterate through the wrappers)
            for (Map<String, Object> wrapper : records) {
                // The JSON structure is [ { "user": { ... } }, ... ]
                // So we must get the "user" object from the wrapper
                Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
                
                if (user == null) continue;

                String dbUser = (String) user.get("username");
                String dbPass = (String) user.get("password");

                if (dbUser != null && dbUser.equals(inputUser) && dbPass != null && dbPass.equals(inputPass)) {
                    foundUserMap = user;
                    break;
                }
            }
        
            if (foundUserMap != null) {
                System.out.println("Login Successful for: " + inputUser);

                // 3. Create User Object from the Map
                // We use helper to safely handle nulls
                User activeUser = new User(
                    getStr(foundUserMap, "username"),
                    getStr(foundUserMap, "password"),
                    getStr(foundUserMap, "name"), // If company, this holds company name usually
                    getStr(foundUserMap, "surname"),
                    getStr(foundUserMap, "phone"),
                    getStr(foundUserMap, "email"),
                    getStr(foundUserMap, "type"),
                    getStr(foundUserMap, "taxId")
                );

                // 4. Populate Accounts
                Object accountsObj = foundUserMap.get("accounts");
                if (accountsObj instanceof List) {
                    List<Map<String, String>> rawAccounts = (List<Map<String, String>>) accountsObj;
                    for (Map<String, String> rawAcc : rawAccounts) {
                        // Skip if accountId is missing (Ghost account check)
                        if (rawAcc.get("accountId") == null || rawAcc.get("accountId").isEmpty()) continue;

                        Account acc = new Account(
                            rawAcc.get("accountId"),
                            rawAcc.get("ownerName"),
                            rawAcc.get("iban"),
                            rawAcc.get("balance"),
                            rawAcc.get("interestRate"),
                            rawAcc.get("secondaryOwner")
                        );
                        activeUser.addAccount(acc);
                    }
                }

                // 5. Start Session
                Session.getInstance().login(activeUser);
                
                // 6. Navigate based on accounts
                if (activeUser.getAccounts().isEmpty()){
                    // No accounts -> Go to Creation
                    view.hide();
                    AccountCreationScreen next = viewHandler.getAccountCreationScreen();
                    next.setHelloMessage(activeUser.getName());
                    next.setPrimaryOwnerLabel(activeUser.getUsername());
                    next.show();
                    ViewSession.getInstance().updateScreenHistory(next);
                } else {
                    // Has accounts -> Go to Selection
                    view.hide();
                    AccountSelectionScreen next = viewHandler.getAccountSelectionScreen();
                    
                    // Convert back to List<Map> just for the view's list display helper
                    // (Or update AccountSelectionScreen to accept List<Account> later)
                    // For now, we rebuild the list for compatibility:
                    List<Map<String, String>> viewList = new ArrayList<>();
                    for(Account a : activeUser.getAccounts()){
                        Map<String, String> m = new java.util.HashMap<>();
                        m.put("accountId", a.getAccountId());
                        m.put("iban", a.getIban());
                        m.put("balance", a.getBalance());
                        viewList.add(m);
                    }
                    
                    next.populateAccounts(viewList); 
                    next.show();
                    ViewSession.getInstance().updateScreenHistory(next);
                }
                
                // Clear history on new login
                ViewSession.getInstance().clearHistory();

            } else {
                System.out.println("Login Failed. User not found or wrong pass.");
                JOptionPane.showMessageDialog(null, "Invalid Credentials");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Login Error: " + e.getMessage());
        }
    }

    // Helper to prevent NullPointer if a field is missing in JSON
    private String getStr(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return (val == null) ? "" : val.toString();
    }
}