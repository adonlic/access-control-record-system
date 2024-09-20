package me.adonlic.uhppote.types;

public enum InOut {
    IN(1),
    OUT(2);

    private final int value;

    InOut(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static InOut fromInt(int value) {
        return value == 2 ? OUT : IN;
    }
}
