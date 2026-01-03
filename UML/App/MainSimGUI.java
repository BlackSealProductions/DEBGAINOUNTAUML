package App;

import App.Controller.ScreenControllers.SimulationCon;
import App.View.Screens.SimulationScreen;
import javax.swing.SwingUtilities;

public class MainSimGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            // 1. Create View
            System.out.println("Building GUI...");
            SimulationScreen view = new SimulationScreen();
            
            // 2. Create Controller (Pass ONLY the view)
            System.out.println("Starting Controller...");
            SimulationCon controller = new SimulationCon(view);
            controller.init();

            // 3. Show Window
            System.out.println("Opening Window...");
            view.getFrame().setVisible(true); 
        });
    }
}