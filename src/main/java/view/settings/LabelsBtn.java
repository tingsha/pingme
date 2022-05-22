package main.java.view.settings;

import javax.swing.*;

/**
 * Переключатель, отвечающий за видимость подписи данных
 */
public class LabelsBtn extends ToggleBtn {
    public LabelsBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        if (!preview.getText().contains("ping: "))
            preview.setText(preview.getText().replace("43", "ping: 43"));
        preview.setText(preview.getText().replace("59", "download: 59"));
        preview.setText(preview.getText().replace("60", "upload: 60"));
    }

    @Override
    public void doUnchecked() {
        preview.setText(preview.getText().replace("ping: ", ""));
        preview.setText(preview.getText().replace("upload: ", ""));
        preview.setText(preview.getText().replace("download: ", ""));
    }
}
