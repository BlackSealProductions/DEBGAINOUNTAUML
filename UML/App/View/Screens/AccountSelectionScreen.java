package App.View.Screens;

import App.View.View_t;
import App.View.helper_classes.RoundedButton;
import App.View.helper_classes.AccountCellRenderer; // Import your new class
import App.View.helper_classes.FontLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AccountSelectionScreen implements View_t {

    String fontPath = "/App/Fonts/RobotoMono-Bold.ttf";
    Font customFont60 = FontLoader.loadCustomFont(fontPath, 60f);
    Font customFont40 = FontLoader.loadCustomFont(fontPath, 40f);
    Font customFont20 = FontLoader.loadCustomFont(fontPath, 20f);

    private JPanel panel = new JPanel();
    private Color blue = Color.decode("#C2E5FF");
    
    public DefaultListModel<String> listModel = new DefaultListModel<>();
    public JList<String> accountList = new JList<>(listModel);
    public RoundedButton selectBtn = new RoundedButton("Επιλογή Λογαριασμού", 15);

    final int wWidth = Utils.GlobalConsts.wWidth;
    final int wHeight = Utils.GlobalConsts.wHeight;

    @Override
    public void init() {
        panel.setLayout(null);
        panel.setBackground(blue);
        panel.setBounds(0, 0, wWidth, wHeight);

        JLabel title = new JLabel("Επιλέξτε Λογαριασμό", SwingConstants.CENTER);
        title.setFont(customFont40);
        title.setBounds(0, 80, wWidth, 60);
        panel.add(title);

        // --- Styled List Setup ---
        accountList.setOpaque(false);
        accountList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Use the new external renderer class
        accountList.setCellRenderer(new AccountCellRenderer());
        accountList.setFixedCellHeight(90); // Height of each "card" including padding

        JScrollPane scrollPane = new JScrollPane(accountList);
        scrollPane.setBounds((wWidth - 700) / 2, 160, 700, 450);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        
        // Hide scrollbar but keep functionality
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(scrollPane);

        selectBtn.setBounds((wWidth - 320) / 2, 650, 320, 65);
        selectBtn.setBackground(Color.decode("#D82F4B"));
        selectBtn.setForeground(Color.white);
        selectBtn.setFont(customFont20);
        panel.add(selectBtn);
        
        hide();
    }

    public void populateAccounts(List<Map<String, String>> accounts) {
        listModel.clear();
        if (accounts == null || accounts.isEmpty()) {
            listModel.addElement("No accounts found");
            selectBtn.setEnabled(false);
            return;
        }
        
        selectBtn.setEnabled(true);
        for (Map<String, String> acc : accounts) {
            // Passing the ID; the renderer will format it
            listModel.addElement(acc.get("accountId"));
        }
    }

    @Override public void show() { panel.setVisible(true); }
    @Override public void hide() { panel.setVisible(false); }
    @Override public JPanel getMainPanel() { return panel; }
}