package com.packtpub.libgdx.bludbourne.components.base;


import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.function.Consumer;

public abstract class Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(Component.class);
    protected final EnumMap<MESSAGE, Consumer<Object[]>> handlers = new EnumMap<>(MESSAGE.class);

    public enum MESSAGE {
        CURRENT_POSITION,
        INIT_START_POSITION,
        CURRENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED
    }

    public abstract void dispose();

    public void receiveMessage(MESSAGE message, Object... args) {
        LOGGER.trace("Message received {}, with args {}", message, args);

        //Specifically for messages with 1 object payload
        if (ArrayUtils.isNotEmpty(args) && handlers.containsKey(message)) {
            handlers.get(message).accept(args);
        }
    }
}
