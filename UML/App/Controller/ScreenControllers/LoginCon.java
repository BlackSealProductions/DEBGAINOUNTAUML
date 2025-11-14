package App.Controller.ScreenControllers;

import java.awt.Color;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.View.View_t;
import App.View.Screens.LoginScreen;

public class LoginCon implements Controller_t {

    private View_t view;
    private ModelHandler model;
    private Color red = new Color(255,20,55);
    private Color blue = new Color(168, 237, 255);

    public LoginCon(View_t view, ModelHandler model){

        this.view=view;
        this.model=model;
        init();
    }

    private void init(){

        // ((LoginScreen)view).button3.addActionListener(e -> doSomething());
    }

    private void doSomething() {

        Color c = view.getMainPanel().getBackground();

        if(c.equals(red)){
            ((LoginScreen)view).changeColor(blue);
        }
    
        else if(c.equals(blue)){
            ((LoginScreen)view).changeColor(red);
        }   

        
    }

    public View_t getViewTemplate(){
        return view;
    }
    
}
