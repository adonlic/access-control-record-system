package me.adonlic.wiegand;

/**
 * Represents a Wiegand protocol packet with raw byte data.
 */
public class WiegandPacket {
    private final byte[] data;

    /**
     * Constructs a WiegandPacket from the provided raw byte array.
     *
     * @param data The raw byte array representing the packet.
     */
    public WiegandPacket(byte[] data) {
        if (data == null || data.length != 64) { // Ensure packet size is 64 bytes
            throw new IllegalArgumentException("Packet data must be exactly 64 bytes.");
        }
        this.data = data;
    }

    /**
     * Returns the raw byte data of the Wiegand packet.
     *
     * @return The byte array representing the packet.
     */
    public byte[] getRawData() {
        return data;
    }
}
