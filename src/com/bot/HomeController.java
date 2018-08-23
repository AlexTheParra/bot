package com.bot;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private Stage stage;

    @FXML
    private JFXButton tasksButton;
    @FXML
    private JFXButton exitButton;
    @FXML
    private Label createNewTask;
    @FXML
    private JFXButton homeButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private AnchorPane container;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    private void goSettings(MouseEvent event) throws IOException {
        stage = (Stage) tasksButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        stage.setScene(new Scene(loader.load()));
        SettingsController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void changeToTasks(MouseEvent event) throws IOException {
        stage = (Stage) tasksButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tasks.fxml"));
        stage.setScene(new Scene(loader.load()));
        TasksController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void hoverStartTaskCreate(MouseEvent event) {
        createNewTask.setUnderline(true);
    }

    @FXML
    private void hoverEndTaskCreate(MouseEvent event) {
        createNewTask.setUnderline(false);
    }


}