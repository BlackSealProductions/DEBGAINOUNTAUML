package App.Controller.ScreenControllers;

import App.Model.ModelHandler;
import App.Model.Simulator;
import App.View.Screens.SimulationScreen;

public class SimulationCon {

    private SimulationScreen view;
    private ModelHandler model;

    public SimulationCon(SimulationScreen view, ModelHandler model) {
        this.view = view;
        this.model = model;
    }

    public void init() {
        Simulator simulator = new Simulator(model, view);

        view.runBtn.addActionListener(e -> {
            try {
                String start = view.startDateField.getText();
                int days = Integer.parseInt(view.daysField.getText());
                
                // Start the Time-Step Simulation
                simulator.startSimulation(start, days);
                
            } catch (NumberFormatException ex) {
                view.appendLog("ERROR: Duration must be a number.");
            } catch (Exception ex) {
                view.appendLog("ERROR: " + ex.getMessage());
            }
        });
        
        // Note: Back button logic is usually handled by AdminMenuCon
    }
}