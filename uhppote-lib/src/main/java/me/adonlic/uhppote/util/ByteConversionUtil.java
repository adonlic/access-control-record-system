package me.adonlic.uhppote.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility class for general byte conversion tasks, including conversions to and from
 * integers, floats, and byte arrays in both little-endian and big-endian formats.
 */
public class ByteConversionUtil {

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal representation of the byte array.
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "<null>";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString().trim();
    }

    /**
     * Converts a little-endian byte array to an integer.
     *
     * @param bytes The byte array in little-endian order.
     * @return The resulting integer.
     */
    public static int bytesToIntLE(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Invalid byte array size for an integer.");
        }
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * Converts an integer to a little-endian byte array.
     *
     * @param value The integer value to convert.
     * @return A byte array representing the integer in little-endian order.
     */
    public static byte[] intToBytesLE(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }

    /**
     * Converts a 2-byte big-endian byte array to an integer.
     *
     * @param bytes The byte array in big-endian order.
     * @return The resulting integer.
     */
    public static int bytesToIntBE2(byte[] bytes) {
        if (bytes == null || bytes.length != 2) {
            throw new IllegalArgumentException("Invalid byte array size for a 2-byte integer.");
        }
        return ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
    }

    /**
     * Converts an integer to a 2-byte big-endian byte array.
     *
     * @param value The integer value to convert.
     * @return A 2-byte array representing the integer in big-endian order.
     */
    public static byte[] intToBytesBE2(int value) {
        return new byte[] {
                (byte) ((value >> 8) & 0xFF),
                (byte) (value & 0xFF)
        };
    }

    /**
     * Converts a little-endian byte array to a float.
     *
     * @param bytes The byte array to convert.
     * @return The resulting float.
     */
    public static float bytesToFloatLE(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Invalid byte array size for a float.");
        }
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    /**
     * Converts a float to a little-endian byte array.
     *
     * @param value The float value to convert.
     * @return The byte array representing the float in little-endian order.
     */
    public static byte[] floatToBytesLE(float value) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putFloat(value);
        return buffer.array();
    }
}
