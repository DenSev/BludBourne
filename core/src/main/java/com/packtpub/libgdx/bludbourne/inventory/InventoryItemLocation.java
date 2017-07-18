package com.packtpub.libgdx.bludbourne.inventory;

/**
 * Created on 04/27/2017.
 */
public class InventoryItemLocation {
    private int locationIndex;
    private String itemTypeAtLocation;
    private int numberItemsAtLocation;

    public InventoryItemLocation() {
    }

    public InventoryItemLocation(int locationIndex, String itemTypeAtLocation, int numberItemsAtLocation) {
        this.locationIndex = locationIndex;
        this.itemTypeAtLocation = itemTypeAtLocation;
        this.numberItemsAtLocation = numberItemsAtLocation;
    }

    public String getItemTypeAtLocation() {
        return itemTypeAtLocation;
    }

    public void setItemTypeAtLocation(String itemTypeAtLocation) {
        this.itemTypeAtLocation = itemTypeAtLocation;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public int getNumberItemsAtLocation() {
        return numberItemsAtLocation;
    }

    public void setNumberItemsAtLocation(int numberItemsAtLocation) {
        this.numberItemsAtLocation = numberItemsAtLocation;
    }
}
