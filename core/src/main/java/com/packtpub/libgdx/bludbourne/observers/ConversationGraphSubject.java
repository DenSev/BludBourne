package com.packtpub.libgdx.bludbourne.observers;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.conversation.ConversationGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 10.07.2017.
 */
public class ConversationGraphSubject {

    private static final Logger LOG = LoggerFactory.getLogger(ConversationGraphSubject.class);

    private Array<ConversationGraphObserver> observers;

    public ConversationGraphSubject() {
        observers = new Array<ConversationGraphObserver>();
    }

    public void addObserver(ConversationGraphObserver graphObserver) {
        observers.add(graphObserver);
    }

    public void removeObserver(ConversationGraphObserver graphObserver) {
        observers.removeValue(graphObserver, true);
    }

    public void removeAllObservers() {
        for (ConversationGraphObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    public void notify(final ConversationGraph graph, ConversationGraphObserver.ConversationCommandEvent event) {
        LOG.debug("Notifying observers {} for event: {}", observers.toString(), event.toString());
        for (ConversationGraphObserver observer : observers) {
            observer.onNotify(graph, event);
        }
    }
}
