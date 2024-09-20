package me.adonlic.util;

import static org.junit.jupiter.api.Assertions.*;

import me.adonlic.uhppote.util.DateUtil;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

    @Test
    public void testParseSwipeTime() {
        byte[] bcdTime = {0x20, 0x23, 0x09, 0x16, 0x12, 0x34, 0x56}; // BCD for 2023-09-16 12:34:56
        String result = DateUtil.parseSwipeTime(bcdTime);
        assertEquals("2023-09-16 12:34:56", result);
    }

    @Test
    public void testParseDate() {
        byte[] bcdDate = {0x20, 0x23, 0x09, 0x16}; // BCD for 2023-09-16
        String result = DateUtil.parseDate(bcdDate);
        assertEquals("2023-09-16", result);
    }

    @Test
    public void testTimestampToBCD() {
        String timestamp = "20230916123456";
        byte[] result = DateUtil.timestampToBCD(timestamp);
        assertArrayEquals(new byte[] {0x20, 0x23, 0x09, 0x16, 0x12, 0x34, 0x56}, result);
    }

    @Test
    public void testInvalidParseSwipeTime() {
        byte[] bcdTime = {0x20, 0x23, 0x09}; // Invalid length
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.parseSwipeTime(bcdTime);
        });
    }

    @Test
    public void testInvalidParseDate() {
        byte[] bcdDate = {0x20}; // Invalid length
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.parseDate(bcdDate);
        });
    }

    @Test
    public void testInvalidTimestampToBCD() {
        String timestamp = "20230916"; // Invalid length
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtil.timestampToBCD(timestamp);
        });
    }
}
