package me.adonlic.wiegand.util;

public class Enums {

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

    public enum Status {
        CLOSED(0),
        OPEN(1);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status fromInt(int value) {
            return value == 1 ? OPEN : CLOSED;
        }
    }

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
}
