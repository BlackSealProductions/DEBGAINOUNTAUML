package App.Controller;

public abstract class Controller_t {

    // 1. This forces all controllers (Login, Main, etc.) to have an init function.
    // This fixes the error: "The method init() is undefined"
    public abstract void init();

    // 2. (Optional) Keeping your template method if you plan to use it later
    // public abstract App.View.View_t getViewTemplate();
}