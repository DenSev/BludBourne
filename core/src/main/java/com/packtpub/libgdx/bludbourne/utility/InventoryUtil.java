package com.packtpub.libgdx.bludbourne.utility;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.components.InventoryItemFactory;
import com.packtpub.libgdx.bludbourne.inventory.InventoryItem;
import com.packtpub.libgdx.bludbourne.inventory.InventoryItemLocation;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlot;
import com.packtpub.libgdx.bludbourne.inventory.InventorySlotSource;
import com.packtpub.libgdx.bludbourne.inventory.ItemTypeID;

/**
 * Created on 07/18/2017.
 */
public enum InventoryUtil {
    ;

    public static void setInventoryItemNames(Table targetTable, String name) {
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            inventorySlot.updateAllInventoryItemNames(name);
        }
    }

    public static Array<InventoryItemLocation> getInventory(Table sourceTable, Table targetTable, String name) {
        Array<InventoryItemLocation> items = getInventory(targetTable, name);
        Array<Cell> sourceCells = sourceTable.getCells();
        int index = 0;
        for (InventoryItemLocation item : items) {
            for (; index < sourceCells.size; index++) {
                InventorySlot inventorySlot = ((InventorySlot) sourceCells.get(index).getActor());
                if (inventorySlot == null) continue;
                int numItems = inventorySlot.getNumItems(name);
                if (numItems == 0) {
                    item.setLocationIndex(index);
                    index++;
                    break;
                }
            }
            if (index == sourceCells.size) {
                item.setLocationIndex(index - 1);
            }
        }
        return items;
    }

    public static Array<InventoryItemLocation> getInventory(Table targetTable, String name) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) {
                continue;
            }
            int numItems = inventorySlot.getNumItems(name);
            if (numItems > 0) {
                //System.out.println("[i] " + i + " itemtype: " + inventorySlot.getTopInventoryItem().getItemTypeID().toString() + " numItems " + numItems);
                items.add(new InventoryItemLocation(
                    i,
                    inventorySlot.getTopInventoryItem().getItemTypeID().toString(),
                    numItems));
            }
        }
        return items;
    }

    public static Array<InventoryItemLocation> removeInventoryItems(String name, Table inventoryTable) {
        Array<Cell> cells = inventoryTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            inventorySlot.removeAllInventoryItemsWithName(name);
        }
        return items;
    }

    public static Array<InventoryItemLocation> getInventory(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        Array<InventoryItemLocation> items = new Array<InventoryItemLocation>();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = ((InventorySlot) cells.get(i).getActor());
            if (inventorySlot == null) continue;
            int numItems = inventorySlot.getNumItems();
            if (numItems > 0) {
                items.add(new InventoryItemLocation(
                    i,
                    inventorySlot.getTopInventoryItem().getItemTypeID().toString(),
                    numItems));
            }
        }
        return items;
    }

    public static void clearInventoryItems(Table targetTable) {
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < cells.size; i++) {
            InventorySlot inventorySlot = (InventorySlot) cells.get(i).getActor();
            if (inventorySlot == null) continue;
            inventorySlot.clearAllInventoryItems();
        }
    }

    public static void populateInventory(Table targetTable, Array<InventoryItemLocation> inventoryItems, DragAndDrop dragAndDrop) {
        Array<Cell> cells = targetTable.getCells();
        for (int i = 0; i < inventoryItems.size; i++) {
            InventoryItemLocation itemLocation = inventoryItems.get(i);
            ItemTypeID itemTypeID = ItemTypeID.valueOf(itemLocation.getItemTypeAtLocation());
            InventorySlot inventorySlot = ((InventorySlot) cells.get(itemLocation.getLocationIndex()).getActor());
            inventorySlot.clearAllInventoryItems();

            for (int index = 0; index < itemLocation.getNumberItemsAtLocation(); index++) {
                InventoryItem item = InventoryItemFactory.INSTANCE.getInventoryItem(itemTypeID);
                item.setName(targetTable.getName());
                inventorySlot.add(item);
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot, dragAndDrop));
            }
        }
    }
}
