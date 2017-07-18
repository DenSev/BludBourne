package com.packtpub.libgdx.bludbourne.inventory;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.packtpub.libgdx.bludbourne.observers.InventorySlotObserver;
import com.packtpub.libgdx.bludbourne.observers.InventorySlotObserver.SlotEvent;
import com.packtpub.libgdx.bludbourne.observers.InventorySlotSubject;
import com.packtpub.libgdx.bludbourne.utility.Utility;

/**
 * Created on 04/27/2017.
 */
public class InventorySlot extends Stack implements InventorySlotSubject {

    //All slots have this default image
    private Stack defaultBackground;
    private Image customBackgroundDecal;
    private Label numItemsLabel;
    private int numItemsVal = 0;
    private int filterItemType;
    private Array<InventorySlotObserver> observers;

    public InventorySlot() {
        filterItemType = 0; //filter nothing
        defaultBackground = new Stack();
        customBackgroundDecal = new Image();
        observers = new Array<InventorySlotObserver>();
        Image image = new Image(new NinePatch(Utility.STATUSUI_TEXTUREATLAS.createPatch("dialog")));

        numItemsLabel = new Label(String.valueOf(numItemsVal), Utility.STATUSUI_SKIN, "inventory-item-count");
        numItemsLabel.setAlignment(Align.bottomRight);
        numItemsLabel.setVisible(false);

        defaultBackground.add(image);

        defaultBackground.setName("background");
        numItemsLabel.setName("numitems");

        this.add(defaultBackground);
        this.add(numItemsLabel);
    }

    public InventorySlot(int filterItemType, Image customBackgroundDecal) {
        this();
        this.filterItemType = filterItemType;
        this.customBackgroundDecal = customBackgroundDecal;
        defaultBackground.add(this.customBackgroundDecal);
    }

    public void decrementItemCount() {
        numItemsVal--;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if (defaultBackground.getChildren().size == 1) {
            defaultBackground.add(customBackgroundDecal);
        }
        checkVisibilityOfItemCount();
        notify(this, SlotEvent.REMOVED_ITEM);
    }

    public void incrementItemCount() {
        numItemsVal++;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if (defaultBackground.getChildren().size > 1) {
            defaultBackground.getChildren().pop();
        }
        checkVisibilityOfItemCount();
        notify(this, SlotEvent.ADDED_ITEM);
    }

    @Override
    public void add(Actor actor) {
        super.add(actor);

        if (numItemsLabel == null) {
            return;
        }

        if (!actor.equals(defaultBackground) && !actor.equals(numItemsLabel)) {
            incrementItemCount();
        }
    }

    public void add(Array<Actor> array) {
        for (Actor actor : array) {
            super.add(actor);

            if (numItemsLabel == null) {
                return;
            }

            if (!actor.equals(defaultBackground) && !actor.equals(numItemsLabel)) {
                incrementItemCount();
            }
        }
    }

    public Array<Actor> getAllInventoryItems() {
        Array<Actor> items = new Array<Actor>();
        if (hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = arrayChildren.size - 2;
            for (int i = 0; i < numInventoryItems; i++) {
                items.add(arrayChildren.pop());
                decrementItemCount();
            }
        }
        return items;
    }

    public void clearAllInventoryItems() {
        if (hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            int numInventoryItems = getNumItems();
            for (int i = 0; i < numInventoryItems; i++) {
                arrayChildren.pop();
                decrementItemCount();
            }
        }
    }

    private void checkVisibilityOfItemCount() {
        if (numItemsVal < 2) {
            numItemsLabel.setVisible(false);
        } else {
            numItemsLabel.setVisible(true);
        }
    }

    public boolean hasItem() {
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 2) {
                return true;
            }
        }
        return false;
    }

    public int getNumItems() {
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            return items.size - 2;
        }
        return 0;
    }

    public boolean doesAcceptItemUseType(int itemUseType) {
        return filterItemType == 0 || ((filterItemType & itemUseType) == itemUseType);
    }

    public InventoryItem getTopInventoryItem() {
        InventoryItem actor = null;
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            if (items.size > 2) {
                actor = (InventoryItem) items.peek();
            }
        }
        return actor;
    }

    public void swapSlots(InventorySlot inventorySlotTarget, InventoryItem dragActor) {
        //check if items can accept each other, otherwise, no swap
        if (!inventorySlotTarget.doesAcceptItemUseType(dragActor.getItemUseType()) ||
            !this.doesAcceptItemUseType(inventorySlotTarget.getTopInventoryItem().getItemUseType())) {
            this.add(dragActor);
            return;
        }

        //swap
        Array<Actor> tempArray = this.getAllInventoryItems();
        tempArray.add(dragActor);
        this.add(inventorySlotTarget.getAllInventoryItems());
        inventorySlotTarget.add(tempArray);
    }


    public void updateAllInventoryItemNames(String name) {
        if (hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for (int i = arrayChildren.size - 1; i > 1; i--) {
                arrayChildren.get(i).setName(name);
            }
        }
    }

    public void removeAllInventoryItemsWithName(String name) {
        if (hasItem()) {
            SnapshotArray<Actor> arrayChildren = this.getChildren();
            //skip first two elements
            for (int i = arrayChildren.size - 1; i > 1; i--) {
                String itemName = arrayChildren.get(i).getName();
                if (itemName.equalsIgnoreCase(name)) {
                    decrementItemCount();
                    arrayChildren.removeIndex(i);
                }
            }
        }
    }

    public int getNumItems(String name) {
        if (hasChildren()) {
            SnapshotArray<Actor> items = this.getChildren();
            int totalFilteredSize = 0;
            for (Actor actor : items) {
                if (name.equalsIgnoreCase(actor.getName())) {
                    totalFilteredSize++;
                }
            }
            return totalFilteredSize;
        }
        return 0;
    }


    @Override
    public void addObserver(InventorySlotObserver slotObserver) {
        observers.add(slotObserver);
    }

    @Override
    public void removeObserver(InventorySlotObserver slotObserver) {
        observers.removeValue(slotObserver, true);
    }

    @Override
    public void removeAllObservers() {
        for (InventorySlotObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event) {
        for (InventorySlotObserver observer : observers) {
            observer.onNotify(slot, event);
        }
    }
}
