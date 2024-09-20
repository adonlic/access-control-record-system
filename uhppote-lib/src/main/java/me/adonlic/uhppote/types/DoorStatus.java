package me.adonlic.uhppote.types;

public enum DoorStatus {
    CLOSED(0),
    OPEN(1);

    private final int value;

    DoorStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DoorStatus fromInt(int value) {
        return value == 1 ? OPEN : CLOSED;
    }
}
