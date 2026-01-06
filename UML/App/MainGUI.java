package App;

import App.Controller.ControllerHandler;
import App.Model.ModelHandler;
import App.View.ViewHandler;

public class MainGUI {

    public static void main(String[] args){
        ViewHandler v = new ViewHandler();
        ModelHandler m = new ModelHandler();
        ControllerHandler c = new ControllerHandler(v, m);

        c.initModel();
        c.initViews();
        c.initControllers();
        c.initStartScreen();
        m.ass.assess();

        
        
    }
    
}
