package com.bot;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class newTaskController implements Initializable {

    @FXML
    private JFXButton exitButton;
    private Stage stage;
    @FXML
    private JFXTextField productTags;

    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXTimePicker timePicker;

    private RequiredFieldValidator validator;
    @FXML
    private AnchorPane container;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validator = new RequiredFieldValidator();
        validator.setMessage("This field is required.");

        productTags.getValidators().add(validator);
        datePicker.getValidators().add(validator);
        timePicker.getValidators().add(validator);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        container.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        container.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    private void closeWindowButton(MouseEvent event) {
        System.exit(0);
    }
    @FXML
    private void exitHoverStart(MouseEvent event) {
        exitButton.setOpacity(.7);
    }
    @FXML
    private void exitHoverEnd(MouseEvent event) {
        exitButton.setOpacity(1);
    }

    @FXML
    private void backButtonPressed(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tasks.fxml"));
        stage.setScene(new Scene(loader.load()));
        TasksController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void makeTask(MouseEvent event) {
        String stringTags = productTags.getText();
        LocalDate date = datePicker.getValue();
        LocalTime time = timePicker.getValue();

        if (stringTags.isEmpty()) {
            System.out.println("oof product");
        } else if (date == null){
            System.out.println("oof date");
        } else if (time == null) {
            System.out.println("oof time");
        } else {
            System.out.println("nega chin");
        }

    }
}
