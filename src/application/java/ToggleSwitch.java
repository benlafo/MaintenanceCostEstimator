package application.java;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Ben LaForge on 5/22/2017.
 *
 * For the use and distribution of Trackmobile, LLC.
 */
public class ToggleSwitch extends HBox {

    private final Label label = new Label();
    private final Button button = new Button();

    private SimpleDoubleProperty difficulty = new SimpleDoubleProperty(1.0);
    public SimpleDoubleProperty difficultyProperty() { return difficulty; }

    private void init() {

        label.setText("Normal");
        button.setId("toggle");

        getChildren().addAll(label, button);
        button.setOnAction((e) -> {
            if (difficulty.get() == 1.0) {
                difficulty.set(2.0);
            } else {
                difficulty.set(1.0);
            }
        });

        label.setOnMouseClicked((e) -> {
            if (difficulty.get() == 1.0) {
                difficulty.set(2.0);
            } else {
                difficulty.set(1.0);
            }
        });
        setStyle();
        bindProperties();
    }

    private void setStyle() {
        //Default Width
        setWidth(80);
        label.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 4;");
        setAlignment(Pos.CENTER_LEFT);
        label.setFont(new Font("Segoe UI Bold", 14));
        label.setTextFill(Color.WHITE);
    }

    private void bindProperties() {
        label.prefWidthProperty().bind(widthProperty().divide(2));
        label.prefHeightProperty().bind(heightProperty());
        button.prefWidthProperty().bind(widthProperty().divide(2));
        button.prefHeightProperty().bind(heightProperty());
    }

    public ToggleSwitch() {
        init();
        difficulty.addListener((a,b,c) -> {
            if (c.doubleValue() == 2.0) {
                label.setText("Severe");
                setStyle("-fx-background-color: red;");
                label.toFront();
            }
            else {
                label.setText("Normal");
                setStyle("-fx-background-color: grey;");
                button.toFront();
            }
        });
    }
}


