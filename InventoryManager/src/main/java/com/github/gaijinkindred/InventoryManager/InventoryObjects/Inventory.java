/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.gaijinkindred.InventoryManager.InventoryObjects;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author gaijin
 */
public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;
    
    public Inventory() {
        //Setup ObservableList for parts
        ArrayList<Part> parts = new ArrayList<Part>();
        allParts = FXCollections.observableList(parts);
        
        //Setup ObservableList for Products
        ArrayList<Product> products = new ArrayList<Product>();
        allProducts = FXCollections.observableList(products);
    }
    
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    public Part lookupPart(int partId) {
        for(Part p : allParts) {
            if(p.getId() == partId) {
                return p;
            }
        }
        return null;
    }
    
    public Product lookupProduct(int productId) {
        for(Product p : allProducts) {
            if(p.getId() == productId) {
                return p;
            }
        }
        return null;
    }
    
    public ArrayList<Part> lookupPart(String partName) {
        ArrayList<Part> parts = new ArrayList<Part>();
        for(Part p : allParts) {
            if(p.getName().equalsIgnoreCase(partName)) {
                parts.add(p);
            }
        }
        return parts;
    }
    
    public ArrayList<Product> lookupProduct(String productName) {
        ArrayList<Product> products = new ArrayList<Product>();
        for(Product p : allProducts) {
            if(p.getName().equalsIgnoreCase(productName)) {
                products.add(p);
            }
        }
        return products;
    }
    
    public void updatePart(int index, Part selectedPart) {
        Part p = allParts.get(index);
        p.setName(selectedPart.getName());
        p.setPrice(selectedPart.getPrice());
        p.setId(selectedPart.getId());
        //TODO: more getters and setters
    }
    
    public void updateProduct(int index, Product selectedProduct) {
        Product p = allProducts.get(index);
        p.setName(selectedProduct.getName());
        p.setPrice(selectedProduct.getPrice());
        p.setId(selectedProduct.getId());
        //TODO: more getters and setters
    }
    
    public void deletePart(Part selectedPart) {
        int p = allParts.indexOf(selectedPart);
        if(p > -1 && selectedPart.getId() == allParts.get(p).getId()) {
            allParts.remove(allParts.indexOf(selectedPart));
        }
    }
    
    public void deleteProduct(Product selectedProduct) {
        int p = allProducts.indexOf(selectedProduct);
        if(p > -1 && selectedProduct.getId() == allProducts.get(p).getId()) {
            allProducts.remove(p);
        }
    }
    
    public ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
