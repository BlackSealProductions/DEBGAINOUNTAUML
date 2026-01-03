package App.Controller.ScreenControllers;

import App.Controller.ControllerHandler;
import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Entities.UserEntities.Account;
import App.View.ViewHandler;
import App.View.ViewSession;
import App.View.Screens.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountManagementCon implements Controller_t{

    private AccountManagementScreen view;
    private ModelHandler model; 
    private ViewHandler viewHandler;

    // --- 2. UPDATE CONSTRUCTOR TO RECEIVE IT ---
    public AccountManagementCon(AccountManagementScreen view, ModelHandler model, ViewHandler viewHandler) {
        this.view = view;
        this.model = model;
        this.viewHandler = viewHandler; // Save it!
    }

    @Override
    public void init() {
        if (view == null)return;
        
        view.getChangeOwnerBtn().addActionListener(e -> {
            String newOwner = view.showInputPopup("Εισάγετε όνομα συνδικαιούχου:");

            if (newOwner != null && !newOwner.trim().isEmpty()) {
                view.setSecOwner(newOwner);
                Account user = Session.getInstance().getActiveAccount();
                user.setSecondaryOwner(newOwner);
                model.saveChangesToUDB_conv();
                // TODO: Update your Database/Model here
            }
        });
    }
}