package com.packtpub.libgdx.bludbourne.handlers;

import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.handlers.base.BehaviorHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCBehaviorHandler extends BehaviorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NPCBehaviorHandler.class);
    private static final NPCBehaviorHandler instance = new NPCBehaviorHandler();

    public static BehaviorHandler instance() {
        return instance;
    }

    private NPCBehaviorHandler() {
        handlers.put(
            Message.COLLISION_WITH_MAP,
            (entity, args) -> entity.getEntityState().setCurrentDirection(Direction.getRandomNext())
        );
        handlers.put(
            Message.COLLISION_WITH_ENTITY,
            (entity, args) -> entity.getEntityState().setCurrentState(State.IDLE)
        );
        handlers.put(
            Message.INIT_STATE,
            (entity, args) -> entity.getEntityState().setCurrentState((State) args[0])
        );
        handlers.put(
            Message.INIT_DIRECTION,
            (entity, args) -> entity.getEntityState().setCurrentDirection((Direction) args[0])
        );
    }

    @Override
    public void update(Entity entity, float delta) {

        //If IMMOBILE, don't update anything
        if (entity.getEntityState().getCurrentState() == State.IMMOBILE) {
            entity.sendMessage(Message.CURRENT_STATE, State.IMMOBILE);
            return;
        }

        entity.getEntityState().setFrameTime(entity.getEntityState().getFrameTime() + delta);

        //Change direction after so many seconds
        if (entity.getEntityState().getFrameTime() > MathUtils.random(1, 5)) {
            entity.getEntityState().setCurrentState(State.getRandomNext());
            entity.getEntityState().setCurrentDirection(Direction.getRandomNext());
            entity.getEntityState().setFrameTime(0.0f);
        }

        if (entity.getEntityState().getCurrentState().equals(State.IDLE)) {
            entity.sendMessage(Message.CURRENT_STATE, State.IDLE);
            return;
        }

        entity.sendMessage(Message.CURRENT_STATE, State.WALKING);
        //TODO what?
        //entity.sendMessage(Message.CURRENT_DIRECTION, entity.getEntityState().getCurrentDirection());
    }

    public void dispose() {
    }
}
