package com.bot;

import com.bot.objects.Task;
import com.bot.objects.TaskList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class newTaskController implements Initializable {

    @FXML
    private JFXButton exitButton;
    private Stage stage;
    @FXML
    private JFXTextField productTags;

    @FXML
    private ToggleGroup size;

    @FXML
    private JFXRadioButton smButton;
    @FXML
    private JFXRadioButton mdButton;
    @FXML
    private JFXRadioButton lgButton;
    @FXML
    private JFXRadioButton xlButton;

    @FXML
    private JFXTextField quantity;
    @FXML
    private JFXButton lessButton;
    @FXML
    private JFXButton moreButton;

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
        smButton.setUserData("Small");
        mdButton.setUserData("Medium");
        lgButton.setUserData("Large");
        xlButton.setUserData("Extra Large");
        quantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantity.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.equals("")) {
                quantity.setText("1");
            }
        });
        productTags.textProperty().addListener((observable, oldValue, newValue) -> {
            if (productTags.getActiveValidator() != null) {
                productTags.resetValidation();;
            }
        });

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
    private void changeToTasks(MouseEvent event) throws IOException {
        stage = (Stage) exitButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tasks.fxml"));
        stage.setScene(new Scene(loader.load()));
        TasksController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void makeTask(MouseEvent event) throws IOException{
        String stringTags = productTags.getText();
        LocalDate date = datePicker.getValue();
        LocalTime time = timePicker.getValue();
        RadioButton sizeButton = (RadioButton) size.getSelectedToggle();
        String sizeString = sizeButton.getUserData().toString();
        int quant = Integer.parseInt(quantity.getText());

        if (stringTags.isEmpty()) {
            System.out.println("oof product");
            productTags.validate();
        } else if (date == null){
            System.out.println("oof date");
            datePicker.validate();
        } else if (time == null) {
            System.out.println("oof time");
            timePicker.validate();
        } else if (sizeString.isEmpty()) {
            System.out.println("oof size");
        } else {
            Gson gson = new Gson();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader("assets/tasks.json"));
                TaskList taskList = gson.fromJson(bufferedReader, TaskList.class);
                if (taskList != null) {
                    for (Task t : taskList.getTaskList()) {
                        System.out.println(t.getId());
                    }
                } else {
                    taskList = new TaskList();
                }
                Task newTask = new Task();
                newTask.setId(0);
                newTask.setProductTags(stringTags);
                newTask.setStartDate(date.toString());
                newTask.setStartTime(time.toString());
                newTask.setSize(sizeString);
                newTask.setQuantity(quant);
                taskList.addToTaskList(Arrays.asList(newTask));
                gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonObj = gson.toJson(taskList);
                FileWriter writer = null;

                try {
                    writer = new FileWriter("assets/tasks.json");
                    writer.write(jsonObj);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        }

        changeToTasks(event);
    }

    @FXML
    private void moreQuantity(MouseEvent event) {
        Integer currQ = Integer.parseInt(quantity.getText());
        quantity.setText(Integer.toString(currQ+1));
    }

    @FXML
    private void lessQuantity(MouseEvent event) {
        Integer currQ = Integer.parseInt(quantity.getText());
        if (currQ - 1 > 0) {
            quantity.setText(Integer.toString(currQ-1));
        } else {
            quantity.setText(Integer.toString(currQ));
        }
    }

}
