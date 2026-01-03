package App.Model;

import java.util.List;
import java.util.Map;
import java.util.Random;

import App.Model.Entities.OperationEntities.Transaction;
import App.Model.Entities.UserEntities.Account;
import App.Model.Entities.UserEntities.Company;
import App.Model.Entities.UserEntities.Customer;
import App.Model.Entities.UserEntities.Individual;
import App.View.Screens.SimulationScreen;

public class Simulator {

    private ModelHandler model;
    private SimulationScreen view;
    private boolean isRunning = false;
    
    // CHANGE: Using your existing converter (No import needed as it's in App.Model)
    private DatabaseObjectConverter conv = new DatabaseObjectConverter(); 

    private final String[] BILL_TYPES = {"Electricity Bill", "Water Bill", "Internet", "Phone", "Tax", "Insurance"};

    public Simulator(ModelHandler model, SimulationScreen view) {
        this.model = model;
        this.view = view;
    }

    public void startSimulation(int botCount, int actionsPerBot) {
        isRunning = true;
        
        new Thread(() -> {
            view.appendLog(">>> STARTING SIMULATION (USING DATABASE OBJECT CONVERTER) <<<");

            List<Map<String, Object>> userRecords = model.get_uDB().getAllRecords();
            
            if (userRecords.size() < 2) {
                view.appendLog("ERROR: Not enough users in DB.");
                return;
            }

            Random rand = new Random();
            int successCount = 0;
            double totalVolume = 0;

            for (int i = 0; i < actionsPerBot; i++) {
                if (!isRunning) break;

                for (int b = 0; b < botCount; b++) {
                    try {
                        // --- A. SETUP ---
                        Map<String, Object> uMap = userRecords.get(rand.nextInt(userRecords.size()));
                        Customer mainUser = mapToCustomer(uMap);
                        if (mainUser == null || mainUser.getAccounts() == null || mainUser.getAccounts().isEmpty()) continue;

                        Account mainAccount = mainUser.getAccounts().get(rand.nextInt(mainUser.getAccounts().size()));

                        int actionType = rand.nextInt(4); 
                        double amount = 10 + (490 * rand.nextDouble()); 
                        Transaction tx = null;
                        boolean success = false;
                        String logMessage = ""; 

                        // Parse Balance Safely
                        double currentBalance = 0.0;
                        try {
                            currentBalance = Double.parseDouble(String.valueOf(mainAccount.getBalance()).replace(",", "."));
                        } catch (Exception e) { currentBalance = 0.0; }

                        // --- B. LOGIC ---
                        switch (actionType) {
                            case 0: // TRANSFER
                                Map<String, Object> rMap = userRecords.get(rand.nextInt(userRecords.size()));
                                Customer rUser = mapToCustomer(rMap); 
                                if (rUser == null || rUser.getAccounts() == null || rUser.getAccounts().isEmpty()) continue;
                                Account rAccount = rUser.getAccounts().get(rand.nextInt(rUser.getAccounts().size()));

                                if (mainAccount.getAccountId().equals(rAccount.getAccountId())) continue;

                                tx = new Transaction(generateTxId(), mainAccount.getAccountId(), rAccount.getAccountId(), amount, "2026-01-03", "12:00", "Transfer", "Transfer");

                                // TYPO FIX: using getAmmount() to match your Entity
                                if (currentBalance >= tx.getAmmount()) {
                                    updateBalance(mainAccount, currentBalance - amount);
                                    
                                    double rBal = Double.parseDouble(String.valueOf(rAccount.getBalance()).replace(",", "."));
                                    updateBalance(rAccount, rBal + amount);
                                    
                                    saveChanges(rUser, rAccount); // Save Receiver
                                    success = true;
                                    logMessage = String.format("[TRANSFER] %s -> %s : €%.2f", 
                                        mainAccount.getAccountId(), rAccount.getAccountId(), amount);
                                }
                                break;

                            case 1: // DEPOSIT
                                tx = new Transaction(generateTxId(), mainAccount.getAccountId(), mainAccount.getAccountId(), amount, "2026-01-03", "12:00", "ATM Deposit", "Deposit");
                                updateBalance(mainAccount, currentBalance + amount);
                                success = true;
                                logMessage = String.format("[DEPOSIT] %s : €%.2f", mainAccount.getAccountId(), amount);
                                break;

                            case 2: // WITHDRAWAL
                                tx = new Transaction(generateTxId(), mainAccount.getAccountId(), "ATM", amount, "2026-01-03", "12:00", "ATM Withdrawal", "Withdrawal");
                                if (currentBalance >= tx.getAmmount()) {
                                    updateBalance(mainAccount, currentBalance - amount);
                                    success = true;
                                    logMessage = String.format("[WITHDRAWAL] %s : €%.2f", mainAccount.getAccountId(), amount);
                                }
                                break;

                            case 3: // PAYMENT
                                String bill = BILL_TYPES[rand.nextInt(BILL_TYPES.length)];
                                tx = new Transaction(generateTxId(), mainAccount.getAccountId(), "SERVICE", amount, "2026-01-03", "12:00", bill, "Payment");
                                if (currentBalance >= tx.getAmmount()) {
                                    updateBalance(mainAccount, currentBalance - amount);
                                    success = true;
                                    logMessage = String.format("[PAYMENT] %s : €%.2f (%s)", 
                                        mainAccount.getAccountId(), amount, bill);
                                }
                                break;
                        }

                        // --- C. SAVE & LOG ---
                        if (success && tx != null) {
                            mainAccount.addTransaction(tx);
                            saveChanges(mainUser, mainAccount);
                            
                            view.appendLog(logMessage);
                            
                            successCount++;
                            totalVolume += amount;
                            
                            final int s = successCount;
                            final double v = totalVolume;
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                view.setTotalTx(String.valueOf(s));
                                view.setVolume(String.format("%.2f", v));
                            });
                        }
                        
                        Thread.sleep(50); 

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            view.appendLog(">>> SIMULATION COMPLETE <<<");
            isRunning = false;
        }).start();
    }

    // ==========================================================
    //    HELPERS
    // ==========================================================

    private void updateBalance(Account acct, double newAmount) {
        acct.setBalance(String.valueOf(newAmount).replace(",", "."));
    }

    public void saveChanges(Customer user, Account acct){
        saveChangesToUDB_conv(user);
        saveChangesToTDB_conv(acct);
    }

    public void saveChangesToUDB_conv(Customer user){
        // Calling your DatabaseObjectConverter
        this.model.get_uDB().updateUserRecord(conv.convertUserToMap(user, user.getAccounts()));
    }

    public void saveChangesToTDB_conv(Account acct){
        // Calling your DatabaseObjectConverter
        this.model.get_tDB().appendTransactionRecord(conv.convertAcctTransactionsToMap(acct));
    }

    private String generateTxId() {
        return String.valueOf(System.currentTimeMillis() + new Random().nextInt(10000));
    }

    private Customer mapToCustomer(Map<String, Object> wrapper) {
        Map<String, Object> uData = (Map<String, Object>) wrapper.get("user");
        String type = (String) uData.get("type");
        
        Customer customer;

        if ("Company".equalsIgnoreCase(type)) {
            Company c = new Company(); 
            c.setCompanyName((String) uData.get("companyName"));
            customer = c;
        } else {
            Individual i = new Individual();
            i.setFirstName((String) uData.get("name"));
            i.setLastName((String) uData.get("surname"));
            customer = i;
        }

        customer.setUsername((String) uData.get("username"));
        customer.setPassword((String) uData.get("password"));
        customer.setEmail((String) uData.get("email"));
        customer.setPhoneNumber((String) uData.get("phone"));
        customer.setTaxId((String) uData.get("taxId"));

        List<Map<String, String>> accMaps = (List<Map<String, String>>) uData.get("accounts");
        if (accMaps != null) {
            for (Map<String, String> am : accMaps) {
                Account a = new Account(); 
                a.setAccountId(am.get("accountId"));
                a.setIban(am.get("iban"));
                a.setPrimaryOwnerId(am.get("ownerName")); 
                a.setBalance(am.get("balance")); 
                a.setRfCode(am.get("rfCode"));
                
                customer.addAccount(a);
            }
        }
        return customer;
    }
}
    
