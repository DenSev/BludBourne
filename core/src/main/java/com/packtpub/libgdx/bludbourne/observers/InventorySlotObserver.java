package com.packtpub.libgdx.bludbourne.observers;

import com.packtpub.libgdx.bludbourne.inventory.InventorySlot;

/**
 * Created on 17.07.2017.
 */
public interface InventorySlotObserver {
    enum SlotEvent {
        ADDED_ITEM,
        REMOVED_ITEM
    }

    void onNotify(final InventorySlot slot, SlotEvent event);
}
