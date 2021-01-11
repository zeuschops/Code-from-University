package com.github.gaijinkindred.InventoryManager;

import com.github.gaijinkindred.InventoryManager.InventoryObjects.InHouse;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Inventory;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Outsourced;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Part;
import com.github.gaijinkindred.InventoryManager.InventoryObjects.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    //I do this because the UML says I can't put this many other places (i.e. in the Inventory class)
    public static Inventory inventoryInstance = new Inventory(); //Used to manage the inventory, just instancing storage
    public static ArrayList<Stage> childStages = new ArrayList<Stage>(); //Used to close windows, just instancing storage
    public static String notificationDialog = ""; //Used for DialogBox.fxml
    
    //Managing childStages/jfx windows
    public static void dismissRecentStage() {
        if(childStages.size() > 0) {
            Stage s = childStages.get(childStages.size() - 1);
            s.close();
            childStages.remove(s);
        }
    }
    
    //Managing childStages/jfx windows
    public static void addChildStage(Stage stage) {
        childStages.add(stage);
    }
    
    //Starts the initial program
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/DialogBox.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }
    
    //Setup some test data:
    public static void createTestData() {
        //Create variables
        Product productA = new Product(1234, "Product A", 60.01, 5, 0, 20);
        InHouse associatedPartA = new InHouse(1235, "Associated Part A", 4.99, 10, 0, 40, 2277);
        InHouse associatedPartB = new InHouse(1236, "Associated Part B", 1.99, 5, 0, 20, 1337);
        
        Product productB = new Product(1237, "Product B", 60.01, 5, 0, 20);
        Outsourced associatedPartC = new Outsourced(1238, "Associated Part C", 4.99, 10, 0, 40, "ACME Building Co.");
        Outsourced associatedPartD = new Outsourced(1239, "Associated Part D", 1.99, 5, 0, 20, "Discordian Disbelief Building Company, LLC");
        Outsourced associatedPartE = new Outsourced(1240, "Associated Part E", 1.99, 5, 0, 20, "Freudian Insurance Claims, LLC");
        
        Product productC = new Product(1241, "Product C", 60.01, 5, 0, 20);
        Part associatedPartF = new Part(1242, "Associated Part F", 4.99, 10, 0, 40);
        Part associatedPartG = new Part(1243, "Associated Part G", 1.99, 5, 0, 20);
        Part associatedPartH = new Part(1244, "Associated Part H", 4.99, 10, 0, 40);
        Part associatedPartI = new Part(5241, "Associated Part I", 1.99, 5, 0, 20);
        
        //Testing random integers instead of doubles, and testing unassociated Parts and Products
        Product productD = new Product(4444, "Product D", 120, 3, 1, 4);
        Product productE = new Product(4455, "Product E", 50, 3, 1, 4);
        Product productF = new Product(4466, "Product F", 550, 3, 1, 4);
        Outsourced partJ = new Outsourced(5241, "Associated Part J", 1999, 20, 0, 20, "The Amazonian Parts Store, LLC");
        Outsourced partK = new Outsourced(5241, "Associated Part K", 299, 0, 0, 20, "RangerParts, INC");
        Outsourced partL = new Outsourced(5241, "Associated Part L", 399, 3, 0, 20, "FitzCentral, LLC");
        InHouse partM = new InHouse(5241, "Associated Part M", 499, 7, 0, 20, 7787);
        InHouse partN = new InHouse(5241, "Associated Part N", 49, 17, 0, 20, 2098);
        
        //Associate Products with Parts
        productA.addAssociatedPart(associatedPartA);
        productA.addAssociatedPart(associatedPartB);
        
        productB.addAssociatedPart(associatedPartC);
        productB.addAssociatedPart(associatedPartD);
        productB.addAssociatedPart(associatedPartE);
        
        productC.addAssociatedPart(associatedPartF);
        productC.addAssociatedPart(associatedPartG);
        productC.addAssociatedPart(associatedPartH);
        productC.addAssociatedPart(associatedPartI);
        
        //Add these things into the Inventory
        inventoryInstance.addProduct(productA);
        inventoryInstance.addProduct(productB);
        inventoryInstance.addProduct(productC);
        inventoryInstance.addProduct(productD);
        inventoryInstance.addProduct(productE);
        inventoryInstance.addProduct(productF);
        inventoryInstance.addPart(associatedPartA);
        inventoryInstance.addPart(associatedPartB);
        inventoryInstance.addPart(associatedPartC);
        inventoryInstance.addPart(associatedPartD);
        inventoryInstance.addPart(associatedPartE);
        inventoryInstance.addPart(associatedPartF);
        inventoryInstance.addPart(associatedPartG);
        inventoryInstance.addPart(associatedPartH);
        inventoryInstance.addPart(associatedPartI);
        inventoryInstance.addPart(partJ);
        inventoryInstance.addPart(partK);
        inventoryInstance.addPart(partL);
        inventoryInstance.addPart(partM);
        inventoryInstance.addPart(partN);
    }
    
    public static void main(String[] args) {
        createTestData(); //This should run prior to the JavaFX file
        launch(args);
    }
}
