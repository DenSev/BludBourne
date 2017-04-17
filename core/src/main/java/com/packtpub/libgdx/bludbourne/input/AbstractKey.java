package com.packtpub.libgdx.bludbourne.input;

/**
 * Created on 11.04.2017.
 */
public class AbstractKey {

    private final String uid;
    private final int[] keycodes;
    private boolean isPressed = false;

    public AbstractKey(final String uid, final int[] keycodes) {
        this.uid = uid;
        this.keycodes = keycodes;
    }

    public String getUid() {
        return uid;
    }

    public void pressed() {
        this.isPressed = true;
    }

    public void released() {
        this.isPressed = false;
    }

    public boolean isPressed() {
        return this.isPressed;
    }

    public boolean hasKeycode(int keycode) {
        for (int keycode1 : keycodes) {
            if (keycode1 == keycode) {
                return true;
            }
        }
        return false;
    }
}
