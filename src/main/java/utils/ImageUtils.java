package main.java.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Утилита для работы с изображениями
 */
public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static void createImageWithText(Integer w, Integer h, String str) {
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(null);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.BLACK);
        g.drawString(str, 2, 20);
        try {
            File outputfile = new File("src/main/resources/img/save.png");
            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (Exception e) {
            logger.error(String.format("Exception during reading image from file %s. %s", "src/main/resources/img/save.png", e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить изображение из файла
     *
     * @param file файл с изображением
     * @return {@link Image}, если чтение успешно, иначе выбрасывает {@link RuntimeException}
     */
    public static ImageIcon getImageIcon(File file) {
        try {
            return new ImageIcon(ImageIO.read(file));
        } catch (IOException e) {
            logger.error(String.format("Exception during reading image from file %s. %s", file.getAbsolutePath(), e));
            throw new RuntimeException(e);
        }
    }

    public static Image getImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            logger.error(String.format("Exception during reading image from file %s. %s", file.getAbsolutePath(), e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}
