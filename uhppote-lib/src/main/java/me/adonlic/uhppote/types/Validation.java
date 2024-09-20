package me.adonlic.uhppote.types;

public enum Validation {
    NO_PASS(0),
    PASS(1);

    private final int value;

    Validation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Validation fromInt(int value) {
        return value == 1 ? PASS : NO_PASS;
    }
}
