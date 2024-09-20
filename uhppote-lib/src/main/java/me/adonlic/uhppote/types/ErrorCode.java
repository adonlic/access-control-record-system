package me.adonlic.uhppote.types;

public enum ErrorCode {
    NO_ERROR(0),
    ADJUST_TIME(1);

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ErrorCode fromInt(int value) {
        return value > 0 ? ADJUST_TIME : NO_ERROR;
    }
}
