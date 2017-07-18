package com.packtpub.libgdx.bludbourne.observers;

/**
 * Created on 17.07.2017.
 */
public interface StatusSubject {
    void addObserver(StatusObserver statusObserver);

    void removeObserver(StatusObserver statusObserver);

    void removeAllObservers();

    void notify(final int value, StatusObserver.StatusEvent event);
}

