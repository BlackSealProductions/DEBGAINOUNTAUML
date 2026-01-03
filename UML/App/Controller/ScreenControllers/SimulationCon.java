package App.Controller.ScreenControllers;

import App.View.Screens.SimulationScreen;
import App.Model.ModelHandler;
import App.Model.Simulator; // Import the new class

public class SimulationCon {

    private SimulationScreen view;
    private ModelHandler model;

    // Update Constructor to accept ModelHandler
    public SimulationCon(SimulationScreen view, ModelHandler model) {
        this.view = view;
        this.model = model;
    }

    public void init() {
        view.getRunButton().addActionListener(e -> runSimulation());
    }

    private void runSimulation() {
        try {
            // Get inputs from the GUI
            int botCount = Integer.parseInt(view.getBotCount());
            int actions = Integer.parseInt(view.getActionsCount());
            
            // Start the Simulator
            Simulator sim = new Simulator(model, view);
            sim.startSimulation(botCount, actions);

        } catch (NumberFormatException ex) {
            view.appendLog("ERROR: Invalid configuration. Please enter numbers for Bots/Actions.");
        }
    }
}