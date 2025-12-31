package com.jhonny.gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jhonny.App;
import com.jhonny.gui.listeners.DataChangeListener;
import com.jhonny.gui.util.Alerts;
import com.jhonny.gui.util.Utils;
import com.jhonny.model.entity.Department;
import com.jhonny.model.services.DepartmentService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepartmentListController implements Initializable, DataChangeListener {

    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button buttonNew;
    @FXML
    private TableColumn<Department, Department> tableColumnEDIT;

    private ObservableList<Department> observableList = FXCollections.observableArrayList();

    private DepartmentService service;

    @FXML
    public void onButtonNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was not set");
        }
        List<Department> list = service.findAll();
        observableList.setAll(list);
        tableViewDepartment.setItems(observableList);
        initEditButtons();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty().subtract(40));
    }

    private void createDialogForm(Department entity, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            AnchorPane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(entity);
            controller.setDepartmentService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department Data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("Error", "Error loading view", e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
            }
        });
    }

}
