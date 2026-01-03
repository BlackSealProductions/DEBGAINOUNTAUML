package App.Controller.ScreenControllers;

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
        Double amount = Double.parseDouble(view.getAmount());

        String transID = String.valueOf(System.currentTimeMillis());
        String orderID = String.valueOf(System.currentTimeMillis()+1);


        Transaction tr = new Transaction(transID, Session.getInstance().getActiveAccount().getAccountId(), acc, amount, date, date, "Standing order", "send");
        StandingOrder order = new StandingOrder(name, acc, tr, orderID, 2, date, freq);

        Session.getInstance().getActiveAccount().addOrder(order);

        model.addEntryToODB_conv(Session.getInstance().getActiveAccount());
        String[] dataForList = {order.getName(), order.getAccountIban(), order.getPresentDay(), order.getPaymentFrequency(), String.valueOf(order.getAmount())};
        view.addListRow2(dataForList);

    }

}