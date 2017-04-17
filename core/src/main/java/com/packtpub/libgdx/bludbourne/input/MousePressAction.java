package com.packtpub.libgdx.bludbourne.input;

import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.support.QuadConsumer;

/**
 * Facade over {@link QuadConsumer} may not be needed
 * <p>
 * Created on 11.04.2017.
 */
public interface MousePressAction extends QuadConsumer<Float, Float, Entity, Float> {
}
