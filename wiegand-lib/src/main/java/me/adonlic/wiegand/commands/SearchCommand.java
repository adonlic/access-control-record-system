package me.adonlic.wiegand.commands;

import me.adonlic.communication.UDPClient;
import me.adonlic.wiegand.WiegandCommand;
import me.adonlic.wiegand.WiegandPacket;
import me.adonlic.wiegand.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * Represents the search command to communicate with a Wiegand controller.
 */
public class SearchCommand implements WiegandCommand {

    private static final Logger logger = LogManager.getLogger(SearchCommand.class);
    private static final byte FUNCTION_ID = (byte) 0x94;

    /**
     * Constructs the Wiegand packet for the search command.
     *
     * @return The WiegandPacket representing the search command.
     */
    @Override
    public WiegandPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;
        data[1] = FUNCTION_ID;
        // Fill the rest of the packet with zeros
        Arrays.fill(data, 2, 64, (byte) 0x00);

        logger.debug("SearchCommand packet created: {}", DataConversionUtil.bytesToHex(data));
        return new WiegandPacket(data);
    }

    /**
     * Parses the response packet to extract information.
     *
     * @param packet The response packet.
     * @return A SearchResponse object with the parsed data.
     */
    @Override
    public SearchResponse fromPacket(WiegandPacket packet) {
        byte[] response = packet.getRawData();
        logger.info("Received response packet: {}", DataConversionUtil.bytesToHex(response));

        int controllerSN = DataConversionUtil.bytesToIntLE(Arrays.copyOfRange(response, 4, 8));
        String ipAddress = DataConversionUtil.bytesToIpAddress(Arrays.copyOfRange(response, 8, 12));
        String subnetMask = DataConversionUtil.bytesToIpAddress(Arrays.copyOfRange(response, 12, 16));
        String gateway = DataConversionUtil.bytesToIpAddress(Arrays.copyOfRange(response, 16, 20));
        String macAddress = DataConversionUtil.bytesToMacAddress(Arrays.copyOfRange(response, 20, 26));
        int driverVersion = DataConversionUtil.bcdToInt(Arrays.copyOfRange(response, 26, 28));
        int releaseDate = DataConversionUtil.bcdToInt(Arrays.copyOfRange(response, 28, 32));

        SearchResponse responseObj = new SearchResponse(controllerSN, ipAddress, subnetMask, gateway, macAddress, driverVersion, releaseDate);
        logger.debug("Parsed SearchResponse: {}", responseObj);

        return responseObj;
    }

    /**
     * Executes the search command using the provided UDP client.
     *
     * @param client The UDP client to use for communication.
     * @return The parsed response from the command.
     * @throws IOException If an I/O error occurs during communication.
     */
    @Override
    public SearchResponse execute(UDPClient client) throws IOException {
        logger.info("Executing SearchCommand with UDP client.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new WiegandPacket(response));
        } else {
            logger.warn("No response received from SearchCommand.");
            return null;
        }
    }

    /**
     * Represents the response data from a Wiegand search command.
     */
    public static class SearchResponse {
        private final int controllerSN;
        private final String ipAddress;
        private final String subnetMask;
        private final String gateway;
        private final String macAddress;
        private final int driverVersion;
        private final int releaseDate;

        public SearchResponse(int controllerSN, String ipAddress, String subnetMask, String gateway, String macAddress, int driverVersion, int releaseDate) {
            this.controllerSN = controllerSN;
            this.ipAddress = ipAddress;
            this.subnetMask = subnetMask;
            this.gateway = gateway;
            this.macAddress = macAddress;
            this.driverVersion = driverVersion;
            this.releaseDate = releaseDate;
        }

        public int getControllerSN() {
            return controllerSN;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getSubnetMask() {
            return subnetMask;
        }

        public String getGateway() {
            return gateway;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public int getDriverVersion() {
            return driverVersion;
        }

        public int getReleaseDate() {
            return releaseDate;
        }

        @Override
        public String toString() {
            return "SearchResponse{" +
                    "controllerSN=" + controllerSN +
                    ", ipAddress='" + ipAddress + '\'' +
                    ", subnetMask='" + subnetMask + '\'' +
                    ", gateway='" + gateway + '\'' +
                    ", macAddress='" + macAddress + '\'' +
                    ", driverVersion=" + driverVersion +
                    ", releaseDate=" + releaseDate +
                    '}';
        }
    }
}
