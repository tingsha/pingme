package main.java.view.settings;

import javax.swing.*;

public class UnitsBtn extends ToggleBtn{
    public UnitsBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        preview.setText(preview.getText().replaceAll("60", "60Mb/s"));
        preview.setText(preview.getText().replaceAll("59", "59Mb/s"));
        if (!preview.getText().contains("ms"))
            preview.setText(preview.getText().replaceAll("43", "43ms"));
    }

    @Override
    public void doUnchecked() {
        preview.setText(preview.getText().replaceAll("Mb/s", ""));
        preview.setText(preview.getText().replaceAll("ms", ""));
    }
}
