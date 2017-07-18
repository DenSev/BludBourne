package com.packtpub.libgdx.bludbourne.handlers.base;

import com.packtpub.libgdx.bludbourne.entity.Entity;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.function.BiConsumer;

/**
 * Created on 03.05.2017.
 */
public abstract class EntityMessageHandler implements Handler<Entity, Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMessageHandler.class);
    protected final EnumMap<Message, BiConsumer<Entity, Object[]>> handlers = new EnumMap<>(Message.class);

    @Override
    public void handle(Entity e, Message message, Object... args) {
        LOGGER.trace("Message received {}, with args {}", message, args);

        if (ArrayUtils.isNotEmpty(args) && handlers.containsKey(message)) {
            handlers.get(message).accept(e, args);
        } else if (handlers.containsKey(message)) {
            handlers.get(message).accept(e, null);
        }
    }

    public abstract void dispose();

}
