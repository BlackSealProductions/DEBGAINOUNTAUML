package App.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private int totalActions = 0;
    private double totalVolume = 0.0;
    
    // --- REDUCED SIMULATION SETTINGS (To prevent JSON bloat) ---
    private final int USERS_PER_DAY = 3;    // Only 3 active users per day
    private final int TX_PER_USER = 2;      // Only 2 transactions each

    public Simulator(ModelHandler model, SimulationScreen view) {
        this.model = model;
        this.view = view;
    }

    public void startSimulation(String startDateStr, int durationDays) {
        isRunning = true;
        totalActions = 0;
        totalVolume = 0.0;
        
        new Thread(() -> {
            view.appendLog(">>> STARTING OPTIMIZED SIMULATION <<<");
            view.appendLog(String.format("TARGET: %d Users x %d Tx per day", USERS_PER_DAY, TX_PER_USER));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate currentDate;

            try {
                currentDate = LocalDate.parse(startDateStr, formatter);
            } catch (Exception e) {
                view.appendLog("ERROR: Invalid Date. Use YYYY-MM-DD");
                return;
            }
            
            List<Map<String, Object>> userRecords = model.get_uDB().getAllRecords();
            Random rand = new Random();

            // --- DAY LOOP ---
            for (int day = 0; day < durationDays; day++) {
                if (!isRunning) break;

                String dateString = currentDate.format(formatter);
                
                javax.swing.SwingUtilities.invokeLater(() -> view.setDateLabel("Date: " + dateString));
                view.appendLog("--------------------------------------------------");
                view.appendLog("PROCESSING DATE: " + dateString);

                // =============================================================
                // 1. MONTHLY INTEREST (End of Month Only)
                // =============================================================
                if (currentDate.getMonth() != currentDate.plusDays(1).getMonth()) {
                    view.appendLog("   [SYSTEM] END OF MONTH: CALCULATING INTEREST...");
                    for (Map<String, Object> uMap : userRecords) {
                        try {
                            Customer user = mapToCustomer(uMap);
                            if (user.getAccounts() != null) {
                                for (Account acc : user.getAccounts()) {
                                    double balance = parseBalance(acc.getBalance());
                                    if (balance > 0) {
                                        double interest = balance * (0.03 / 12.0); 
                                        if (interest >= 0.01) {
                                            updateBalance(acc, balance + interest);
                                            Transaction t = new Transaction(generateTxId(), "BANK_SYSTEM", acc.getAccountId(), interest, dateString, "23:59", "Monthly Interest Yield", "Interest");
                                            acc.addTransaction(t);
                                            saveChanges(user, acc);
                                            totalVolume += interest;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {}
                    }
                    totalActions++;
                }

                // =============================================================
                // 2. STANDING ORDERS (Rent on the 1st)
                // =============================================================
                if (currentDate.getDayOfMonth() == 1) {
                    view.appendLog("   [SYSTEM] 1st OF MONTH: PAYING RENT...");
                    // Limit to just 2 automatic payments to keep logs clean
                    for (int k = 0; k < 2; k++) { 
                        try {
                            Customer user = mapToCustomer(userRecords.get(rand.nextInt(userRecords.size())));
                            if(user.getAccounts().isEmpty()) continue;
                            Account acc = user.getAccounts().get(0);
                            
                            double rent = 450.0;
                            double bal = parseBalance(acc.getBalance());

                            if (bal >= rent) {
                                updateBalance(acc, bal - rent);
                                Transaction t = new Transaction(generateTxId(), acc.getAccountId(), "LANDLORD_ACC", rent, dateString, "09:00", "Standing Order: Rent", "Payment");
                                acc.addTransaction(t);
                                saveChanges(user, acc);
                                view.appendLog(String.format("   [AUTO] %s Rent: -€%.2f", acc.getAccountId(), rent));
                                totalActions++;
                                totalVolume += rent;
                            }
                        } catch(Exception ex) {}
                    }
                }

                // =============================================================
                // 3. CONTROLLED RANDOM ACTIVITY (Low Volume)
                // =============================================================
                List<Map<String, Object>> dailyUsers = new ArrayList<>(userRecords);
                Collections.shuffle(dailyUsers);
                
                // Only pick a few users to act today
                int usersToSimulate = Math.min(USERS_PER_DAY, dailyUsers.size());
                
                for (int i = 0; i < usersToSimulate; i++) {
                    if(!isRunning) break;
                    try {
                        Customer activeUser = mapToCustomer(dailyUsers.get(i));
                        for (int j = 0; j < TX_PER_USER; j++) {
                            performUserAction(activeUser, userRecords, rand, dateString);
                        }
                    } catch(Exception e) {}
                }

                // Update UI Stats
                final int tActs = totalActions;
                final double tVol = totalVolume;
                javax.swing.SwingUtilities.invokeLater(() -> {
                    view.setTotalTx(String.valueOf(tActs));
                    view.setVolume(String.format("%.2f", tVol));
                });

                currentDate = currentDate.plusDays(1);
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
            
            view.appendLog(">>> SIMULATION FINISHED <<<");
            isRunning = false;
        }).start();
    }

    private void performUserAction(Customer actor, List<Map<String, Object>> allRecords, Random rand, String date) {
        if(actor.getAccounts().isEmpty()) return;
        
        Account actorAcc = actor.getAccounts().get(rand.nextInt(actor.getAccounts().size()));
        double actorBal = parseBalance(actorAcc.getBalance());

        // 0=Deposit, 1=Withdraw, 2=Bill, 3=Transfer
        int type = rand.nextInt(4); 
        double amount = 10 + (rand.nextDouble() * 100); // Smaller amounts (€10 - €110)

        try {
            switch (type) {
                case 0: // DEPOSIT
                    updateBalance(actorAcc, actorBal + amount);
                    Transaction tDep = new Transaction(generateTxId(), "ATM_Machine", actorAcc.getAccountId(), amount, date, "10:00", "Cash Deposit", "Deposit");
                    actorAcc.addTransaction(tDep);
                    saveChanges(actor, actorAcc);
                    view.appendLog(String.format("   [DEPOSIT] %s: +€%.2f", actorAcc.getAccountId(), amount));
                    totalVolume += amount;
                    break;

                case 1: // WITHDRAWAL
                    if (actorBal >= amount) {
                        updateBalance(actorAcc, actorBal - amount);
                        Transaction tWith = new Transaction(generateTxId(), actorAcc.getAccountId(), "ATM_Machine", amount, date, "12:30", "Cash Withdrawal", "Withdrawal");
                        actorAcc.addTransaction(tWith);
                        saveChanges(actor, actorAcc);
                        view.appendLog(String.format("   [WITHDRAW] %s: -€%.2f", actorAcc.getAccountId(), amount));
                        totalVolume += amount;
                    }
                    break;

                case 2: // BILL PAYMENT
                    if (actorBal >= amount) {
                        updateBalance(actorAcc, actorBal - amount);
                        Transaction tPay = new Transaction(generateTxId(), actorAcc.getAccountId(), "SERVICE_PROV", amount, date, "15:45", "Bill Payment", "Payment");
                        actorAcc.addTransaction(tPay);
                        saveChanges(actor, actorAcc);
                        view.appendLog(String.format("   [BILL] %s: -€%.2f", actorAcc.getAccountId(), amount));
                        totalVolume += amount;
                    }
                    break;

                case 3: // TRANSFER
                    Customer target = mapToCustomer(allRecords.get(rand.nextInt(allRecords.size())));
                    if(target.getAccounts().isEmpty()) return;
                    Account targetAcc = target.getAccounts().get(0);

                    if (!actorAcc.getAccountId().equals(targetAcc.getAccountId()) && actorBal >= amount) {
                        double targetBal = parseBalance(targetAcc.getBalance());
                        
                        updateBalance(actorAcc, actorBal - amount);
                        Transaction tSent = new Transaction(generateTxId(), actorAcc.getAccountId(), targetAcc.getAccountId(), amount, date, "18:00", "Transfer to " + target.getUsername(), "Transfer");
                        actorAcc.addTransaction(tSent);
                        
                        updateBalance(targetAcc, targetBal + amount);
                        Transaction tRec = new Transaction(generateTxId(), actorAcc.getAccountId(), targetAcc.getAccountId(), amount, date, "18:00", "Received from " + actor.getUsername(), "Transfer");
                        targetAcc.addTransaction(tRec);

                        saveChanges(actor, actorAcc);
                        saveChanges(target, targetAcc);

                        view.appendLog(String.format("   [TRANSFER] %s -> %s: €%.2f", actorAcc.getAccountId(), targetAcc.getAccountId(), amount));
                        totalVolume += amount;
                    }
                    break;
            }
            totalActions++;
        } catch (Exception e) {}
    }

    // =============================================================
    // DATABASE HELPERS
    // =============================================================

    public void saveChanges(Customer user, Account acct){
        this.model.get_uDB().updateUserRecord(convertUserToMap(user, user.getAccounts()));
        this.model.get_tDB().appendTransactionRecord(convertAccountToTransactionMap(acct));
    }

    private double parseBalance(String bal) {
        if(bal == null || bal.isEmpty()) return 0.0;
        try { return Double.parseDouble(bal.replace(",", ".")); } catch (Exception e) { return 0.0; }
    }
    
    private void updateBalance(Account acct, double newAmount) {
        acct.setBalance(String.format(Locale.US, "%.2f", newAmount));
    }
    
    private String generateTxId() {
        return String.valueOf(System.currentTimeMillis() + new Random().nextInt(1000));
    }

    // --- MAPPERS ---
    private Customer mapToCustomer(Map<String, Object> wrapper) {
        Map<String, Object> uData = (Map<String, Object>) wrapper.get("user");
        String type = (String) uData.get("type");
        Customer customer;
        if ("Company".equalsIgnoreCase(type)) {
            Company c = new Company(); c.setCompanyName((String) uData.get("companyName")); customer = c;
        } else {
            Individual i = new Individual(); i.setFirstName((String) uData.get("name")); i.setLastName((String) uData.get("surname")); customer = i;
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
                String owner = (am.get("ownerName") != null) ? am.get("ownerName") : am.get("primaryOwnerId"); 
                a.setPrimaryOwnerId(owner); 
                a.setBalance(am.get("balance"));
                a.setSecondaryOwner(am.get("secondaryOwner"));
                if(am.containsKey("rfCode")) a.setRfCode(am.get("rfCode"));
                customer.addAccount(a);
            }
        }
        return customer;
    }

    private Map<String, Object> convertUserToMap(Customer user, List<Account> accounts) {
        Map<String, Object> uMap = new HashMap<>();
        uMap.put("username", user.getUsername());
        uMap.put("password", user.getPassword());
        uMap.put("email", user.getEmail());
        uMap.put("phone", user.getPhoneNumber());
        uMap.put("taxId", user.getTaxId());
        
        String type = (user instanceof Company) ? "Company" : "Individual";
        uMap.put("type", type);
        
        if (user instanceof Company) {
            uMap.put("companyName", ((Company) user).getCompanyName());
        } else if (user instanceof Individual) {
            uMap.put("name", ((Individual) user).getFirstName());
            uMap.put("surname", ((Individual) user).getLastName());
        }

        List<Map<String, String>> accList = new ArrayList<>();
        if (accounts != null) {
            for (Account a : accounts) {
                Map<String, String> aMap = new HashMap<>();
                aMap.put("accountId", a.getAccountId());
                aMap.put("iban", a.getIban());
                aMap.put("ownerName", a.getOwnerName());
                aMap.put("balance", a.getBalance());
                aMap.put("secondaryOwner", a.getSecondaryOwner());
                if (a.getRfCode() != null) aMap.put("rfCode", a.getRfCode());
                aMap.put("interestRate", (a.getInterestRate() != null) ? a.getInterestRate() : "3.0");
                accList.add(aMap);
            }
        }
        uMap.put("accounts", accList);
        
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("user", uMap);
        return wrapper;
    }

    private Map<String, Object> convertAccountToTransactionMap(Account acct) {
        Map<String, Object> aMap = new HashMap<>();
        aMap.put("accountId", acct.getAccountId());
        
        List<Map<String, String>> tList = new ArrayList<>();
        if (acct.getTransactions() != null) {
            for (Transaction t : acct.getTransactions()) {
                Map<String, String> tMap = new HashMap<>();
                tMap.put("transactionId", t.getTransactionId());
                tMap.put("senderId", t.getSenderId());
                tMap.put("recieverId", t.getRecieverId());
                tMap.put("amount", String.format(Locale.US, "%.2f", t.getAmount()));
                tMap.put("date", t.getDate());
                tMap.put("time", t.getTime());
                tMap.put("description", t.getDescription());
                tMap.put("type", t.getType());
                tList.add(tMap);
            }
        }
        aMap.put("transactions", tList);
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("account", aMap);
        return wrapper;
    }
}