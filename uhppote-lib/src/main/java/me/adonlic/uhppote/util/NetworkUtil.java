package me.adonlic.uhppote.util;

/**
 * Utility class for handling network-related conversions, such as IP address
 * and MAC address conversions between byte arrays and string formats.
 */
public class NetworkUtil {

    /**
     * Converts a byte array to an IP address string (e.g., "192.168.1.1").
     *
     * @param bytes The byte array representing an IP address (must be 4 bytes long).
     * @return The IP address as a string.
     * @throws IllegalArgumentException if the byte array is not 4 bytes long.
     */
    public static String bytesToIpAddress(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Invalid byte array for an IP address.");
        }
        return String.format("%d.%d.%d.%d", bytes[0] & 0xFF, bytes[1] & 0xFF, bytes[2] & 0xFF, bytes[3] & 0xFF);
    }

    /**
     * Converts an IP address string to a byte array.
     *
     * @param ipAddress The IP address string (e.g., "192.168.1.1").
     * @return The byte array representing the IP address.
     * @throws IllegalArgumentException if the IP address is invalid.
     * @throws NumberFormatException if the IP address contains invalid numbers.
     */
    public static byte[] ipAddressToBytes(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format.");
        }
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            try {
                int part = Integer.parseInt(parts[i]);
                if (part < 0 || part > 255) {
                    throw new IllegalArgumentException("IP address part out of range.");
                }
                bytes[i] = (byte) part;
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid number format in IP address.");
            }
        }
        return bytes;
    }

    /**
     * Converts a byte array to a MAC address string (e.g., "00:1A:2B:3C:4D:5E").
     *
     * @param bytes The byte array representing a MAC address (must be 6 bytes long).
     * @return The MAC address as a string.
     * @throws IllegalArgumentException if the byte array is not 6 bytes long.
     */
    public static String bytesToMacAddress(byte[] bytes) {
        if (bytes == null || bytes.length != 6) {
            throw new IllegalArgumentException("Invalid byte array for a MAC address.");
        }
        StringBuilder mac = new StringBuilder(18);
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                mac.append(":");
            }
            mac.append(String.format("%02X", bytes[i]));
        }
        return mac.toString();
    }
}
