package main.java.controller;

import main.java.model.Model;
import main.java.service.NetworkService;
import main.java.view.MainView;
import main.java.view.StatisticView;

/**
 * Основной контроллер приложения
 */
public class Controller {
    /**
     * Главное окно
     */
    private MainView mainView;
    /**
     * Модель главного окна
     */
    private final Model model;

    private NetworkService networkService;

    public Controller(Model model) {
        this.model = model;
    }

    /**
     * Установить в модели сервер, на который будут отправляться ping запросы
     */
    public void onClickServerBtn(String serverAddress) {
        model.setServerAddress(serverAddress);
    }

    /**
     * Запускает тестирование задержки и скорости сети
     */
    public void onSelectStartBtn() {
        if (model.getServerAddress() == null || model.getServerAddress().isEmpty()) {
            //TODO вызвать окно с предупреждением
            return;
        }
        mainView.hideToTray();
        StatisticView.getInstance().setVisible(true);

        networkService = new NetworkService();
        networkService.startMonitoring(model.getServerAddress());
        networkService.refreshView();
    }

    /**
     * Прекращает тестирование задержки и скорости сети
     */
    public void onDeselectPingBtn() {
        networkService.stopMonitoring();
        StatisticView.getInstance().updateValues("0", 0, 0);
        mainView.removeFromTray();
    }

    /**
     * Завершить все процессы и выйти из программы
     */
    public void onClickExit() {
        networkService.stopMonitoring();
        mainView.removeFromTray();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }
}
