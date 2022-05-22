package main.java.model;

import main.java.utils.FileUtils;
import main.java.utils.PropertiesUtils;

import java.util.Properties;

/**
 * Модель основного окна приложения
 */
public class Model {
    /**
     * Сервер, на который будут отправляться ping запросы
     */
    private String serverAddress;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Получить выбранный сервер, если сервер не выбран, то загружается из {@code config.properties}
     */
    public String getServerAddress() {
        if (serverAddress == null || serverAddress.isEmpty()) {
            Properties properties = PropertiesUtils.loadProperties(FileUtils.getFileFromResources("/config.properties"));
            serverAddress = properties.getProperty("serverAddress");
        }
        return serverAddress;
    }
}
