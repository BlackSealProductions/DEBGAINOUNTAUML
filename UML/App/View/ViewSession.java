package App.View;

import java.awt.List;
import java.util.ArrayDeque;
import java.util.Deque;

import App.Model.Entities.UserEntities.Account;
import App.View.Screens.FirstPageScreen;

public class ViewSession {

    private static ViewSession instance;

    // Data to keep in memory
    private View_t curr=null;
    private Deque<View_t> screen_history = new ArrayDeque<>();

    private ViewSession() {}

    public static ViewSession getInstance() {
        if (instance == null) {
            instance = new ViewSession();
        }
        return instance;
    }

    public View_t getCurrentScreen(){
        return curr;
    }

    public View_t getPreviousScreen(){
        return screen_history.peek();
    }

    public void setCurrentScreen(View_t screen){
        curr = screen;
    }

    public boolean isHistoryEmpty(){

        return this.screen_history.isEmpty();
    }          
    
    public void updateScreenHistory(View_t nextScreen){

        screen_history.push(curr);
        curr = nextScreen;
        // System.out.println("\ncurr:" + curr + " prevs:" + screen_history);
    }

    public void goBack(){

        if(!screen_history.isEmpty()){

            curr = screen_history.pop();
            // System.out.println("\ncurr:" + curr + " prevs:" + screen_history);
        }

    }

    public void clearHistory(){
        screen_history.clear();
        // System.out.println("\ncurr:" + curr + " prevs:" + screen_history);

    }

}