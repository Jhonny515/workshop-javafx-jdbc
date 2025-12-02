package com.jhonny.gui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jhonny.model.entity.Department;
import com.jhonny.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

public class DepartmentListController implements Initializable {

    @FXML
    private TableView<Department> tableViewDepartment;
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    @FXML
    private TableColumn<Department, String> tableColumnName;
    @FXML
    private Button buttonNew;

    @FXML
    public void onButtonNewAction() {
        System.out.println("onButtonNewAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty().subtract(40));
    }

}
