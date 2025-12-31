package App.Controller.ScreenControllers;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.TypeSelectionScreen;
import App.View.Screens.FirstPageScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TypeSelectionCon implements Controller_t{
    private TypeSelectionScreen view;
    private ModelHandler model; 
    
    private ViewHandler viewHandler;

    public TypeSelectionCon(TypeSelectionScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler;
    }
    @Override
    public void init() {
        
        view.getIndividualBtn().addActionListener(e -> handleIndividual());
        view.getCompanyBtn().addActionListener(e -> handleCompany());

    }

    private void handleIndividual(){

        view.hide();
        RegisterIndividualScreen next = viewHandler.getRegisterScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);

    }

    private void handleCompany(){

        view.hide();
        RegisterCompanyScreen next = viewHandler.getRegisterCompanyScreen();
        next.show();
        ViewSession.getInstance().updateScreenHistory(next);
    }
}
