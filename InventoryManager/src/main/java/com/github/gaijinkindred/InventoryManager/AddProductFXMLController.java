package com.github.gaijinkindred.InventoryManager;

import com.github.gaijinkindred.InventoryManager.InventoryObjects.Part;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AddProductFXMLController implements Initializable {
    
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label inventoryLevelLabel;
    @FXML private Label priceLabel;
    @FXML private Label minValLabel;
    @FXML private Label maxValLabel;
    @FXML private Label companyNameLabel;
    
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField inventoryLevelField;
    @FXML private TextField priceField;
    @FXML private TextField maxValueField;
    @FXML private TextField minValueField;
    @FXML private TextField companyNameField;
    
    @FXML private TableView partContentTable;
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button addPartButton;
    @FXML private Button deletePartButton;
    
    @FXML private TextField searchBox;
    
    public static ObservableList<Part> associatedParts;
    
    @FXML
    private void save(ActionEvent event) {
        int partId = Integer.parseInt(idField.getText());
        String partName = nameField.getText();
        double partPrice = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(inventoryLevelField.getText());
        int priceMin = Integer.parseInt(minValueField.getText());
        int priceMax = Integer.parseInt(maxValueField.getText());
        Product p;
        if(MainFXMLController.productPartIndex != -1) {
            p = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex);
            p.setId(partId);
            p.setMax(priceMax);
            p.setMin(priceMin);
            p.setName(partName);
            p.setPrice(partPrice);
            p.setStock(stock);
            ArrayList<Part> ps = new ArrayList<Part>();
            ObservableList<Part> parts = FXCollections.observableList(ps);
            int aPartSize = associatedParts.size();
            int allPartSize = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex).getAllAssociatedParts().size();
            boolean found = false;
            if(aPartSize > allPartSize) {
                System.out.println("Checkpoint A-A");
                for (Part part : associatedParts) {
                    found = false;
                    for (Part pt : MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex).getAllAssociatedParts()) {
                        if (pt == part) {
                            System.out.println("Checkpoint A-B");
                            found = true;
                        }
                    }
                    if(!found) {
                        System.out.println("Checkpoint A-C");
                        parts.add(part);
                    }
                    System.out.println("Checkpoint A-D");
                }
            } else if(aPartSize < allPartSize) {
                System.out.println("Checkpoint B");
                for(Part part : MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex).getAllAssociatedParts()) {
                    found = false;
                    for(Part pt : associatedParts) {
                        if(pt == part) {
                            found = true;
                        }
                    }
                    if(!found) {
                        parts.add(part);
                    }
                }
            }
            //Work on adding/removing part(s)
            System.out.println("Debug: " + aPartSize + " | " + allPartSize + " | " + (aPartSize < allPartSize) + " | " + (aPartSize > allPartSize));
            if(aPartSize < allPartSize && allPartSize != 0) {
                for(Part part : parts) {
                    System.out.println("Name check: " + part.getName());
                    Product prod = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex);
                    prod.getAllAssociatedParts().remove(part);
                }
            } else if(aPartSize > allPartSize) {
                for(Part part : parts) {
                    System.out.println("Check Name: " + part.getName());
                    Product prod = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex);
                    prod.getAllAssociatedParts().add(part);
                }
            } /*else {

                Product prod = new Product(partId, partName, partPrice, stock, priceMin, priceMax);
                MainApp.inventoryInstance.addProduct(prod);
                for(Part part : parts) {
                    prod.addAssociatedPart(part);
                }
            }*/
            System.out.println("End if/elif check");
        } else {
            Product prod = new Product(partId, partName, partPrice, stock, priceMin, priceMax);
            MainApp.inventoryInstance.addProduct(prod);
            for(Part part : associatedParts) {
                prod.addAssociatedPart(part);
            }
        }
        //} else {
        //    MainApp.inventoryInstance.addProduct(new Product(partId, partName, partPrice, stock, priceMin, priceMax));
        //}
        MainApp.dismissRecentStage();
    }
    
    @FXML
    private void addPart(ActionEvent event) {
        try {
            Stage stage = new Stage();
            MainApp.addChildStage(stage);
            stage.setTitle("Add Part");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddPartScene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            //Now we hope things can wait and not throw errors...
            //Brb doing some janky code, that technically isn't proper error handling but if it works - I can't complain...
        }
    }

    @FXML
    private void editPart(ActionEvent event) {
        int tempStore = MainFXMLController.productPartIndex;
        Part p = (Part)(partContentTable.getSelectionModel().getSelectedItem());
        MainFXMLController.productPartIndex = MainApp.inventoryInstance.getAllParts().indexOf(p);
        try {
            Stage stage = new Stage();
            MainApp.addChildStage(stage);
            stage.setTitle("Modify Part");
            MainFXMLController.addItem = false;
            MainFXMLController.product = false;
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddPartScene.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            //Now we hope things can wait and not throw errors...
            //Brb doing some janky code, that technically isn't proper error handling but if it works - I can't complain...
        }
        partContentTable.refresh();
        MainFXMLController.productPartIndex = tempStore;
        MainFXMLController.addItem = true;
        MainFXMLController.product = true;
    }
    
    @FXML
    private void deletePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to delete this part?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            associatedParts.remove(partContentTable.getSelectionModel().getSelectedItem());
            updateTable(associatedParts);
        }
    }

    @FXML
    private void removePart(ActionEvent event) {
        associatedParts.remove(partContentTable.getSelectionModel().getSelectedItem());
        updateTable(associatedParts);
    }

    @FXML
    private void addExistingPart(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        MainApp.addChildStage(stage);
        stage.setTitle("Add Existing Part");
        AddExistingFXMLController.parts = MainApp.inventoryInstance.getAllParts();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddExisting.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void search(ActionEvent event) {
        ArrayList<Part> parts = new ArrayList<Part>();
        Product prod = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex);
        for(Part p : prod.getAllAssociatedParts()) {
            if(p.getName().equalsIgnoreCase(searchBox.getText()) || p.getId() == Integer.parseInt(searchBox.getText())) {
                parts.add(p);
            }
        }
        updateTable(FXCollections.observableList(parts));
    }
    
    @FXML
    private void cancel(ActionEvent event) {
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
        if(MainFXMLController.product && MainFXMLController.productPartIndex != -1) {
            Product p = MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex);
            idField.setText(p.getId() + "");
            nameField.setText(p.getName());
            priceField.setText(p.getPrice() + "");
            inventoryLevelField.setText(p.getStock() + "");
            minValueField.setText(p.getMin() + "");
            maxValueField.setText(p.getMax() + "");

            associatedParts = FXCollections.observableList(new ArrayList<Part>());
            //ObservableList passes memory pointers using the `=` operand, so we have to recreate the list to avoid this...
            //  ...such that we desire a cancel button to work like a cancel button.
            for(Part part : MainApp.inventoryInstance.getAllProducts().get(MainFXMLController.productPartIndex).getAllAssociatedParts()) {
                associatedParts.add(part);
            }
            if(associatedParts == null) {
                System.out.println("ERROR NULL ASSOCIATEDPARTS VARIABLE");
            }
            updateTable(associatedParts);
        } else {
            associatedParts = FXCollections.observableList(new ArrayList<Part>());
            updateTable(associatedParts);
        }
    }
    
    private void updateTable(ObservableList<Part> parts) {
        partContentTable.setItems(parts);
        ((TableColumn) (partContentTable.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        ((TableColumn) (partContentTable.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn) (partContentTable.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        ((TableColumn) (partContentTable.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
    }
}
