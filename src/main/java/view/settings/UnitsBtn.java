package main.java.view.settings;

import javax.swing.*;

/**
 * Переключатель, отвечающий за видимость единиц измерения
 */
public class UnitsBtn extends ToggleBtn {
    public UnitsBtn(JTextPane preview) {
        super(preview);
    }

    @Override
    public void doChecked() {
        preview.setText(preview.getText().replace("60", "60Mb/s"));
        preview.setText(preview.getText().replace("59", "59Mb/s"));
        if (!preview.getText().contains("ms"))
            preview.setText(preview.getText().replace("43", "43ms"));
    }

    @Override
    public void doUnchecked() {
        preview.setText(preview.getText().replace("Mb/s", ""));
        preview.setText(preview.getText().replace("ms", ""));
    }
}
