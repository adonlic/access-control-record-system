package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;  // Import DataConversionUtil for byte conversion
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Represents the function to configure the IP of a controller.
 */
public class ConfigureIPFunction implements ProtocolFunction {

    private static final Logger logger = LogManager.getLogger(ConfigureIPFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x96;

    private final int controllerSN;
    private final InetAddress controllerIP;
    private final InetAddress controllerMask;
    private final InetAddress controllerGateway;

    /**
     * Constructs a new ConfigureIPFunction.
     *
     * @param controllerSN    The serial number of the controller.
     * @param controllerIP    The new IP address for the controller.
     * @param controllerMask  The new subnet mask for the controller.
     * @param controllerGateway The new gateway address for the controller.
     */
    public ConfigureIPFunction(int controllerSN, InetAddress controllerIP, InetAddress controllerMask, InetAddress controllerGateway) {
        this.controllerSN = controllerSN;
        this.controllerIP = controllerIP;
        this.controllerMask = controllerMask;
        this.controllerGateway = controllerGateway;
    }

    /**
     * Constructs the packet for the configure IP function.
     *
     * @return The ProtocolPacket representing the configure IP function.
     */
    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
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

        logger.debug("ConfigureIPFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    /**
     * This method is not used as this function does not expect a response.
     *
     * @param packet The response packet.
     * @return Always returns null as no response is expected.
     */
    @Override
    public Object fromPacket(ProtocolPacket packet) {
        // No response handling is needed for this function
        return null;
    }

    /**
     * Executes the configure IP function using the provided UDP client.
     *
     * @param client The UDP client to use for communication.
     * @return Always returns null as no response is expected.
     * @throws IOException If an I/O error occurs during communication.
     */
    @Override
    public Object execute(UDPClient client) throws IOException {
        logger.info("Executing ConfigureIPFunction with UDP client.");
        byte[] data = toPacket().getRawData();
        client.send(data); // Send the packet without expecting a response
        logger.info("ConfigureIPFunction sent successfully.");
        return null;
    }
}
