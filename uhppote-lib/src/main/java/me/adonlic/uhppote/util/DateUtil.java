package me.adonlic.uhppote.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for handling date and time parsing and formatting, particularly
 * from BCD-encoded byte arrays.
 */
public class DateUtil {

    /**
     * Parses a BCD-encoded time byte array and returns a formatted date string.
     * The input is expected to be in BCD format representing year, month, day, hour, minute, second.
     *
     * @param bcdTime The BCD-encoded time byte array (7 bytes).
     * @return The formatted date string (yyyy-MM-dd HH:mm:ss).
     * @throws IllegalArgumentException if the byte array is not of length 7.
     */
    public static String parseSwipeTime(byte[] bcdTime) {
        if (bcdTime == null || bcdTime.length != 7) {
            throw new IllegalArgumentException("Invalid BCD-encoded time array.");
        }

        String year = BCDUtil.bcdToString(new byte[] {bcdTime[0], bcdTime[1]});
        String month = BCDUtil.bcdToString(new byte[] {bcdTime[2]});
        String day = BCDUtil.bcdToString(new byte[] {bcdTime[3]});
        String hour = BCDUtil.bcdToString(new byte[] {bcdTime[4]});
        String minute = BCDUtil.bcdToString(new byte[] {bcdTime[5]});
        String second = BCDUtil.bcdToString(new byte[] {bcdTime[6]});

        String formattedDate = String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, minute, second);
        return formattedDate;
    }

    /**
     * Parses a BCD-encoded date byte array and returns a formatted date string.
     * The input is expected to be in BCD format representing year, month, day.
     *
     * @param bcdDate The BCD-encoded date byte array (4 bytes).
     * @return The formatted date string (yyyy-MM-dd).
     * @throws IllegalArgumentException if the byte array is not of length 4.
     */
    public static String parseDate(byte[] bcdDate) {
        if (bcdDate == null || bcdDate.length != 4) {
            throw new IllegalArgumentException("Invalid BCD-encoded date array.");
        }

        String year = BCDUtil.bcdToString(new byte[] {bcdDate[0], bcdDate[1]});
        String month = BCDUtil.bcdToString(new byte[] {bcdDate[2]});
        String day = BCDUtil.bcdToString(new byte[] {bcdDate[3]});

        return String.format("%s-%s-%s", year, month, day);
    }

    /**
     * Converts a timestamp (yyyyMMddHHmmss) to a BCD-encoded byte array.
     *
     * @param timestamp The timestamp string.
     * @return The BCD-encoded byte array representing the timestamp.
     * @throws IllegalArgumentException if the timestamp format is invalid.
     */
    public static byte[] timestampToBCD(String timestamp) {
        if (timestamp == null || timestamp.length() != 14) {
            throw new IllegalArgumentException("Invalid timestamp format. Expected yyyyMMddHHmmss.");
        }
        return BCDUtil.stringToBCD(timestamp);
    }
}
