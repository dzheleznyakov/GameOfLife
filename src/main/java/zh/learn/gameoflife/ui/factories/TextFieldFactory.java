package zh.learn.gameoflife.ui.factories;

import javafx.scene.control.TextField;

import java.util.Objects;

public class TextFieldFactory {
    public static TextField getNumericField(int initialValue, int max) {
        TextField tf = new TextField(String.valueOf(initialValue));
        tf.setPrefColumnCount(3);
        tf.textProperty().addListener((prop, oldValue, newValue) -> {
            String value = newValue.matches("\\d*")
                    ? newValue
                    : newValue.replaceAll("[\\D]", "");
            if (Objects.equals(value, ""))
                return;
            int num = Integer.parseInt(value);
            if (num < 2)
                tf.setText("2");
            else if (num > max)
                tf.setText(oldValue);
            else
                tf.setText(value);
        });
        return tf;
    }
}
