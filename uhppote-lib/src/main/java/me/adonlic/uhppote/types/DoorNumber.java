package me.adonlic.uhppote.types;

public enum DoorNumber {
    DOOR_1(1),
    DOOR_2(2),
    DOOR_3(3),
    DOOR_4(4);

    private final int value;

    DoorNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DoorNumber fromInt(int value) {
        for (DoorNumber door : values()) {
            if (door.value == value) {
                return door;
            }
        }
        return DOOR_1; // default to DOOR_1
    }
}
