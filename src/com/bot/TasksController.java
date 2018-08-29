package com.bot;

import com.bot.objects.Task;
import com.bot.objects.TaskList;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class TasksController implements Initializable {

    static final class TaskRow extends RecursiveTreeObject<TaskRow> {

        final IntegerProperty id;
        final StringProperty productTags;
        final StringProperty size;
        final StringProperty date;
        final StringProperty time;
        final IntegerProperty quantity;

        TaskRow(Integer id, String productTags, String size, String date, String time, Integer quantity) {
            this.id = new SimpleIntegerProperty(id);
            this.productTags = new SimpleStringProperty(productTags);
            this.size = new SimpleStringProperty(size);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.quantity = new SimpleIntegerProperty(quantity);
        }
    }

    @FXML
    private JFXButton homeButton;
    @FXML
    private JFXButton exitButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private AnchorPane container;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private JFXTreeTableView<TaskRow> taskTable;

    private Stage stage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Gson gson = new Gson();

        BufferedReader reader = null;

        JFXTreeTableColumn<TaskRow, Integer> idColumn = new JFXTreeTableColumn<>("ID");
        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<TaskRow, Integer> param) -> {
                return idColumn.getComputedValue(param);
        });
        JFXTreeTableColumn<TaskRow, String> productColumn = new JFXTreeTableColumn<>("Product Tags");
        productColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTags"));
        JFXTreeTableColumn<TaskRow, String> sizeColumn = new JFXTreeTableColumn<>("Size");
        sizeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));
        JFXTreeTableColumn<TaskRow, String> dateColumn = new JFXTreeTableColumn<>("Start Date");
        dateColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        JFXTreeTableColumn<TaskRow, String> timeColumn = new JFXTreeTableColumn<>("Start Time");
        timeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("time"));
        JFXTreeTableColumn<TaskRow, Integer> quantityColumn = new JFXTreeTableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("quantity"));

        taskTable.getColumns().addAll(idColumn, productColumn,dateColumn,timeColumn,sizeColumn,quantityColumn);

        try {
            reader = new BufferedReader(new FileReader("assets/tasks.json"));

            TaskList taskList = gson.fromJson(reader, TaskList.class);

            ObservableList<TaskRow> listRows = FXCollections.observableArrayList();


            if (taskList != null) {
                for (Task t : taskList.getTaskList()) {
                    TaskRow tRow = new TaskRow(t.getId(), t.getProductTags(), t.getSize(), t.getStartDate(), t.getStartTime(), t.getQuantity());
                    listRows.add(tRow);
                }
            }

            final TreeItem<TaskRow> treeItem = new RecursiveTreeItem<>(listRows, RecursiveTreeObject::getChildren);
            taskTable.setRoot(treeItem);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        stage.setScene(new Scene(loader.load()));
        SettingsController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void goHome(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        stage.setScene(new Scene(loader.load()));
        HomeController c = loader.getController();
        c.setStage(stage);
    }

    @FXML
    private void newTask(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("newTask.fxml"));
        stage.setScene(new Scene(loader.load()));
        newTaskController c = loader.getController();
        c.setStage(stage);
    }

}
