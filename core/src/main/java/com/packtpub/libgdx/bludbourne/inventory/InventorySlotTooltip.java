package com.packtpub.libgdx.bludbourne.inventory;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Created on 04/27/2017.
 */
public class InventorySlotTooltip extends Window {

    private Skin skin;
    private Label description;

    public InventorySlotTooltip(final Skin skin) {
        super("", skin);
        this.skin = skin;

        description = new Label("", skin, "inventory-item-count");

        this.add(description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }

    public void setVisible(InventorySlot inventorySlot, boolean visible) {
        super.setVisible(visible);

        if (inventorySlot == null) {
            return;
        }

        if (!inventorySlot.hasItem()) {
            super.setVisible(false);
        }
    }

    public void updateDescription(InventorySlot inventorySlot) {
        if (inventorySlot.hasItem()) {
            StringBuilder string = new StringBuilder();
            string.append(inventorySlot.getTopInventoryItem().getItemShortDescription());
            string.append(System.getProperty("line.separator"));
            string.append(String.format("Original Value: %s GP", inventorySlot.getTopInventoryItem().getItemValue()));
            string.append(System.getProperty("line.separator"));
            string.append(String.format("Trade Value: %s GP", inventorySlot.getTopInventoryItem().getTradeValue()));
            description.setText(string);

            //description.setText(inventorySlot.getTopInventoryItem().getItemShortDescription());
            this.pack();
        } else {
            description.setText("");
            this.pack();
        }

    }
}
