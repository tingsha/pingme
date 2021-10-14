package main.java.view.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertiesHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Can't load settings " + e.getMessage());
        }
        return properties;
    }

    public static void rewriteProperties(Map<String, String> newProperties){
        Properties properties = loadProperties();
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
            propertiesToSave.setProperty("domain", properties.getProperty("domain"));
            propertiesToSave.setProperty("lastServer", properties.getProperty("lastServer"));
            propertiesToSave.setProperty("addBtnDomain", properties.getProperty("addBtnDomain"));
            for (Map.Entry<String, String> entry : newProperties.entrySet()){
                propertiesToSave.setProperty(entry.getKey(), entry.getValue());
            }
            propertiesToSave.store(outputStream, null);
        } catch (IOException e) {
            logger.warn("Can't rewrite domain in property " + e.getMessage());
        }
    }
}
