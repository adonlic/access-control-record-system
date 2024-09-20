package me.adonlic.uhppote.types;

public enum InputStatus {
    FORCE_LOCK(0),
    FIRE(1);

    private final int value;

    InputStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static InputStatus fromInt(int value) {
        return value == 1 ? FIRE : FORCE_LOCK;
    }
}
