package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to retrieve DataServer information from the controller.
 */
public class GetDataServerInfoFunction implements ProtocolFunction<GetDataServerInfoFunction.GetDataServerInfoResponse> {

    private static final Logger logger = LogManager.getLogger(GetDataServerInfoFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x92;

    private final int controllerSN;

    /**
     * Constructs a new GetDataServerInfoFunction.
     *
     * @param controllerSN The serial number of the controller.
     */
    public GetDataServerInfoFunction(int controllerSN) {
        this.controllerSN = controllerSN;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for getting DataServer information

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        logger.debug("GetDataServerInfoFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public GetDataServerInfoResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        int controllerSN = DataConversionUtil.bytesToIntLE(new byte[]{data[4], data[5], data[6], data[7]});
        String dataServerIP = DataConversionUtil.bytesToIpAddress(new byte[]{data[8], data[9], data[10], data[11]});
        // int dataServerPort = DataConversionUtil.bytesToIntLE(new byte[]{data[12], data[13]});
        int dataServerPort = ((data[12] & 0xFF) << 8) | (data[13] & 0xFF); // Is this right?
        int autoSendCycle = data[14];  // Auto send cycle (0 means send only on new events)

        GetDataServerInfoResponse responseObj = new GetDataServerInfoResponse(controllerSN, dataServerIP, dataServerPort, autoSendCycle);
        logger.debug("Parsed GetDataServerInfoResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public GetDataServerInfoResponse execute(UDPClient client) throws IOException {
        logger.info("Executing GetDataServerInfoFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for GetDataServerInfoFunction.");
            return null;
        }
    }

    /**
     * Represents the response to a GetDataServerInfoFunction.
     */
    public static class GetDataServerInfoResponse {
        private final int controllerSN;
        private final String dataServerIP;
        private final int dataServerPort;
        private final int autoSendCycle;

        public GetDataServerInfoResponse(int controllerSN, String dataServerIP, int dataServerPort, int autoSendCycle) {
            this.controllerSN = controllerSN;
            this.dataServerIP = dataServerIP;
            this.dataServerPort = dataServerPort;
            this.autoSendCycle = autoSendCycle;
        }

        public int getControllerSN() {
            return controllerSN;
        }

        public String getDataServerIP() {
            return dataServerIP;
        }

        public int getDataServerPort() {
            return dataServerPort;
        }

        public int getAutoSendCycle() {
            return autoSendCycle;
        }

        @Override
        public String toString() {
            return "GetDataServerInfoResponse{" +
                    "controllerSN=" + controllerSN +
                    ", dataServerIP='" + dataServerIP + '\'' +
                    ", dataServerPort=" + dataServerPort +
                    ", autoSendCycle=" + autoSendCycle +
                    '}';
        }
    }
}
