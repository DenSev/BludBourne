package com.packtpub.libgdx.bludbourne.observers;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.entity.EntityConfig;

/**
 * Created on 17.07.2017.
 */

public class EntitySubject {
    private Array<EntityObserver> observers;

    public EntitySubject() {
        observers = new Array<EntityObserver>();
    }

    public void addObserver(EntityObserver conversationObserver) {
        observers.add(conversationObserver);
    }

    public void removeObserver(EntityObserver conversationObserver) {
        observers.removeValue(conversationObserver, true);
    }

    public void removeAllObservers() {
        for (EntityObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    public void notify(final EntityConfig config, EntityObserver.ComponentEvent event) {
        for (EntityObserver observer : observers) {
            observer.onNotify(config, event);
        }
    }
}

