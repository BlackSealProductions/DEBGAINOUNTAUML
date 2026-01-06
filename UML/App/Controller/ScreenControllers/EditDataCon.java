package App.Controller.ScreenControllers;

import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import App.View.Screens.EditDataScreen;
import App.Model.ModelHandler;

public class EditDataCon {

    private EditDataScreen view;
    private ModelHandler model;

    public EditDataCon(EditDataScreen view, ModelHandler model) {
        this.view = view;
        this.model = model;
    }

    public void init() {
        refreshTables();

        // ----------------------------------------------------
        // TAB 1: USER MANAGEMENT
        // ----------------------------------------------------
        view.getDeleteUserBtn().addActionListener(e -> {
            int row = view.getUserTable().getSelectedRow();
            if (row >= 0) {
                String username = (String) view.getUserModel().getValueAt(row, 1);
                int confirm = JOptionPane.showConfirmDialog(null, "Delete user " + username + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.get_uDB().deleteUser(username);
                    refreshTables();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select a user first.");
            }
        });

        view.getEditUserBtn().addActionListener(e -> {
            int row = view.getUserTable().getSelectedRow();
            if (row >= 0) {
                String username = (String) view.getUserModel().getValueAt(row, 1);
                String newPass = JOptionPane.showInputDialog("New password for " + username + ":");
                if (newPass != null && !newPass.isEmpty()) {
                    model.get_uDB().updateUserPassword(username, newPass);
                    JOptionPane.showMessageDialog(null, "Password updated.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select a user first.");
            }
        });

        // ----------------------------------------------------
        // TAB 2: ACCOUNT MANAGEMENT
        // ----------------------------------------------------
        view.getDeleteAccountBtn().addActionListener(e -> {
            int row = view.getAccountTable().getSelectedRow();
            if (row >= 0) {
                String accId = (String) view.getAccountModel().getValueAt(row, 2);
                int confirm = JOptionPane.showConfirmDialog(null, "Delete account " + accId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.get_uDB().deleteAccount(accId);
                    refreshTables();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select an account first.");
            }
        });

        view.getEditBalanceBtn().addActionListener(e -> {
            int row = view.getAccountTable().getSelectedRow();
            if (row >= 0) {
                String accId = (String) view.getAccountModel().getValueAt(row, 2);
                String oldBal = (String) view.getAccountModel().getValueAt(row, 3);
                String newBal = JOptionPane.showInputDialog("New Balance:", oldBal);
                if (newBal != null) {
                    try {
                        Double.parseDouble(newBal); // Validate number
                        model.get_uDB().updateAccountBalance(accId, newBal);
                        refreshTables();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select an account first.");
            }
        });

        // [NEW] Logic for Interest Rate Editing
        view.getEditInterestBtn().addActionListener(e -> {
            int row = view.getAccountTable().getSelectedRow();
            if (row >= 0) {
                String accId = (String) view.getAccountModel().getValueAt(row, 2);
                String oldRate = (String) view.getAccountModel().getValueAt(row, 4); // Column 4 is Interest
                String newRate = JOptionPane.showInputDialog("New Interest Rate (%):", oldRate);
                
                if (newRate != null) {
                    try {
                        Double.parseDouble(newRate); // Validate it's a number
                        model.get_uDB().updateAccountInterest(accId, newRate);
                        refreshTables();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number (e.g. use 3.0 for 3%)");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select an account first.");
            }
        });
    }

    private void refreshTables() {
        populateUserTable();
        populateAccountTable();
    }

    private void populateUserTable() {
        DefaultTableModel modelUser = view.getUserModel();
        modelUser.setRowCount(0); 

        List<Map<String, Object>> allRecords = model.get_uDB().getAllRecords();
        for (Map<String, Object> wrapper : allRecords) {
            Map<String, Object> u = (Map<String, Object>) wrapper.get("user");
            modelUser.addRow(new Object[]{
                u.get("taxId"), u.get("username"), u.get("type"), u.get("email"), u.get("phone")
            });
        }
    }

    private void populateAccountTable() {
        DefaultTableModel modelAcc = view.getAccountModel();
        modelAcc.setRowCount(0); 

        List<Map<String, Object>> allRecords = model.get_uDB().getAllRecords();
        for (Map<String, Object> wrapper : allRecords) {
            Map<String, Object> u = (Map<String, Object>) wrapper.get("user");
            List<Map<String, String>> accounts = (List<Map<String, String>>) u.get("accounts");
            if (accounts != null) {
                for (Map<String, String> acc : accounts) {
                    modelAcc.addRow(new Object[]{
                        acc.get("iban"), 
                        acc.get("ownerName"), 
                        acc.get("accountId"), 
                        acc.get("balance"), 
                        acc.get("interestRate") // This goes into the last column
                    });
                }
            }
        }
    }
}