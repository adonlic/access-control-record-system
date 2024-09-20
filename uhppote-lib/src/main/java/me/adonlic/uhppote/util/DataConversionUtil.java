package me.adonlic.uhppote.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for data conversion.
 */
public class DataConversionUtil {

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal representation of the byte array.
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return "<object is null>";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x ", b));
        }
        return String.format("%s (%d)", sb, bytes.length);
    }

    /**
     * Converts a byte array to an integer, interpreting the bytes in little-endian order.
     *
     * @param bytes The byte array to convert.
     * @return The resulting integer.
     */
    public static int bytesToIntLE(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }

    /**
     * Converts an integer to a byte array in little-endian order.
     *
     * @param value The integer value to convert.
     * @return A byte array representing the integer in little-endian order.
     */
    public static byte[] intToBytesLE(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }

    /**
     * Converts a byte array to an IP address string.
     *
     * @param bytes The byte array to convert.
     * @return The IP address string.
     */
    public static String bytesToIpAddress(byte[] bytes) {
        return (bytes[0] & 0xFF) + "." + (bytes[1] & 0xFF) + "." + (bytes[2] & 0xFF) + "." + (bytes[3] & 0xFF);
    }

    /**
     * Converts a byte array to a MAC address string.
     *
     * @param bytes The byte array to convert.
     * @return The MAC address string.
     */
    public static String bytesToMacAddress(byte[] bytes) {
        StringBuilder mac = new StringBuilder(18);
        for (byte b : bytes) {
            if (mac.length() > 0) mac.append(":");
            mac.append(String.format("%02x", b));
        }
        return mac.toString().toUpperCase();
    }

    /**
     * Converts a Binary-Coded Decimal (BCD) byte array to an integer.
     *
     * @param bcd The BCD byte array to convert.
     * @return The resulting integer.
     */
    public static int bcdToInt(byte[] bcd) {
        int result = 0;
        for (byte b : bcd) {
            result = result * 100 + ((b >> 4) & 0xF) * 10 + (b & 0xF);
        }
        return result;
    }

    /**
     * Converts a BCD byte to its integer representation.
     *
     * @param bcd The BCD-encoded byte.
     * @return The integer value.
     */
    public static int bcdToInt(byte bcd) {
        return ((bcd >> 4) * 10) + (bcd & 0x0F);
    }

    /**
     * Parses the swipe time from BCD-encoded bytes.
     *
     * @param bcdTime The BCD-encoded time bytes.
     * @return A formatted date string in "yyyy-MM-dd HH:mm:ss".
     */
    public static String parseSwipeTime(byte[] bcdTime) {
        try {
            String year = String.format("%02d%02d", bcdToInt(bcdTime[0]), bcdToInt(bcdTime[1]));
            String month = String.format("%02d", bcdToInt(bcdTime[2]));
            String day = String.format("%02d", bcdToInt(bcdTime[3]));
            String hour = String.format("%02d", bcdToInt(bcdTime[4]));
            String minute = String.format("%02d", bcdToInt(bcdTime[5]));
            String second = String.format("%02d", bcdToInt(bcdTime[6]));

            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            Date date = format.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
            return format.format(date);
        } catch (ParseException e) {
            System.err.println("Failed to parse swipe time: " + e.getMessage());
            return null;
        }
    }

    public static String parseDate(byte[] bcdTime) {
        try {
            String year = String.format("%02d%02d", bcdToInt(bcdTime[0]), bcdToInt(bcdTime[1]));
            String month = String.format("%02d", bcdToInt(bcdTime[2]));
            String day = String.format("%02d", bcdToInt(bcdTime[3]));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(year + "-" + month + "-" + day);
            return format.format(date);
        } catch (ParseException e) {
            System.err.println("Failed to parse date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a string of digits (e.g., a date) into a BCD-encoded byte array.
     *
     * @param str The string to convert (e.g., "20230101").
     * @return The resulting byte array in BCD format.
     */
    public static byte[] bcdToBytes(String str) {
        if (str == null || str.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must be a non-null, even-length string.");
        }

        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            int high = Character.digit(str.charAt(i), 10);
            int low = Character.digit(str.charAt(i + 1), 10);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("Invalid character in input string: " + str);
            }
            result[i / 2] = (byte) ((high << 4) | low);
        }

        return result;
    }
}
