package com.jhonny.gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jhonny.App;
import com.jhonny.db.DbIntegrityException;
import com.jhonny.gui.listeners.DataChangeListener;
import com.jhonny.gui.util.Alerts;
import com.jhonny.gui.util.Utils;
import com.jhonny.model.entity.Seller;
import com.jhonny.model.services.SellerService;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SellerListController implements Initializable, DataChangeListener {

    @FXML
    private TableView<Seller> tableViewSeller;
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    @FXML
    private Button buttonNew;
    @FXML
    private TableColumn<Seller, Seller> tableColumnEDIT;
    @FXML
    private TableColumn<Seller, Seller> tableColumnREMOVE;

    private ObservableList<Seller> observableList = FXCollections.observableArrayList();

    private SellerService service;

    @FXML
    public void onButtonNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Seller();
        // createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was not set");
        }
        List<Seller> list = service.findAll();
        observableList.setAll(list);
        tableViewSeller.setItems(observableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Seller, Integer>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Seller, String>("name"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty().subtract(40));
    }

    // private void createDialogForm(Seller entity, String absoluteName, Stage parentStage) {
    //     try {
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
    //         AnchorPane pane = loader.load();

    //         SellerFormController controller = loader.getController();
    //         controller.setSeller(entity);
    //         controller.setSellerService(new SellerService());
    //         controller.subscribeDataChangeListener(this);
    //         controller.updateFormData();

    //         Stage dialogStage = new Stage();
    //         dialogStage.setTitle("Enter Seller Data");
    //         dialogStage.setScene(new Scene(pane));
    //         dialogStage.setResizable(false);
    //         dialogStage.initOwner(parentStage);
    //         dialogStage.initModality(Modality.WINDOW_MODAL);
    //         dialogStage.showAndWait();
    //     } catch (IOException e) {
    //         Alerts.showAlert("Error", "Error loading view", e.getMessage(), AlertType.ERROR);
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                // button.setOnAction(
                        // event -> createDialogForm(
                                // obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
                                // null);
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Delete Confirmation", "Are you sure you want to delete this Seller?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was not set");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error", "Error deleting object", e.getMessage(), AlertType.ERROR);
            }
        }
    }

}
