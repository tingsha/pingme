package main.java.view.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageWriteEx {

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
            e.printStackTrace();
        }

    }
}
