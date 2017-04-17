package com.packtpub.libgdx.bludbourne.support;

import java.util.Objects;

/**
 * Support functional interface based on {@link java.util.function.BiConsumer}
 * <p>
 * Created on 11.04.2017.
 */
@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {

    void accept(A a, B b, C c, D d);

    default QuadConsumer<A, B, C, D> andThen(QuadConsumer<? super A, ? super B, ? super C, ? super D> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}
