package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.RoundedButton;
import Utils.GlobalConsts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AuditLogScreen implements View_t {

    private JPanel panel;
    
    // Public fields
    public JTextField searchField;
    public RoundedButton refreshBtn;
    public JTable logTable; 
    public RoundedButton backBtn;

    public AuditLogScreen() {
    }

    @Override
    public void init() {
        // Main Panel Setup
        panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Color.decode("#C2E5FF")); 
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- 1. Top Bar (Title & Back) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        backBtn = new RoundedButton("Back", 15);
        backBtn.setBackground(Color.decode("#D82F4B"));
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(80, 40));
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel titleLabel = new JLabel("Audit Transaction Logs", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);

        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(Box.createHorizontalStrut(80), BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        // --- 2. Action Bar (Search & Refresh) ---
        JPanel actionPanel = new JPanel(new BorderLayout(15, 0));
        actionPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));

        refreshBtn = new RoundedButton("Refresh Logs", 15);
        refreshBtn.setBackground(Color.decode("#D82F4B"));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setPreferredSize(new Dimension(150, 40));
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        RoundedButton searchBtn = new RoundedButton("Search", 15);
        searchBtn.setBackground(Color.decode("#D82F4B"));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setPreferredSize(new Dimension(100, 40));
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel searchContainer = new JPanel(new BorderLayout(10, 0));
        searchContainer.setOpaque(false);
        searchContainer.add(searchField, BorderLayout.CENTER);
        searchContainer.add(searchBtn, BorderLayout.EAST);

        actionPanel.add(searchContainer, BorderLayout.CENTER);
        actionPanel.add(refreshBtn, BorderLayout.EAST);

        panel.add(actionPanel, BorderLayout.CENTER);

        // --- 3. Table Section ---
        String[] columns = {"Date & Time", "Tx ID", "Type", "From Account", "To Account", "Amount (€)", "Status"};
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        logTable = new JTable(model);
        logTable.setRowHeight(30);
        logTable.setFont(new Font("Arial", Font.PLAIN, 14));
        logTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        logTable.getTableHeader().setBackground(Color.decode("#333333"));
        logTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);
        tableContainer.add(actionPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);

        panel.add(tableContainer, BorderLayout.CENTER);

    }

    @Override
    public JPanel getMainPanel() {
        return panel;
    }

    @Override
    public void show() {
        panel.setVisible(true);
    }

    @Override
    public void hide() {
        panel.setVisible(false);
    }

    // --- ADDED GETTER FOR BACK BUTTON ---
    public RoundedButton getBackBtn() {
        return backBtn;
    }
}