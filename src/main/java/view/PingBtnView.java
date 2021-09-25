package main.java.view;

import main.java.controller.Controller;
import main.java.model.PingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PingBtnView extends JCheckBox implements View {
    private Controller controller;
    private final Color bgColor = new Color(43, 43, 43);
    private final static Logger logger = LoggerFactory.getLogger(PingBtnView.class);

    public PingBtnView(){
        setHorizontalAlignment((int) CENTER_ALIGNMENT);
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(0, 17, 0, 7));
        setBorderPaintedFlat(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setImageIcon(Path.of("src/main/resources/img/charger/charger_grey.png"));
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                StatisticView statisticView = null;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    setImageIcon(Path.of("src/main/resources/img/charger/charger_complete.png"));
                    statisticView = new StatisticView();
                    StatisticView finalStatisticView = statisticView;
                    controller.onSelectPingBtn();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            while (e.getStateChange() == ItemEvent.SELECTED){
                                finalStatisticView.refresh(PingTask.getPing());
                            }
                        }
                    };
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(runnable);
                } else if (e.getStateChange() == ItemEvent.DESELECTED){
                    setImageIcon(Path.of("src/main/resources/img/charger/charger_grey.png"));
                    statisticView.setVisible(false);
                    controller.onDeselectPingBtn();
                }
            }
        });
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setImageIcon(Path pathToIcon){
        try {
            Image cbIcon = ImageIO.read(pathToIcon.toFile());
            setIcon(new ImageIcon(cbIcon));
        } catch (IOException e) {
            logger.error("Can't load ping btn image! " + e.getMessage());
        }
    }
}
