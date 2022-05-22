package main.java.view;

import main.java.utils.Colors;
import main.java.utils.FileUtils;
import main.java.utils.PropertiesUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Представление окна с выбором своего сервера
 */
public class ServerDialogView extends JDialog {

    public ServerDialogView(ServersView.ServerButton btn) {
        setLayout(new BorderLayout());
        setBackground(Colors.TOOLBAR_BACKGROUND);

        JTextField field = new JTextField();
        field.setBackground(Colors.TOOLBAR_BACKGROUND);
        field.setPreferredSize(new Dimension(300, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(null);

        JButton saveBtn = new JButton("Ok");
        saveBtn.setBorder(null);
        saveBtn.setPreferredSize(new Dimension(300, 30));
        //TODO вынести в Colors
        saveBtn.setBackground(new Color(100, 98, 252));
        saveBtn.setForeground(new Color(253, 252, 255));
        saveBtn.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        saveBtn.addActionListener(e -> {
            btn.setServerAddress(field.getText());
            setVisible(false);
            PropertiesUtils.rewriteProperties(FileUtils.getFileFromResources("/config.properties"), Map.of("userServerAddress", field.getText()));
        });

        add(field, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);
        setVisible(true);
        setLocationRelativeTo(null);
        pack();
    }
}
