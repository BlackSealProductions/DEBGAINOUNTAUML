package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.FirstPageScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterScreen;

public class FirstPageCon implements Controller_t {
    private FirstPageScreen view;
    private ModelHandler model; 
    
    private ViewHandler viewHandler;

    public FirstPageCon(FirstPageScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }

    @Override
    public void init() {
        
        view.getLoginBut().addActionListener(e -> handleLogin());
        view.getRegisterBut().addActionListener(e -> handleRegistration());

    }

    private void handleLogin(){

        view.hide();
        LoginScreen next = viewHandler.getLoginScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);

    }

    private void handleRegistration(){

        view.hide();
        RegisterScreen next = viewHandler.getRegisterScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }

    
}
