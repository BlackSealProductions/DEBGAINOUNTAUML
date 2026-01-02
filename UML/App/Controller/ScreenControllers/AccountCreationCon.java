package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.AccountCreationScreen;
import App.View.Screens.DashboardScreen;
import App.View.Screens.LoginScreen;

import javax.swing.*;
import java.util.*;

public class AccountCreationCon implements Controller_t {

    private AccountCreationScreen view;
    private ModelHandler model;
    private ViewHandler viewHandler;
   

    public AccountCreationCon(AccountCreationScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        // Handle the "Ολοκλήρωση" button
        view.getFinishBtn().addActionListener(e -> handleAcctCreation());
        
        // Handle Logout
        view.getLogoutBtn().addActionListener(e -> {
            Session.getInstance().logout();
            view.hide();
            LoginScreen next = viewHandler.getLoginScreen();
            next.show();
            ViewSession.getInstance().updateScreenHistory(next);
            ViewSession.getInstance().clearHistory();
        });
    }

    private void handleAcctCreation() {

        Customer user = Session.getInstance().getActiveCustomer();
        List<Account> currentAccounts = Session.getInstance().getCustomerAccounts();

        // 2. Business Logic Validation: Companies can only have one account
        if ("Company".equalsIgnoreCase(user.getUserTypeString()) && !currentAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Σφάλμα: Οι εταιρικοί λογαριασμοί επιτρέπεται να έχουν μόνο έναν τραπεζικό λογαριασμό.", 
                "Περιορισμός Λογαριασμού", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String pOwner = view.getPrimaryOwner();
        String sOwner = view.getSecondaryOwner();

        // 1. Generate Unique Data
        String newId = generateUniqueID();
        String newIban = generateFakeIBAN();
        String initialBalance = "0";
        String rate = "1%";

        Account newAccount = new Account(newId, pOwner, newIban, initialBalance, rate, sOwner);

        // --- NEW: Generate RF Code if the user is a Company ---
        if ("Company".equalsIgnoreCase(user.getUserTypeString())) {
            String rfCode = generateRFCode();
            newAccount.setRfCode(rfCode);
            
        }
        // -----------------------------------------------------

        Session.getInstance().appendCustomerAccounts(newAccount);
        Session.getInstance().setActiveAccount(newAccount);

        // Save to Database (This will now include the RF code because we updated JsonDatabase earlier)
        model.saveChangesToDB_sess();

        if (!"Company".equalsIgnoreCase(user.getUserTypeString())) {
             JOptionPane.showMessageDialog(null, "Ο λογαριασμός δημιουργήθηκε επιτυχώς!\nIBAN: " + newIban);
        }

        String type = Session.getInstance().getActiveCustomer().getUserTypeString();
        String name;
        if(type.equals("Company")){
            name = ((Company)Session.getInstance().getActiveCustomer()).getCompanyName();
        }
        else if (type.equals("Individual")){
            name = ((Individual)Session.getInstance().getActiveCustomer()).getFirstName();
        }
        else{
            name = Session.getInstance().getActiveCustomer().getUsername();
        }
        
        // 5. Transition to Dashboard
        view.hide();
        DashboardScreen next = viewHandler.getDashboardScreen();
        next.setAccountDetails(name, initialBalance, newId, type);
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
        ViewSession.getInstance().clearHistory();
    }

    private String generateUniqueID() {
        JsonDatabase db = model.getDB();
        Set<String> existingIds = db.getExistingAcctIds();

        // Generate a random ID and ensure it's not a duplicate
        Random rand = new Random();
        String candidateId;
        do {
            candidateId = "ID_" + (10000000 + rand.nextInt(90000000));
        } while (existingIds.contains(candidateId));

        return candidateId;
    }

    private String generateFakeIBAN() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("GR");
        for (int i = 0; i < 25; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    // --- NEW HELPER ---
    private String generateRFCode() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("RF");
        // RF codes are usually 25 chars total (RF + 23 digits)
        for (int i = 0; i < 23; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}