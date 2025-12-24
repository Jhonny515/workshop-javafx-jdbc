package com.jhonny.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jhonny.db.DbException;
import com.jhonny.gui.listeners.DataChangeListener;
import com.jhonny.gui.util.Alerts;
import com.jhonny.gui.util.Constraints;
import com.jhonny.gui.util.Utils;
import com.jhonny.model.entity.Department;
import com.jhonny.model.services.DepartmentService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService service;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label labelErrorName;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service) {   
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was not set");
        }
        if (service == null) {
            throw new IllegalStateException("Service was not set");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error", "Error saving object", e.getMessage(), AlertType.ERROR);
        }
    }
    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was not set");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public Department getFormData() {
        entity.setId(Utils.tryParseToInt(txtId.getText()));
        entity.setName(txtName.getText());
        return entity;
    }
}
