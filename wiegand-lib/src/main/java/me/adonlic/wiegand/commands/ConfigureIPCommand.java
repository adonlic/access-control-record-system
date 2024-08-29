package me.adonlic.wiegand.commands;

import me.adonlic.communication.UDPClient;
import me.adonlic.wiegand.WiegandCommand;
import me.adonlic.wiegand.WiegandPacket;
import me.adonlic.wiegand.util.DataConversionUtil;  // Import DataConversionUtil for byte conversion
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Represents the command to configure the IP of a Wiegand controller.
 */
public class ConfigureIPCommand implements WiegandCommand {

    private static final Logger logger = LogManager.getLogger(ConfigureIPCommand.class);
    private static final byte FUNCTION_ID = (byte) 0x96;

    private final int controllerSN;
    private final InetAddress controllerIP;
    private final InetAddress controllerMask;
    private final InetAddress controllerGateway;

    /**
     * Constructs a new ConfigureIPCommand.
     *
     * @param controllerSN    The serial number of the controller.
     * @param controllerIP    The new IP address for the controller.
     * @param controllerMask  The new subnet mask for the controller.
     * @param controllerGateway The new gateway address for the controller.
     */
    public ConfigureIPCommand(int controllerSN, InetAddress controllerIP, InetAddress controllerMask, InetAddress controllerGateway) {
        this.controllerSN = controllerSN;
        this.controllerIP = controllerIP;
        this.controllerMask = controllerMask;
        this.controllerGateway = controllerGateway;
    }

    /**
     * Constructs the Wiegand packet for the configure IP command.
     *
     * @return The WiegandPacket representing the configure IP command.
     */
    @Override
    public WiegandPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Command start
        data[1] = FUNCTION_ID;  // Function ID for configuring IP

        // Set controller serial number (bytes 4-7, low to high byte for little-endian)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set IP address (bytes 8-11)
        byte[] ipBytes = controllerIP.getAddress();
        System.arraycopy(ipBytes, 0, data, 8, 4);

        // Set subnet mask (bytes 12-15)
        byte[] maskBytes = controllerMask.getAddress();
        System.arraycopy(maskBytes, 0, data, 12, 4);

        // Set gateway (bytes 16-19)
        byte[] gatewayBytes = controllerGateway.getAddress();
        System.arraycopy(gatewayBytes, 0, data, 16, 4);

        // Set fixed flags
        data[20] = (byte) 0x55;
        data[21] = (byte) 0xaa;
        data[22] = (byte) 0xaa;
        data[23] = (byte) 0x55;

        // Rest of the packet is already initialized to 0 by default

        logger.debug("ConfigureIPCommand packet created: {}", DataConversionUtil.bytesToHex(data));
        return new WiegandPacket(data);
    }

    /**
     * This method is not used as this command does not expect a response.
     *
     * @param packet The response packet.
     * @return Always returns null as no response is expected.
     */
    @Override
    public Object fromPacket(WiegandPacket packet) {
        // No response handling is needed for this command
        return null;
    }

    /**
     * Executes the configure IP command using the provided UDP client.
     *
     * @param client The UDP client to use for communication.
     * @return Always returns null as no response is expected.
     * @throws IOException If an I/O error occurs during communication.
     */
    @Override
    public Object execute(UDPClient client) throws IOException {
        logger.info("Executing ConfigureIPCommand with UDP client.");
        byte[] data = toPacket().getRawData();
        client.send(data); // Send the packet without expecting a response
        logger.info("ConfigureIPCommand sent successfully.");
        return null;
    }
}
