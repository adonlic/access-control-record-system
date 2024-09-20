package me.adonlic.uhppote.types;

public enum SwipeReason {
    SWIPE_PASS(0x01, "SwipePass", "Swipe"),
    RESERVED_02(0x02, "(Reserved)", ""),
    RESERVED_03(0x03, "(Reserved)", ""),
    RESERVED_04(0x04, "(Reserved)", ""),
    SWIPE_NO_PASS_PC_CONTROL(0x05, "SwipeNOPass", "Denied Access: PC Control"),
    SWIPE_NO_PASS_NO_PRIVILEGE(0x06, "SwipeNOPass", "Denied Access: No PRIVILEGE"),
    SWIPE_NO_PASS_WRONG_PASSWORD(0x07, "SwipeNOPass", "Denied Access: Wrong PASSWORD"),
    SWIPE_NO_PASS_ANTI_BACK(0x08, "SwipeNOPass", "Denied Access: AntiBack"),
    SWIPE_NO_PASS_MORE_CARDS(0x09, "SwipeNOPass", "Denied Access: More Cards"),
    SWIPE_NO_PASS_FIRST_CARD_OPEN(0x0A, "SwipeNOPass", "Denied Access: First Card Open"),
    SWIPE_NO_PASS_DOOR_SET_NC(0x0B, "SwipeNOPass", "Denied Access: Door Set NC"),
    SWIPE_NO_PASS_INTERLOCK(0x0C, "SwipeNOPass", "Denied Access: InterLock"),
    SWIPE_NO_PASS_LIMITED_TIMES(0x0D, "SwipeNOPass", "Denied Access: Limited Times"),
    RESERVED_0E(0x0E, "(Reserved)", ""),
    SWIPE_NO_PASS_INVALID_TIMEZONE(0x0F, "SwipeNOPass", "Denied Access: Invalid Timezone"),
    RESERVED_10(0x10, "(Reserved)", ""),
    RESERVED_11(0x11, "(Reserved)", ""),
    SWIPE_NO_PASS_DENIED_ACCESS(0x12, "SwipeNOPass", "Denied Access"),
    RESERVED_13(0x13, "(Reserved)", ""),
    VALID_EVENT_PUSH_BUTTON(0x14, "ValidEvent", "Push Button"),
    RESERVED_15(0x15, "(Reserved)", ""),
    RESERVED_16(0x16, "(Reserved)", ""),
    VALID_EVENT_DOOR_OPEN(0x17, "ValidEvent", "Door Open"),
    VALID_EVENT_DOOR_CLOSED(0x18, "ValidEvent", "Door Closed"),
    VALID_EVENT_SUPER_PASSWORD_OPEN_DOOR(0x19, "ValidEvent", "Super Password Open Door"),
    RESERVED_1A(0x1A, "(Reserved)", ""),
    RESERVED_1B(0x1B, "(Reserved)", ""),
    WARN_CONTROLLER_POWER_ON(0x1C, "Warn", "Controller Power On"),
    WARN_CONTROLLER_RESET(0x1D, "Warn", "Controller Reset"),
    RESERVED_1E(0x1E, "(Reserved)", ""),
    WARN_PUSH_BUTTON_INVALID_FORCED_LOCK(0x1F, "Warn", "Push Button Invalid: Forced Lock"),
    WARN_PUSH_BUTTON_INVALID_NOT_ONLINE(0x20, "Warn", "Push Button Invalid: Not On Line"),
    WARN_PUSH_BUTTON_INVALID_INTERLOCK(0x21, "Warn", "Push Button Invalid: InterLock"),
    WARN_THREAT(0x22, "Warn", "Threat"),
    RESERVED_23(0x23, "(Reserved)", ""),
    RESERVED_24(0x24, "(Reserved)", ""),
    WARN_OPEN_TOO_LONG(0x25, "Warn", "Open too long"),
    WARN_FORCED_OPEN(0x26, "Warn", "Forced Open"),
    WARN_FIRE(0x27, "Warn", "Fire"),
    WARN_FORCED_CLOSE(0x28, "Warn", "Forced Close"),
    WARN_GUARD_AGAINST_THEFT(0x29, "Warn", "Guard Against Theft"),
    WARN_7_24HOUR_ZONE(0x2A, "Warn", "7*24Hour Zone"),
    WARN_EMERGENCY_CALL(0x2B, "Warn", "Emergency Call"),
    REMOTE_OPEN_DOOR(0x2C, "RemoteOpen", "Remote Open Door"),
    REMOTE_OPEN_DOOR_BY_USB_READER(0x2D, "RemoteOpen", "Remote Open Door By USB Reader"),
    UNKNOWN(0x00, "Unknown", "Unknown");

    private final int code;
    private final String eventCategory;
    private final String description;

    SwipeReason(int code, String eventCategory, String description) {
        this.code = code;
        this.eventCategory = eventCategory;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getDescription() {
        return description;
    }

    public static SwipeReason fromCode(int code) {
        for (SwipeReason reason : SwipeReason.values()) {
            if (reason.code == code) {
                return reason;
            }
        }
        return UNKNOWN;  // Default to UNKNOWN if no matching code is found
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", description, eventCategory);
    }
}
