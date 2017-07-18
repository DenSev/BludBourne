package com.packtpub.libgdx.bludbourne.observers;

import com.packtpub.libgdx.bludbourne.conversation.ConversationGraph;

/**
 * Created on 10.07.2017.
 */
public interface ConversationGraphObserver {

    enum ConversationCommandEvent {
        LOAD_STORE_INVENTORY,
        EXIT_CONVERSATION,
        NONE
    }

    void onNotify(final ConversationGraph graph, ConversationCommandEvent event);
}
