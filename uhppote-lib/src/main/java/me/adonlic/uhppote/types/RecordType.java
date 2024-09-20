package me.adonlic.uhppote.types;

public enum RecordType {
    NO_RECORD(0),
    SWIPE_RECORD(1),
    DOOR_STATUS_EVENT(2),
    WARN_EVENT(3);

    private final int value;

    RecordType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RecordType fromInt(int value) {
        for (RecordType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return NO_RECORD; // default
    }
}
