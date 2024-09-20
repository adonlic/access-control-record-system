package me.adonlic.uhppote;

/**
 * Represents a protocol packet with raw byte data.
 */
public class ProtocolPacket {
    private final byte[] data;

    /**
     * Constructs a ProtocolPacket from the provided raw byte array.
     *
     * @param data The raw byte array representing the packet.
     */
    public ProtocolPacket(byte[] data) {
        if (data == null || data.length != 64) { // Ensure packet size is 64 bytes
            throw new IllegalArgumentException("Packet data must be exactly 64 bytes.");
        }
        this.data = data;
    }

    /**
     * Returns the raw byte data of the packet.
     *
     * @return The byte array representing the packet.
     */
    public byte[] getRawData() {
        return data;
    }
}
