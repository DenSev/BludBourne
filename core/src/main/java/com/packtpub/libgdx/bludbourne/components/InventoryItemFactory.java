package com.packtpub.libgdx.bludbourne.components;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Scaling;
import com.packtpub.libgdx.bludbourne.inventory.InventoryItem;
import com.packtpub.libgdx.bludbourne.inventory.ItemTypeID;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import com.packtpub.libgdx.bludbourne.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Inventory item factory, creates map of inventory items from json file
 * <p>
 * Created on 04/27/2017.
 */
public enum InventoryItemFactory {

    INSTANCE;

    private final String INVENTORY_ITEM = "scripts/inventory_items.json";
    private final Map<ItemTypeID, InventoryItem> inventoryItemList;

    InventoryItemFactory() {
        ArrayList<JsonValue> list = MapperProvider.INSTANCE.mapper().fromJson(ArrayList.class, Gdx.files.internal(INVENTORY_ITEM));
        inventoryItemList = new HashMap<>();

        for (JsonValue jsonVal : list) {
            InventoryItem inventoryItem = MapperProvider.INSTANCE.mapper().readValue(InventoryItem.class, jsonVal);
            inventoryItemList.put(inventoryItem.getItemTypeID(), inventoryItem);
        }
    }

    public InventoryItem getInventoryItem(ItemTypeID inventoryItemType) {
        InventoryItem item = new InventoryItem(inventoryItemList.get(inventoryItemType));
        item.setDrawable(new TextureRegionDrawable(Utility.ITEMS_TEXTUREATLAS.findRegion(item.getItemTypeID().toString())));
        item.setScaling(Scaling.none);
        return item;
    }
}
