package com.jhonny.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.jhonny.App;
import com.jhonny.gui.util.Alerts;
import com.jhonny.model.services.DepartmentService;
import com.jhonny.model.services.SellerService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
            controller.setSellerService(new SellerService());
            controller.updateTableView();
        });
    }
    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }
    @FXML
    public void onMenuItemAboutAction() {
        loadView("/gui/About.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializeController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
            Scene mainScene = App.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
            
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);   // Adiciona o menu principal novamente
            mainVBox.getChildren().add(newVBox);

            T controller = loader.getController();
            initializeController.accept(controller);
        } catch (IOException e) {
            Alerts.showAlert("Error", "Error loading view", e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
