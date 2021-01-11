/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.gaijinkindred.InventoryManager;

import com.github.gaijinkindred.InventoryManager.InventoryObjects.InHouse;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Outsourced;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Part;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author gaijin
 */
public class AddPartFXMLController implements Initializable {
    
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    
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
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    
    @FXML
    private void saveButton(ActionEvent event) {
        int idInt = Integer.parseInt(idField.getText());
        String nameText = nameField.getText();
        double priceDouble = Double.parseDouble(priceField.getText());
        int stockInt = Integer.parseInt(inventoryLevelField.getText());
        int minInt = Integer.parseInt(minValueField.getText());
        int maxInt = Integer.parseInt(maxValueField.getText());
        boolean inHouse = inHouseRadioButton.isSelected();

        if(MainApp.childStages.size() > 1 && MainFXMLController.addItem) {
            if (MainApp.childStages.get(MainApp.childStages.size() - 2).getTitle().toLowerCase().contains("product")) {
                if (inHouse) {
                    InHouse inhouse = new InHouse(idInt, nameText, priceDouble, stockInt, minInt, maxInt, Integer.parseInt(companyNameField.getText()));
                    AddProductFXMLController.associatedParts.add(inhouse);
                } else {
                    Outsourced outsourced = new Outsourced(idInt, nameText, priceDouble, stockInt, minInt, maxInt, companyNameField.getText());
                    AddProductFXMLController.associatedParts.add(outsourced);
                }
            }
        } else if(MainApp.childStages.size() > 1) {
            int ppi = MainFXMLController.productPartIndex;
            Part p = MainApp.inventoryInstance.getAllParts().get(ppi);
            p.setPrice(priceDouble);
            p.setId(idInt);
            p.setName(nameText);
            p.setStock(stockInt);
            p.setMin(minInt);
            p.setMax(maxInt);
        } else if(priceDouble < minInt || priceDouble > maxInt) {
            try {
                Stage stage = new Stage();
                MainApp.addChildStage(stage);
                MainApp.notificationDialog = "Pricing Error encountered. " + (priceDouble < minInt ? "Price must be greater than the minimum." : "Price must be lower than the maximum.");
                
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/DialogBox.fxml"));
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                stage.setTitle("Price error");
                
                stage.setScene(scene);
                stage.show();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            if(MainFXMLController.addItem && !MainFXMLController.product) {
                Part part = new Part(idInt, nameText, priceDouble, stockInt, minInt, maxInt);
                MainApp.inventoryInstance.addPart(part);
            } else if(!MainFXMLController.product) {
                Part p = MainApp.inventoryInstance.getAllParts().get(MainFXMLController.productPartIndex);
                if (p != null) {
                    p.setName(nameText);
                    p.setPrice(priceDouble);
                    p.setMax(maxInt);
                    p.setMin(minInt);
                    p.setStock(stockInt);
                    p.setId(idInt);
                } else {
                    Part part = new Part(idInt, nameText, priceDouble, stockInt, minInt, maxInt);
                    MainApp.inventoryInstance.addPart(part);
                }
            }
        }

        MainApp.dismissRecentStage();
    }
    
    @FXML
    private void cancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.setContentText("Are you sure you want to cancel?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            MainApp.dismissRecentStage();
        }
    }
    
    @FXML
    private void handleInHouseRadioButton(ActionEvent event) {
        companyNameLabel.setText("Machine ID:");
    }
    
    @FXML
    private void handleOutsourcedRadioButton(ActionEvent event) {
        companyNameLabel.setText("Company Name:");
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup tg = new ToggleGroup();
        inHouseRadioButton.setToggleGroup(tg);
        inHouseRadioButton.setSelected(true);
        outsourcedRadioButton.setToggleGroup(tg);
        
        if(!MainFXMLController.product && MainFXMLController.productPartIndex != -1) {
            Part p = MainApp.inventoryInstance.getAllParts().get(MainFXMLController.productPartIndex);
            idField.setText(p.getId() + "");
            nameField.setText(p.getName() + "");
            priceField.setText(p.getPrice() + "");
            inventoryLevelField.setText(p.getStock() + "");
            minValueField.setText(p.getMin() + "");
            maxValueField.setText(p.getMax() + "");
            try {
                if(((InHouse) p).getMachineId() != -1) {
                    InHouse temp = (InHouse) p;
                    companyNameField.setText(temp.getMachineId() + "");
                    companyNameLabel.setText("Machine ID:");
                }
            } catch(ClassCastException cce) {
                if(((Outsourced) p).getCompanyName() != null) {
                    Outsourced temp = (Outsourced) p;
                    outsourcedRadioButton.setSelected(true);
                    companyNameField.setText(temp.getCompanyName());
                    companyNameLabel.setText("Company Name:");
                }
            } finally {
                companyNameField.setVisible(false);
                companyNameLabel.setVisible(false);
            }
        } else if(MainFXMLController.productPartIndex == -1) {
            companyNameLabel.setText("Machine ID:");
        }
    }
}
