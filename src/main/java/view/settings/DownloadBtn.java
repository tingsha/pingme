package main.java.view.settings;

import javax.swing.*;

/**
 * Переключатель, отвечающий за видимость скорости загрузки
 */
public class DownloadBtn extends ToggleBtn {

    public DownloadBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        if (!preview.getText().contains("59")) {
            preview.setText(preview.getText() + "download: 59Mb/s\n");
        }
    }

    @Override
    public void doUnchecked() {
        if (preview.getText().contains("59")) {
            preview.setText(preview.getText().replace("download: 59Mb/s\n", ""));
            preview.setText(preview.getText().replace("download: 59\n", ""));
            preview.setText(preview.getText().replace("59Mb/s\n", ""));
            preview.setText(preview.getText().replace("59\n", ""));
        }
    }
}
