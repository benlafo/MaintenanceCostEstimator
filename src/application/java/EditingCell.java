package application.java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

/**
 * Created by Ben LaForge on 5/19/2017.
 *
 * For the use and distribution of Trackmobile, LLC.
 */

public class EditingCell extends TableCell<Part, Double> {

    private TextField textField;
    private String type;

    public EditingCell(String type) {
        this.type = type;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        if (type.equals("price")) {
            setText("$" + String.format("%.2f", getItem()));
            setGraphic(null);
        } else if (type.equals("number")) {
            setText(String.format("%.0f", getItem()));
            setGraphic(null);
        } else if (type.equals("double")) {
            setText(String.format("%.1f", getItem()));
            setGraphic(null);
        }
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    if (type.equals("price")) {
                        textField.setText("$" + String.format("%.2f", getString()));
                    } else if (type.equals("number")) {
                        textField.setText(String.format("%.0f", getString()));
                    } else if (type.equals("double")) {
                        textField.setText(String.format("%.1f", getString()));
                    }
                }
                setText(null);
                setGraphic(textField);
            } else {
                if (type.equals("price")) {
                    setText("$" + String.format("%.2f", getString()));
                    setGraphic(null);
                } else if (type.equals("number")) {
                    setText(String.format("%.0f", getString()));
                    setGraphic(null);
                } else if (type.equals("double")) {
                    setText(String.format("%.1f", getString()));
                    setGraphic(null);
                }
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString().toString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0,
                                Boolean arg1, Boolean arg2) {
                if (!arg2) {
                    if (textField.getText().length() != 0) {
                        commitEdit(Double.parseDouble(textField.getText()));
                    } else {
                        commitEdit(0.00);
                    }
                }
            }
        });
    }

    private Double getString() {
        return getItem() == null ? 0.00 : getItem();
    }
}
