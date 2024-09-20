package me.adonlic.util;

import static org.junit.jupiter.api.Assertions.*;

import me.adonlic.uhppote.util.ByteConversionUtil;
import org.junit.jupiter.api.Test;

public class ByteConversionUtilTest {

    @Test
    public void testBytesToHex() {
        byte[] bytes = {0x1A, 0x2B, 0x3C};
        String hex = ByteConversionUtil.bytesToHex(bytes);
        assertEquals("1a 2b 3c", hex.trim());
    }

    @Test
    public void testBytesToIntLE() {
        byte[] bytes = {0x01, 0x00, 0x00, 0x00}; // Little-endian representation of 1
        int result = ByteConversionUtil.bytesToIntLE(bytes);
        assertEquals(1, result);
    }

    @Test
    public void testIntToBytesLE() {
        int value = 1;
        byte[] result = ByteConversionUtil.intToBytesLE(value);
        assertArrayEquals(new byte[] {0x01, 0x00, 0x00, 0x00}, result);
    }

    @Test
    public void testBytesToIntBE2() {
        byte[] bytes = {0x01, 0x02}; // Big-endian representation of 258
        int result = ByteConversionUtil.bytesToIntBE2(bytes);
        assertEquals(258, result);
    }

    @Test
    public void testIntToBytesBE2() {
        int value = 258;
        byte[] result = ByteConversionUtil.intToBytesBE2(value);
        assertArrayEquals(new byte[] {0x01, 0x02}, result);
    }

    @Test
    public void testBytesToFloatLE() {
        byte[] bytes = {0x00, 0x00, (byte) 0x80, 0x3F}; // Little-endian for 1.0f
        float result = ByteConversionUtil.bytesToFloatLE(bytes);
        assertEquals(1.0f, result);
    }

    @Test
    public void testFloatToBytesLE() {
        float value = 1.0f;
        byte[] result = ByteConversionUtil.floatToBytesLE(value);
        assertArrayEquals(new byte[] {0x00, 0x00, (byte) 0x80, 0x3F}, result);
    }

    @Test
    public void testInvalidBytesToIntLE() {
        byte[] bytes = {0x01, 0x00}; // Invalid length
        assertThrows(IllegalArgumentException.class, () -> {
            ByteConversionUtil.bytesToIntLE(bytes);
        });
    }
}
