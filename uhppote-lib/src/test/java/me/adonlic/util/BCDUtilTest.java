package me.adonlic.util;

import static org.junit.jupiter.api.Assertions.*;

import me.adonlic.uhppote.util.BCDUtil;
import org.junit.jupiter.api.Test;

public class BCDUtilTest {

    @Test
    public void testStringToBCD() {
        String str = "12345678";
        byte[] result = BCDUtil.stringToBCD(str);
        assertArrayEquals(new byte[] {0x12, 0x34, 0x56, 0x78}, result);
    }

    @Test
    public void testBCDToString() {
        byte[] bcd = {0x12, 0x34, 0x56, 0x78};
        String result = BCDUtil.bcdToString(bcd);
        assertEquals("12345678", result);
    }

    @Test
    public void testBCDToInt() {
        byte[] bcd = {0x12, 0x34};
        int result = BCDUtil.bcdToInt(bcd);
        assertEquals(1234, result);
    }

    @Test
    public void testBCDByteToInt() {
        byte bcd = 0x12;
        int result = BCDUtil.bcdToInt(bcd);
        assertEquals(12, result);
    }

    @Test
    public void testInvalidStringToBCD() {
        String str = "123"; // Odd length
        assertThrows(IllegalArgumentException.class, () -> {
            BCDUtil.stringToBCD(str);
        });
    }

    @Test
    public void testInvalidCharactersInStringToBCD() {
        String str = "1234a678"; // Contains non-digit characters
        assertThrows(IllegalArgumentException.class, () -> {
            BCDUtil.stringToBCD(str);
        });
    }
}
