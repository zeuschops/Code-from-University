package com.github.gaijinkindred.InventoryManager;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.gaijinkindred.InventoryManager.InventoryObjects.Part;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;

public class AddExistingFXMLController implements Initializable {
    public static ObservableList<Part> parts;

    @FXML private TableView contentTable;

    @FXML
    private void addPart(ActionEvent event) {
        Part p = parts.get(contentTable.getSelectionModel().getSelectedIndex());
        AddProductFXMLController.associatedParts.add(p);
        MainApp.dismissRecentStage();
    }

    @FXML
    private void dismiss(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            MainApp.dismissRecentStage();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(parts != null) {
            contentTable.setItems(parts);
            ((TableColumn) (contentTable.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
            ((TableColumn) (contentTable.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
            ((TableColumn) (contentTable.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
            ((TableColumn) (contentTable.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        }
    }
}
