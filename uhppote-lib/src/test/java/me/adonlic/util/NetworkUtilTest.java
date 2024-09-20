package me.adonlic.util;

import me.adonlic.uhppote.util.NetworkUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NetworkUtilTest {

    @Test
    public void testBytesToIpAddress() {
        byte[] bytes = {(byte) 192, (byte) 168, 0, 1};
        String result = NetworkUtil.bytesToIpAddress(bytes);
        assertEquals("192.168.0.1", result);
    }

    @Test
    public void testIpAddressToBytes() {
        String ipAddress = "192.168.0.1";
        byte[] result = NetworkUtil.ipAddressToBytes(ipAddress);
        assertArrayEquals(new byte[] {(byte) 192, (byte) 168, 0, 1}, result);
    }

    @Test
    public void testBytesToMacAddress() {
        byte[] bytes = {0x00, 0x1A, 0x2B, 0x3C, 0x4D, 0x5E};
        String result = NetworkUtil.bytesToMacAddress(bytes);
        assertEquals("00:1A:2B:3C:4D:5E", result);
    }

    @Test
    public void testInvalidBytesToIpAddress() {
        byte[] bytes = {0x01, 0x02}; // Invalid length for IP address
        assertThrows(IllegalArgumentException.class, () -> {
            NetworkUtil.bytesToIpAddress(bytes);
        });
    }

    @Test
    public void testInvalidIpAddressToBytes() {
        String ipAddress = "300.168.0.1"; // Invalid IP address part
        assertThrows(IllegalArgumentException.class, () -> {
            NetworkUtil.ipAddressToBytes(ipAddress);
        });
    }

    @Test
    public void testInvalidIpAddressNumberFormat() {
        String ipAddress = "abc.168.0.1"; // Invalid number format
        assertThrows(NumberFormatException.class, () -> {
            NetworkUtil.ipAddressToBytes(ipAddress);
        });
    }

    @Test
    public void testInvalidBytesToMacAddress() {
        byte[] bytes = {0x01, 0x02, 0x03}; // Invalid length for MAC address
        assertThrows(IllegalArgumentException.class, () -> {
            NetworkUtil.bytesToMacAddress(bytes);
        });
    }
}
