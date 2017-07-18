package com.packtpub.libgdx.bludbourne.observers;

import com.packtpub.libgdx.bludbourne.inventory.InventorySlot;

/**
 * Created on 17.07.2017.
 */
public interface InventorySlotSubject {

    void addObserver(InventorySlotObserver inventorySlotObserver);

    void removeObserver(InventorySlotObserver inventorySlotObserver);

    void removeAllObservers();

    void notify(final InventorySlot slot, InventorySlotObserver.SlotEvent event);
}

