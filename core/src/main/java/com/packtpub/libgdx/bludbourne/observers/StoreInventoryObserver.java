package com.packtpub.libgdx.bludbourne.observers;

/**
 * Created on 17.07.2017.
 */
public interface StoreInventoryObserver {
    enum StoreInventoryEvent {
        PLAYER_GP_TOTAL_UPDATED,
        PLAYER_INVENTORY_UPDATED
    }

    void onNotify(String value, StoreInventoryEvent event);
}

