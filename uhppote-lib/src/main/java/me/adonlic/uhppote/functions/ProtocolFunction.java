package me.adonlic.uhppote.functions;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;

import java.io.IOException;

/**
 * Interface for protocol functions.
 */
public interface ProtocolFunction<T> {
    /**
     * Converts the function to a packet.
     *
     * @return The ProtocolPacket representing the function.
     */
    ProtocolPacket toPacket();

    /**
     * Parses a response packet to extract information.
     *
     * @param packet The response packet.
     * @return The parsed response object.
     */
    T fromPacket(ProtocolPacket packet);

    /**
     * Executes the function using the provided UDP client.
     *
     * @param client The UDP client to use for communication.
     * @return The parsed response from the function.
     * @throws IOException If an I/O error occurs during communication.
     */
    T execute(UDPClient client) throws IOException;
}
