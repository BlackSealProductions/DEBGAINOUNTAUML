package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
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

        // 1. Get User Data from Session
        Map<String, Object> userData = Session.getInstance().getUserData();
        String userType = (String) userData.get("type");
        List<Map<String, String>> currentAccounts = (List<Map<String, String>>) userData.get("accounts");

        // 2. Business Logic Validation: Companies can only have one account
        if ("Company".equalsIgnoreCase(userType) && currentAccounts != null && !currentAccounts.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Σφάλμα: Οι εταιρικοί λογαριασμοί επιτρέπεται να έχουν μόνο έναν τραπεζικό λογαριασμό.", 
                "Περιορισμός Λογαριασμού", 
                JOptionPane.WARNING_MESSAGE);
            
            // If they came from the Dashboard, take them back. 
            // If they just logged in, this case shouldn't realistically happen due to your Login logic,
            // but we return here to prevent any DB writing.
            return;
        }
        String pOwner = view.getPrimaryOwner();
        String sOwner = view.getSecondaryOwner();

        if (pOwner.isEmpty() || pOwner.equals("Primary owner")) {
            JOptionPane.showMessageDialog(null, "Παρακαλώ εισάγετε το όνομα του κύριου κατόχου.");
            return;
        }

        // 1. Generate Unique Data
        String newId = generateUniqueID();
        String newIban = generateFakeIBAN();
        String initialBalance = "0";
        String rate = "1%";

        // 2. Create the Map for JsonDatabase
        Map<String, String> newAccountMap = new HashMap<>();
        newAccountMap.put("accountId", newId);
        newAccountMap.put("iban", newIban);
        newAccountMap.put("ownerName", pOwner);
        newAccountMap.put("secondaryOwner", sOwner.equals("Secondary owner") ? "-" : sOwner);
        newAccountMap.put("balance", initialBalance);
        newAccountMap.put("interestRate", rate);

        // 3. Update Database
        String currentUsername = Session.getInstance().getUsername();
        JsonDatabase.addAccountToUser(currentUsername, newAccountMap);

        // 4. Update Session with the new formal Account Object
        Account activeAcc = new Account(newId, newIban, pOwner, sOwner, initialBalance, rate);
        Session.getInstance().setActiveAccount(activeAcc);

        JOptionPane.showMessageDialog(null, "Ο λογαριασμός δημιουργήθηκε επιτυχώς!\nIBAN: " + newIban);

        // 5. Transition to Dashboard
        view.hide();
        DashboardScreen next = viewHandler.getDashboardScreen();
        next.setAccountDetails(pOwner, initialBalance, newId);
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
        ViewSession.getInstance().clearHistory();
    }

    private String generateUniqueID() {
        List<Map<String, Object>> records = JsonDatabase.getAllRecords();
        Set<String> existingIds = new HashSet<>();

        // Collect all IDs currently in the system
        for (Map<String, Object> wrapper : records) {
            Map<String, Object> user = (Map<String, Object>) wrapper.get("user");
            List<Map<String, String>> accounts = (List<Map<String, String>>) user.get("accounts");
            if (accounts != null) {
                for (Map<String, String> acc : accounts) {
                    existingIds.add(acc.get("accountId"));
                }
            }
        }

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
}