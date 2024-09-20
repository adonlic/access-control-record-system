package me.adonlic.uhppote.util;

/**
 * Utility class for handling Binary-Coded Decimal (BCD) conversions, including
 * conversions between strings, integers, and BCD-encoded byte arrays.
 */
public class BCDUtil {

    /**
     * Converts a string of digits into a BCD-encoded byte array.
     *
     * @param str The string to convert (e.g., "20230101" for a date).
     * @return The BCD-encoded byte array.
     * @throws IllegalArgumentException if the string length is odd or contains non-digit characters.
     */
    public static byte[] stringToBCD(String str) {
        if (str == null || str.length() % 2 != 0) {
            throw new IllegalArgumentException("Input string must be non-null and even-length.");
        }

        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            int high = Character.digit(str.charAt(i), 10);
            int low = Character.digit(str.charAt(i + 1), 10);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("Invalid digit in input string.");
            }
            result[i / 2] = (byte) ((high << 4) | low);
        }

        return result;
    }

    /**
     * Converts a BCD-encoded byte array back to its string representation.
     *
     * @param bcd The BCD-encoded byte array.
     * @return The decoded string (e.g., "20230101").
     */
    public static String bcdToString(byte[] bcd) {
        if (bcd == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(bcd.length * 2);
        for (byte b : bcd) {
            sb.append((b >> 4) & 0x0F);  // High nibble
            sb.append(b & 0x0F);         // Low nibble
        }

        return sb.toString();
    }

    /**
     * Converts a BCD-encoded byte array to an integer.
     *
     * @param bcd The BCD-encoded byte array.
     * @return The integer representation of the BCD.
     */
    public static int bcdToInt(byte[] bcd) {
        int result = 0;
        for (byte b : bcd) {
            result = result * 100 + ((b >> 4) & 0xF) * 10 + (b & 0xF);
        }
        return result;
    }

    /**
     * Converts a single BCD-encoded byte to its integer representation.
     *
     * @param bcd The BCD-encoded byte.
     * @return The integer value.
     */
    public static int bcdToInt(byte bcd) {
        return ((bcd >> 4) * 10) + (bcd & 0x0F);
    }
}
