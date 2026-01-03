package App.Controller.ScreenControllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import App.Model.ModelHandler;
import App.View.Screens.AdminMenuScreen;
import App.View.Screens.SimulationScreen;
import App.View.Screens.AuditLogScreen;
import App.View.Screens.EditDataScreen;

public class AdminMenuCon {

    private AdminMenuScreen view;
    private ModelHandler model;
    private JFrame mainFrame; // We will capture the main window here

    public AdminMenuCon(AdminMenuScreen view, ModelHandler model) {
        this.view = view;
        this.model = model;
    }

    public void init() {
        view.getSimulationBtn().addActionListener(e -> openSimulation());
        view.getAuditBtn().addActionListener(e -> openAuditLogs());
        view.getEditDataBtn().addActionListener(e -> openEditData());
        view.getLogoutBtn().addActionListener(e -> handleLogout());
    }

    private void openSimulation() {
        JFrame frame = getFrame(); 

        SimulationScreen simView = new SimulationScreen();
        simView.init(); // Init View FIRST

        // PASS THE MODEL HERE
        SimulationCon simCon = new SimulationCon(simView, this.model); 
        simCon.init();

        frame.setContentPane(simView.getMainPanel());
        frame.revalidate();
        frame.repaint();

        simView.getBackButton().addActionListener(back -> {
            frame.setContentPane(view.getMainPanel()); 
            frame.revalidate();
            frame.repaint();
        });
    }

    // --- 2. AUDIT LOGS SCREEN ---
    private void openAuditLogs() {
        JFrame frame = getFrame();

        AuditLogScreen auditView = new AuditLogScreen();
        auditView.init();
        
        AuditLogCon auditCon = new AuditLogCon(auditView, this.model); 
        auditCon.init();

        // SWAP
        frame.setContentPane(auditView.getMainPanel());
        frame.revalidate();
        frame.repaint();
        
        // BACK BUTTON
        auditView.getBackBtn().addActionListener(back -> {
            frame.setContentPane(view.getMainPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    private void openEditData() {
        JFrame frame = getFrame();

        // 1. Create View
        EditDataScreen editView = new EditDataScreen();
        editView.init();

        // 2. Create Controller (PASS THE MODEL!)
        EditDataCon editCon = new EditDataCon(editView, this.model);
        
        // 3. CRITICAL: START THE CONTROLLER
        editCon.init();  // <--- IF THIS IS MISSING, YOU SEE DUMMY DATA

        // 4. Swap Screen
        frame.setContentPane(editView.getMainPanel());
        frame.revalidate();
        frame.repaint();

        // 5. Back Button
        editView.getBackBtn().addActionListener(back -> {
            frame.setContentPane(view.getMainPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    private void handleLogout() {
        getFrame().dispose(); 
        System.out.println("Logged out.");
        // If you want to reopen Login here, you'd need the ViewHandler logic
    }

    // --- HELPER: Get the Main Window ---
    private JFrame getFrame() {
        if (mainFrame == null) {
            // Find the window that holds our Admin View
            mainFrame = (JFrame) SwingUtilities.getWindowAncestor(view.getMainPanel());
        }
        return mainFrame;
    }
}