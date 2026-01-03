package App.Controller.ScreenControllers;

import javax.swing.JOptionPane;

import App.Controller.Controller_t;
import App.Model.ModelHandler;
import App.Model.Session;
import App.Model.Database.OrderDB;
import App.Model.Entities.OperationEntities.StandingOrder;
import App.Model.Entities.OperationEntities.Transaction;
import App.View.ViewHandler;
import App.View.Screens.StandingOrdersScreen;

public class StandingOrdersCon implements Controller_t{

    StandingOrdersScreen view;
    ModelHandler model;
    ViewHandler viewHandler;

    public StandingOrdersCon(StandingOrdersScreen view, ModelHandler model, ViewHandler viewHandler){
        this.view=view;
        this.model=model;
        this.viewHandler=viewHandler;
    }

    @Override
    public void init() {
        view.getCompleteBtn().addActionListener(e -> handleOrderCreation());
    }

    public void handleOrderCreation(){
        String name = view.getName();
        String acc = view.getIban();
        String date = view.getDate();
        String freq = view.getFreqBox().getSelectedItem().toString();
        Double amount = 0.0;


        if(!view.validateInputs()){return;}
        try {
            amount = Double.parseDouble(view.getAmount());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
        }
        String transID = String.valueOf(System.currentTimeMillis());
        String orderID = String.valueOf(System.currentTimeMillis()+1);

        //Transaction tr = new Transaction(transID, Session.getInstance().getActiveAccount().getAccountId(), acc, amount, date, date, "Standing order", "send");
        StandingOrder order = new StandingOrder(name, acc, orderID, amount, date, freq);
        order.calcNextDate(freq, date);
        Session.getInstance().getActiveAccount().addOrder(order);
        

        System.out.println(Session.getInstance().getActiveAccount().getStandingorders());

        model.saveChangesToODB_conv();

        if(Session.getInstance().getActiveAccount().getStandingorders().isEmpty()){
            view.resetRowCounter();
        }
        String[] dataForList = {order.getName(), order.getAccountIban(), order.getPresentDay(), order.getPaymentFrequency(), String.valueOf(order.getAmount())};
        view.addListRow2(dataForList);

    }

}