package main.java.view;

import main.java.view.utils.Colors;
import main.java.view.utils.PropertiesHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DomainDialog extends JDialog {

    public DomainDialog(ServersView.ServerButton btn){
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
        saveBtn.setBackground(new Color(100, 98, 252));
        saveBtn.setForeground(new Color(253, 252, 255));
        saveBtn.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn.setDomain(field.getText());
                setVisible(false);
                PropertiesHelper.rewriteProperties(new HashMap<>(){{
                    put("addBtnDomain", field.getText());
                }});
            }
        });
        add(field, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);
        setVisible(true);
        setLocationRelativeTo(null);
        pack();
    }
}
