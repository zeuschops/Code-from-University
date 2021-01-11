/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.gaijinkindred.InventoryManager.InventoryObjects;

/**
 *
 * @author gaijin
 */
public class InHouse extends Part {
    private int machineId = -1;
    
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    
    public void setMachine(int machineId) {
        this.machineId = machineId;
    }
    
    public int getMachineId() {
        return machineId;
    }
}
