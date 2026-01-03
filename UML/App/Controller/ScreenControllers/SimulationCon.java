package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.View.Screens.SimulationScreen;
import javax.swing.SwingUtilities;

public class SimulationCon implements Controller_t {

    private SimulationScreen view;

    // CONSTRUCTOR: No ModelHandler, No ViewHandler. Just the View.
    public SimulationCon(SimulationScreen view) {
        this.view = view;
    }

    @Override
    public void init() {
        // 1. Run Button Logic
        view.getRunButton().addActionListener(e -> startSimulation());

        // 2. Back Button Logic
        view.getBackButton().addActionListener(e -> {
            System.out.println("Back clicked: Closing Simulation.");
            view.getFrame().dispose();
        });
    }

    private void startSimulation() {
        view.getRunButton().setEnabled(false);
        view.getRunButton().setText("RUNNING...");

        // Run in background thread
        new Thread(() -> {
            try {
                // Get inputs from the GUI
                int bots = Integer.parseInt(view.getBotCount());
                int actions = Integer.parseInt(view.getActionsCount());

                System.out.println(">>> STARTING SIMULATION (" + bots + " Bots)...");

                // --- DUMMY LOGIC (Since we have no ModelHandler) ---
                for (int i = 0; i < actions; i++) {
                    // Fake work
                    Thread.sleep(800); 
                    
                    // Log to the GUI (via System.out)
                    System.out.println("Log " + (i+1) + ": Bot_" + (i%bots) + " processed transaction.");
                    
                    // Update the stats on the GUI
                    final int currentStep = i + 1;
                    SwingUtilities.invokeLater(() -> {
                        view.setTotalTx(String.valueOf(currentStep));
                        view.setVolume(currentStep * 50 + ".00");
                    });
                }

                System.out.println(">>> SIMULATION COMPLETE.");

            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            } finally {
                SwingUtilities.invokeLater(() -> {
                    view.getRunButton().setEnabled(true);
                    view.getRunButton().setText("RUN SIMULATION");
                });
            }
        }).start();
    }
}