package me.adonlic.wiegand;

import me.adonlic.communication.UDPClient;

import java.io.IOException;

/**
 * Interface for Wiegand protocol commands.
 */
public interface WiegandCommand {
    /**
     * Converts the command to a Wiegand packet.
     *
     * @return The WiegandPacket representing the command.
     */
    WiegandPacket toPacket();

    /**
     * Parses a response packet to extract information.
     *
     * @param packet The response packet.
     * @return The parsed response object.
     */
    Object fromPacket(WiegandPacket packet);

    /**
     * Executes the command using the provided UDP client.
     *
     * @param client The UDP client to use for communication.
     * @return The parsed response from the command.
     * @throws IOException If an I/O error occurs during communication.
     */
    Object execute(UDPClient client) throws IOException;
}
