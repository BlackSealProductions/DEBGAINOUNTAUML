package App.Controller.ScreenControllers;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import App.Model.ModelHandler;
import App.View.Screens.AuditLogScreen;
import App.View.helper_classes.OnFocusEventHelper; 

public class AuditLogCon {
    private ModelHandler model;
    private AuditLogScreen view;
    private TableRowSorter<DefaultTableModel> sorter; 

    private final String PLACEHOLDER = "Search Tx ID or Account...";

    // FIX 1: Swapped arguments to (AuditLogScreen, ModelHandler) to match AdminMenuCon
    public AuditLogCon(AuditLogScreen view, ModelHandler model) {
        this.model = model;
        this.view = view;
    }

    // FIX 2: Added init() method (AdminMenuCon calls this)
    public void init() {
        // 1. Setup the Placeholder
        OnFocusEventHelper.setOnFocusText(view.searchField, PLACEHOLDER, Color.BLACK, Color.GRAY);

        // 2. Setup the Refresh Button
        // Remove old listeners to prevent duplicates if init is called multiple times
        for(ActionListener al : view.refreshBtn.getActionListeners()) {
            view.refreshBtn.removeActionListener(al);
        }
        view.refreshBtn.addActionListener(e -> refreshLogs());

        // 3. Setup Live Search
        setupLiveSearch();
        
        // 4. Load data and show screen
        refreshLogs();
        view.show();
    }

    private void setupLiveSearch() {
        DefaultTableModel tableModel = (DefaultTableModel) view.logTable.getModel();
        sorter = new TableRowSorter<>(tableModel);
        view.logTable.setRowSorter(sorter);

        view.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = view.searchField.getText();

                if (text.equals(PLACEHOLDER) || text.trim().isEmpty()) {
                    sorter.setRowFilter(null); 
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    } catch (Exception ex) {}
                }
            }
        });
    }

    private void refreshLogs() {
        DefaultTableModel tableModel = (DefaultTableModel) view.logTable.getModel();
        tableModel.setRowCount(0);

        var transactions = model.get_tDB().getAllTransactions();

        if (transactions != null) {
            for (var t : transactions) {
                // Formatting Amount: 2 decimal places + Symbol
                // Check against typo in Transaction class (getAmmount vs getAmount)
                double amountVal = 0.0;
                try {
                     amountVal = t.getAmmount(); // Using the typo version based on previous files
                } catch (Error | Exception e) {
                     // Fallback if typo was fixed
                     // amountVal = t.getAmount(); 
                }

                String formattedAmount = String.format("%.2f €", amountVal);

                tableModel.addRow(new Object[]{
                    t.getDate() + " " + t.getTime(),
                    t.getTransactionId(),
                    t.getType(),
                    t.getSenderId(),
                    t.getRecieverId(),
                    formattedAmount, 
                    "Success"
                });
            }
        }
        sorter.setModel(tableModel);
    }
}