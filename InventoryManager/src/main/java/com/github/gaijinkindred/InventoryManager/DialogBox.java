/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.gaijinkindred.InventoryManager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author gaijin
 */
public class DialogBox implements Initializable {

    @FXML private Label content;
    
    @FXML
    private void dismiss(ActionEvent event) {
        MainApp.dismissRecentStage();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content.setText(MainApp.notificationDialog);
    }
}
