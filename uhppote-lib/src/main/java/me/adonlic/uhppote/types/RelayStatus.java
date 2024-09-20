package me.adonlic.uhppote.types;

public enum RelayStatus {
    LOCK(0),
    UNLOCK(1);

    private final int value;

    RelayStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RelayStatus fromInt(int value) {
        return value == 1 ? UNLOCK : LOCK;
    }
}
