package main.java.view.settings;

import javax.swing.*;

public class LabelsBtn extends ToggleBtn{
    public LabelsBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        if (!preview.getText().contains("ping: "))
            preview.setText(preview.getText().replaceAll("43", "ping: 43"));
        preview.setText(preview.getText().replaceAll("59", "download: 59"));
        preview.setText(preview.getText().replaceAll("60", "upload: 60"));
    }

    @Override
    public void doUnchecked() {
        preview.setText(preview.getText().replaceAll("ping: ", ""));
        preview.setText(preview.getText().replaceAll("upload: ", ""));
        preview.setText(preview.getText().replaceAll("download: ", ""));
    }
}
