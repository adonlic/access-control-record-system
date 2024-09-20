package me.adonlic.uhppote.types;

public enum DoorControlState {
    NORMALLY_OPEN(0x01, "Normally Open"),        // Door stays open
    NORMALLY_CLOSED(0x02, "Normally Closed"),    // Door stays closed
    CONTROLLED_AUTOMATICALLY(0x03, "Controlled Automatically"); // Door follows system rules

    private final int value;
    private final String description;

    DoorControlState(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static DoorControlState fromInt(int value) {
        for (DoorControlState state : DoorControlState.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown control state value: " + value);
    }

    @Override
    public String toString() {
        return description;
    }
}
