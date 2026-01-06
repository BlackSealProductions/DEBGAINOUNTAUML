package App.Controller.ScreenControllers;

import App.Controller.Controller_t; 
import App.Model.Database.UserDB;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.TypeSelectionScreen;
import App.View.Screens.AccountCreationScreen;
import App.View.Screens.AccountSelectionScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.AdminMenuScreen; // <--- NEW IMPORT
import App.View.ViewHandler; 
import App.View.ViewSession;
import Utils.GlobalConsts;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private void handleLogin() {
        String inputUser = view.getUsername();
        String inputPass = view.getPassword();

        // ============================================================
        // 1. ADMIN BYPASS CHECK (Starts Here)
        // ============================================================
        if (inputUser.equals("admin") && inputPass.equals("admin")) {
            System.out.println(">>> Admin Detected. Launching Dashboard...");
            
            view.hide();
            SwingUtilities.getWindowAncestor(view.getMainPanel()).dispose();

            openAdminDashboard();
            view.clearFields();

            return;
        }
        // ============================================================
        //    END ADMIN CHECK
        // ============================================================

        UserDB db = model.get_uDB();
        Map<String, Object> foundUser = db.findUserWithPassword(inputUser, inputPass);
    
        if (foundUser != null) {
            Session.getInstance().login(foundUser, model);
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
            view.clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Credentials");
        }
    }

    // --- Helper to Open Admin Dashboard ---
    private void openAdminDashboard() {
        // Create View
        AdminMenuScreen adminView = new AdminMenuScreen();
        adminView.init();

        // Create Controller (Pass Model so Audit Logs work!)
        AdminMenuCon adminCon = new AdminMenuCon(adminView, this.model, this.viewHandler); 
        adminCon.init();

        // Show Window
        JFrame frame = new JFrame("Bank of TUC - Admin");
        frame.setContentPane(adminView.getMainPanel());
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}