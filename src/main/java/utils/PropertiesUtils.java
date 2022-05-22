package main.java.utils;

import main.java.view.StatisticView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Утилита для работы с файлами .properties
 */
public class PropertiesUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private PropertiesUtils() {
    }

    /**
     * Вернуть все {@link Properties} из файла
     *
     * @param propertiesFile файл со свойствами
     */
    public static Properties loadProperties(File propertiesFile) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertiesFile)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Can't load settings " + e);
        }
        return properties;
    }

    /**
     * Переписать старые свойства на новые из {@code newProperties}
     *
     * @param newProperties свойства, которые необходимо записать
     */
    public static void rewriteProperties(File propertiesFile, Map<String, String> newProperties) {
        Properties properties = loadProperties(propertiesFile);
        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/config.properties")) {
            Properties propertiesToSave = new Properties();
            propertiesToSave.setProperty("red", properties.getProperty("red"));
            propertiesToSave.setProperty("green", properties.getProperty("green"));
            propertiesToSave.setProperty("blue", properties.getProperty("blue"));
            propertiesToSave.setProperty("alpha", properties.getProperty("alpha"));
            propertiesToSave.setProperty("download", properties.getProperty("download"));
            propertiesToSave.setProperty("upload", properties.getProperty("upload"));
            propertiesToSave.setProperty("size", properties.getProperty("size"));
            propertiesToSave.setProperty("units", properties.getProperty("units"));
            propertiesToSave.setProperty("labels", properties.getProperty("labels"));
            propertiesToSave.setProperty("serverAddress", properties.getProperty("serverAddress"));
            propertiesToSave.setProperty("lastServer", properties.getProperty("lastServer"));
            propertiesToSave.setProperty("userServerAddress", properties.getProperty("userServerAddress"));
            propertiesToSave.setProperty("locationX", properties.getProperty("locationX"));
            propertiesToSave.setProperty("locationY", properties.getProperty("locationY"));
            for (Map.Entry<String, String> entry : newProperties.entrySet()) {
                propertiesToSave.setProperty(entry.getKey(), entry.getValue());
            }
            propertiesToSave.store(outputStream, null);
        } catch (IOException e) {
            logger.error(String.format("Exception during rewrite properties in file %s. %s", propertiesFile.getAbsolutePath(), e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Вернуть свойства для окна статистики {@link StatisticView}
     */
    public static Map<String, Integer> getStatisticWindowProperties(File propertiesFile) {
        Properties properties = loadProperties(propertiesFile);
        int red = Integer.parseInt(properties.getProperty("red"));
        int green = Integer.parseInt(properties.getProperty("green"));
        int blue = Integer.parseInt(properties.getProperty("blue"));
        int alpha = Integer.parseInt(properties.getProperty("alpha"));
        int fontSize = Integer.parseInt(properties.getProperty("size"));
        int locationX = Integer.parseInt(properties.getProperty("locationX"));
        int locationY = Integer.parseInt(properties.getProperty("locationY"));
        return Map.of("red", red,
                "green", green,
                "blue", blue,
                "alpha", alpha,
                "fontSize", fontSize,
                "locationX", locationX,
                "locationY", locationY);
    }
}
