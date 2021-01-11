package com.github.gaijinkindred.InventoryManager;

import com.github.gaijinkindred.InventoryManager.InventoryObjects.Part;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Product;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MainFXMLController implements Initializable {
    
    @FXML private Label headerLabelA;
    @FXML private Label headerLabelB;
    @FXML private TextField productSearchField;
    @FXML private TextField partSearchField;
    @FXML private TableView partContentTable;
    @FXML private TableView productContentTable;
    
    public static boolean addItem = false;
    public static boolean product = false;
    public static int productPartIndex = -1;
    
    @FXML
    private void handleAddProductButtonAction(ActionEvent event) {
        productPartIndex = -1;
        addOrModifyButton(true, true);
    }
    
    @FXML
    private void handleAddPartButtonAction(ActionEvent event) {
        productPartIndex = -1;
        addOrModifyButton(false, true);
    }
    
    @FXML
    private void handleModifyProductButtonAction(ActionEvent event) {
        productPartIndex = MainApp.inventoryInstance.getAllProducts().indexOf((Product) productContentTable.getSelectionModel().getSelectedItem());
        addOrModifyButton(true, false);
    }
    
    @FXML
    private void handleModifyPartButtonAction(ActionEvent event) {
        productPartIndex = MainApp.inventoryInstance.getAllParts().indexOf((Part) partContentTable.getSelectionModel().getSelectedItem());
        addOrModifyButton(false, false);
    }
    
    private void addOrModifyButton(boolean product, boolean add) {
        try {
            String title = product ? (add ? "Add Product" : "Modify Product") : (add ? "Add Part" : "Modify Part");
            Stage stage = new Stage();
            MainApp.addChildStage(stage);
            stage.setTitle(title);
            
            addItem = add;
            MainFXMLController.product = product;
            Parent root;
            if(product) {
                root = FXMLLoader.load(getClass().getResource("/fxml/AddProductScene.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/fxml/AddPartScene.fxml"));
            }
            
            if(root != null) {
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                stage.setScene(scene);
                stage.showAndWait();
                //updateTable();
                partContentTable.refresh();
                productContentTable.refresh();
            }
        } catch (IOException ex) {
            //Default catch statement, this should only occur in the event that there are instancing issues -- which shouldn't happen..
            Logger.getLogger(MainFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleDeleteProductButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to delete this product?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            Product p = (Product) productContentTable.getSelectionModel().getSelectedItem();
            MainApp.inventoryInstance.deleteProduct(p);
        }
    }
    
    @FXML
    private void handleDeletePartButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to delete this product?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            Part p = (Part) partContentTable.getSelectionModel().getSelectedItem();
            MainApp.inventoryInstance.deletePart(p);
        }
    }
    
    @FXML
    private void handleSearchProductButtonAction(ActionEvent event) {
        ArrayList<Product> filteredProducts = new ArrayList<Product>();
        String searchTextTemp = "";
        String partNameTemp = "";
        //For Each Product, p,
        for(Product p : MainApp.inventoryInstance.getAllProducts()) {
            //If partSearchField has more than 1 character
            if((searchTextTemp = partSearchField.getText().toLowerCase()).length() > 1) {
                //Then compare the two strings (ignoring case)
                if((partNameTemp = p.getName().toLowerCase()).contains(searchTextTemp)) {
                    filteredProducts.add(p);
                }
            //Else, If partSearchField has only one character in it
            } else if(partSearchField.getText().length() == 1) {
                //Narrow results to be 'a' + " "
                if((partNameTemp = p.getName().toLowerCase()).contains(searchTextTemp + " ")) {
                    filteredProducts.add(p);
                //  or " " + 'a'
                } else if(partNameTemp.contains(" " + searchTextTemp)) {
                    filteredProducts.add(p);
                //  or if there is only one character then add it if it's just 'a'
                } else if(partNameTemp.contains(searchTextTemp) && partNameTemp.length() == 1) {
                    filteredProducts.add(p);
                }
            }
        }
        productContentTable.setItems(FXCollections.observableList(filteredProducts));
    }
    
    @FXML
    private void handleSearchPartButtonAction(ActionEvent event) {
        ArrayList<Part> filteredParts = new ArrayList<Part>();
        String searchTextTemp = "";
        String partNameTemp = "";
        for(Part p : MainApp.inventoryInstance.getAllParts()) {
            if((searchTextTemp = partSearchField.getText().toLowerCase()).length() > 1) {
              if((partNameTemp = p.getName().toLowerCase()).contains(searchTextTemp)) {
                    filteredParts.add(p);
                }
            } else if(partSearchField.getText().length() == 1) {
                if((partNameTemp = p.getName().toLowerCase()).contains(searchTextTemp + " ")) {
                    filteredParts.add(p);
                } else if(partNameTemp.contains(" " + searchTextTemp)) {
                    filteredParts.add(p);
                } else if(partNameTemp.contains(searchTextTemp) && partNameTemp.length() == 1) {
                    filteredParts.add(p);
                }
            }
        }
        partContentTable.setItems(FXCollections.observableList(filteredParts));
    }
    
    @FXML
    private void handleExitButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to exit?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        headerLabelA.setText("Products");
        headerLabelB.setText("Parts");
        updateTable();
    }
    
    public void updateTable() {
        productContentTable.setItems(MainApp.inventoryInstance.getAllProducts());
        partContentTable.setItems(MainApp.inventoryInstance.getAllParts());
        ((TableColumn) (productContentTable.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        ((TableColumn) (productContentTable.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        ((TableColumn) (productContentTable.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));
        ((TableColumn) (productContentTable.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        ((TableColumn) (partContentTable.getColumns().get(0))).setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        ((TableColumn) (partContentTable.getColumns().get(1))).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn) (partContentTable.getColumns().get(2))).setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        ((TableColumn) (partContentTable.getColumns().get(3))).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
    }
}
