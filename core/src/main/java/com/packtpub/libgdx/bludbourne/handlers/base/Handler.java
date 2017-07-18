package com.packtpub.libgdx.bludbourne.handlers.base;

/**
 * Created on 03.05.2017.
 *
 * @param <S> - handled subject
 * @param <M> - sent object
 */
public interface Handler<S, M> {

    void handle(S s, M m, Object... args);


}
