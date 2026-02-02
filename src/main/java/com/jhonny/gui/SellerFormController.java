package com.jhonny.gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.jhonny.db.DbException;
import com.jhonny.gui.listeners.DataChangeListener;
import com.jhonny.gui.util.Alerts;
import com.jhonny.gui.util.Constraints;
import com.jhonny.gui.util.Utils;
import com.jhonny.model.entity.Department;
import com.jhonny.model.entity.Seller;
import com.jhonny.model.exceptions.ValidationException;
import com.jhonny.model.services.DepartmentService;
import com.jhonny.model.services.SellerService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SellerFormController implements Initializable {

    private Seller entity;
    private SellerService service;
    private DepartmentService departmentService;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private ComboBox<Department> comboBoxDepartment;
    @FXML
    private Label labelErrorName;
    @FXML
    private Label labelErrorEmail;
    @FXML
    private Label labelErrorBirthDate;
    @FXML
    private Label labelErrorBaseSalary;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;

    private ObservableList<Department> obsList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setServices(SellerService service, DepartmentService departmentService) {
        this.service = service;
        this.departmentService = departmentService;
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
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
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
        Constraints.setTextFieldMaxLength(txtName, 100);
        Constraints.setTextFieldMaxLength(txtEmail, 70);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was not set");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());
        if (entity.getBirthDate() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
        }
        Locale.setDefault(Locale.US);
        txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        if (entity.getDepartment() == null) {
            comboBoxDepartment.getSelectionModel().selectFirst();
        } else {
            comboBoxDepartment.getSelectionModel().select(entity.getDepartment());
        }
    }

    public void loadAssociatedObjects() {
        if (departmentService == null) {
            throw new IllegalStateException("Department service was not set");
        }
        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public Seller getFormData() {
        ValidationException exception = new ValidationException("Validation error");
        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
            exception.addError("email", "Field can't be empty");
        }

        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
            exception.addError("baseSalary", "Field can't be empty");
        }

        
        try {
            entity.setBirthDate(Date.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } catch (Exception e) {
            exception.addError("birthDate", "Invalid date");
        }

        entity.setId(Utils.tryParseToInt(txtId.getText()));
        entity.setName(txtName.getText());
        entity.setEmail(txtEmail.getText());
        entity.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
        entity.setDepartment(comboBoxDepartment.getSelectionModel().getSelectedItem());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return entity;
    }

    private void setErrorMessages(Map<String, String> errors) {
        clearErrors();
        Set<String> fields = errors.keySet();
        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
        if (fields.contains("email")) {
            labelErrorEmail.setText(errors.get("email"));
        }
        if (fields.contains("baseSalary")) {
            labelErrorBaseSalary.setText(errors.get("baseSalary"));
        }
        if (fields.contains("birthDate")) {
            labelErrorBirthDate.setText(errors.get("birthDate"));
        }
    }

    private void clearErrors() {
        labelErrorName.setText("");
        labelErrorEmail.setText("");
        labelErrorBaseSalary.setText("");
        labelErrorBirthDate.setText("");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}
