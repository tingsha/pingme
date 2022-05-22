package main.java.view.settings;

import javax.swing.*;

public class UploadBtn extends ToggleBtn {
    public UploadBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        if (!preview.getText().contains("60")) {
            preview.setText(preview.getText() + "upload: 60Mb/s\n");
        }
    }

    @Override
    public void doUnchecked() {
        if (preview.getText().contains("60")) {
            preview.setText(preview.getText().replaceAll("upload: 60Mb/s\n", ""));
            preview.setText(preview.getText().replaceAll("upload: 60\n", ""));
            preview.setText(preview.getText().replaceAll("60Mb/s\n", ""));
            preview.setText(preview.getText().replaceAll("60\n", ""));
        }
    }
}
