package com.packtpub.libgdx.bludbourne.observers;

/**
 * Created on 17.07.2017.
 */
public interface StoreInventorySubject {
    void addObserver(StoreInventoryObserver storeObserver);

    void removeObserver(StoreInventoryObserver storeObserver);

    void removeAllObservers();

    void notify(String value, StoreInventoryObserver.StoreInventoryEvent event);
}
