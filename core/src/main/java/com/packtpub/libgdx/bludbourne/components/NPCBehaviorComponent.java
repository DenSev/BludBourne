package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.bludbourne.components.base.BehaviorComponent;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCBehaviorComponent extends BehaviorComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(NPCBehaviorComponent.class);

    private float frameTime = 0.0f;

    public NPCBehaviorComponent() {
        handlers.put(MESSAGE.COLLISION_WITH_MAP, (Object... args) -> currentDirection = Direction.getRandomNext());
        handlers.put(MESSAGE.COLLISION_WITH_ENTITY, (Object... args) -> currentState = State.IDLE);
        handlers.put(MESSAGE.INIT_STATE, (Object... args) -> currentState = (State) args[0]);
        handlers.put(MESSAGE.INIT_DIRECTION, (Object... args) -> currentDirection = (Direction) args[0]);
        currentDirection = Direction.getRandomNext();
        currentState = State.WALKING;
    }

    @Override
    public void update(Entity entity, float delta) {

        //If IMMOBILE, don't update anything
        if (currentState == State.IMMOBILE) {
            entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.IMMOBILE);
            return;
        }

        frameTime += delta;

        //Change direction after so many seconds
        if (frameTime > MathUtils.random(1, 5)) {
            currentState = State.getRandomNext();
            currentDirection = Direction.getRandomNext();
            frameTime = 0.0f;
        }

        if (currentState == State.IDLE) {
            entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.IDLE);
            return;
        }

        entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.WALKING);
        entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, currentDirection);
    }

    @Override
    public void dispose() {
    }
}
