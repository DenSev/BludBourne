package com.packtpub.libgdx.bludbourne.components.base;

import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;

/**
 * Created on 16.04.2017.
 */
public abstract class BehaviorComponent extends Component {

    protected Direction currentDirection = null;
    protected State currentState = null;

    public abstract void update(Entity entity, float delta);

    protected  BehaviorComponent(){

    }
}
