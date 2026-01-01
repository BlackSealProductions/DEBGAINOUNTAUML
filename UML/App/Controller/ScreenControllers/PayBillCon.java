package App.Controller.ScreenControllers;


import App.Controller.Controller_t; 
import App.Model.Database.JsonDatabase;
import App.Model.Entities.UserEntities.Account;
import App.Model.ModelHandler;
import App.Model.Session;
import App.View.Screens.DashboardScreen;
import App.View.Screens.LoginScreen;
import App.View.Screens.RegisterIndividualScreen;
import App.View.ViewHandler; // IMPORT THIS
import App.View.ViewSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;



public class PayBillCon implements Controller_t {

    private LoginScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;
    

          // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public PayBillCon(LoginScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
    
    }

        @Override
        public void init(){



        }
}
