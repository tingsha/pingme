package main.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Утилита для работы с файлами
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Получить файл из папки с ресурсами.
     *
     * @param filPath путь к файлу в ресурсах
     * @return {@link File}, если нет ошибок, иначе выбрасывает {@link RuntimeException}
     */
    public static File getFileFromResources(String filPath) {
        try {
            return new File(FileUtils.class.getResource(filPath).toURI());
        } catch (Exception e) {
            logger.error(String.format("Exception during reading file %s. %s", filPath, e));
            throw new RuntimeException(e);
        }
    }
}
