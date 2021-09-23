package main.java.controller;

import main.java.model.Model;
import main.java.model.PingTask;
import main.java.view.MainView;
import main.java.view.ServersView;
import main.java.view.StatisticView;

import java.util.Objects;

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

    public void onClickPingBtn(boolean isSelected){
        if (model.getDomain() == null || model.getDomain().equals(""))
            return;
        if (isSelected) {
            model.pingTest();
            mainView.hideToTray();
        }
        else
            model.stopPing();
        //model.speedTest();
    }

    public void onClickTrayExit(){
        model.stopPing();
        mainView.removeFromTray();
    }

    public void setMainView(MainView mainView){
        this.mainView = mainView;
    }
}
