package App.Controller.ScreenControllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import App.Model.ModelHandler;
import App.View.ViewHandler;
import App.View.Screens.AdminMenuScreen;
import App.View.Screens.AuditLogScreen;
import App.View.Screens.EditDataScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.SimulationScreen;

public class AdminMenuCon {

    private AdminMenuScreen view;
    private ModelHandler model;
    private JFrame mainFrame; 

    public AdminMenuCon(AdminMenuScreen view, ModelHandler model) {
        this.view = view;
        this.model = model;
    }

    public void init() {
        // Listeners for the Main Menu Buttons
        view.getSimulationBtn().addActionListener(e -> openSimulation());
        view.getAuditBtn().addActionListener(e -> openAuditLogs());
        view.getEditDataBtn().addActionListener(e -> openEditData());
        view.getLogoutBtn().addActionListener(e -> handleLogout());
    }

    // --- 1. SIMULATION ---
    private void openSimulation() {
        JFrame frame = getFrame(); 

        SimulationScreen simView = new SimulationScreen();
        // [FIX]: Ensure init is called
        simView.init(); 

        SimulationCon simCon = new SimulationCon(simView, this.model); 
        simCon.init();

        frame.setContentPane(simView.getMainPanel());
        frame.revalidate();
        frame.repaint();

        // Use direct field access for the new SimulationScreen
        simView.backBtn.addActionListener(back -> {
            frame.setContentPane(view.getMainPanel()); 
            frame.revalidate();
            frame.repaint();
        });
    }

    // --- 2. AUDIT LOGS ---
    private void openAuditLogs() {
        JFrame frame = getFrame();

        AuditLogScreen auditView = new AuditLogScreen();
        // [FIX]: Ensure init is called
        auditView.init();
        
        AuditLogCon auditCon = new AuditLogCon(auditView, this.model); 
        auditCon.init();

        frame.setContentPane(auditView.getMainPanel());
        frame.revalidate();
        frame.repaint();
        
        auditView.getBackBtn().addActionListener(back -> {
            frame.setContentPane(view.getMainPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    // --- 3. EDIT DATA ---
    private void openEditData() {
        JFrame frame = getFrame();

        EditDataScreen editView = new EditDataScreen();
        // [FIX]: CRITICAL - Initialize the screen so userModel is created
        editView.init(); 

        // Now the controller can safely access the tables
        EditDataCon editCon = new EditDataCon(editView, this.model);
        editCon.init(); 

        frame.setContentPane(editView.getMainPanel());
        frame.revalidate();
        frame.repaint();

        editView.getBackBtn().addActionListener(back -> {
            frame.setContentPane(view.getMainPanel());
            frame.revalidate();
            frame.repaint();
        });
    }

    // --- 4. LOGOUT ---
    private void handleLogout() {
        JFrame frame = getFrame();
        
        LoginScreen loginView = new LoginScreen();
        // [FIX]: Ensure init is called
        loginView.init();
        
        frame.setContentPane(loginView.getMainPanel());
        frame.revalidate();
        frame.repaint();
        
        LoginCon loginCon = new LoginCon(loginView, this.model, new ViewHandler());
        loginCon.init();
        
        System.out.println("Logged out.");
    }

    private JFrame getFrame() {
        if (mainFrame == null) {
            mainFrame = (JFrame) SwingUtilities.getWindowAncestor(view.getMainPanel());
        }
        return mainFrame;
    }
}