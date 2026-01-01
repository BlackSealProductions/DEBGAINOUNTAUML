package App.Controller.ScreenControllers;

import App.Controller.ControllerHandler;
import App.Controller.Controller_t; 
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;
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
import java.util.ArrayList;
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

        JsonDatabase db = model.getDB();
        Map<String, Object> foundUser = db.findUserWithPassword(inputUser, inputPass);
    
        if (foundUser != null) {
            // Store the whole user object in the session
            
            
            Session.getInstance().login(foundUser);

            Customer user = Session.getInstance().getActiveCustomer();

            String name = null;
            if (user.getUserType().equals(Utils.GlobalConsts.userType.BUSINESS)){
                name = ((Company)user).getCompanyName();
            }
            else if(user.getUserType().equals(Utils.GlobalConsts.userType.INDIVIDUAL)){
                name = ((Individual)user).getFirstName();
            }
            else{ 
                name = user.getUsername();
            }
            
            List<Account> accountsList = (ArrayList<Account>)Session.getInstance().getCustomerAccounts();
            if ((accountsList == null || accountsList.isEmpty())){
                view.hide();
                AccountCreationScreen next = viewHandler.getAccountCreationScreen();
                next.setHelloMessage(name);
                next.setPrimaryOwnerLabel(user.getUsername());
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