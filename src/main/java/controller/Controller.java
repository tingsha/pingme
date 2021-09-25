package main.java.controller;

import main.java.model.Model;
import main.java.view.MainView;
import main.java.view.ServersView;
import main.java.view.StatisticView;

public class Controller {
    private final Model model;
    private MainView mainView;
    private StatisticView statisticView;

    public Controller(Model model) {
        this.model = model;
    }

    public void onClickServerBtn(ServersView.ServerButton button){
        model.setDomain(button.getDomain());
    }

    public void onSelectPingBtn(){
        if (model.getDomain() == null || model.getDomain().equals(""))
            return;
        model.pingTest();
        mainView.hideToTray();
        //model.speedTest();
    }

    public void onDeselectPingBtn(){
        if (model.getDomain() == null || model.getDomain().equals(""))
            return;
        model.stopPingTask();
    }

    public void onClickTrayExit(){
        model.stopPingTask();
        mainView.removeFromTray();
    }

    public void setMainView(MainView mainView){
        this.mainView = mainView;
    }
}
