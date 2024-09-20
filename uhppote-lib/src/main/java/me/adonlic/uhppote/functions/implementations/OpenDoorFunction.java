package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Represents the function to remotely open a door on a controller.
 */
public class OpenDoorFunction implements ProtocolFunction<OpenDoorFunction.OpenDoorResponse> {
    private static final Logger logger = LogManager.getLogger(OpenDoorFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x40;

    private final int controllerSN;
    private final int doorNumber;

    public OpenDoorFunction(int controllerSN, int doorNumber) {
        this.controllerSN = controllerSN;
        this.doorNumber = doorNumber;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for opening door

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set door number (byte 8)
        data[8] = (byte) doorNumber;

        logger.debug("OpenDoorFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public OpenDoorResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        int controllerSN = DataConversionUtil.bytesToIntLE(new byte[]{data[4], data[5], data[6], data[7]});
        int success = data[8];  // Success flag (1 = success, 0 = failure)

        OpenDoorResponse responseObj = new OpenDoorResponse(controllerSN, success == 1);
        logger.debug("Parsed OpenDoorResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public OpenDoorResponse execute(UDPClient client) throws IOException {
        logger.info("Executing OpenDoorFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for OpenDoorFunction.");
            return null;
        }
    }

    /**
     * Represents the response to a OpenDoorFunction.
     */
    public static class OpenDoorResponse {
        private final int controllerSN;
        private final boolean success;

        public OpenDoorResponse(int controllerSN, boolean success) {
            this.controllerSN = controllerSN;
            this.success = success;
        }

        public int getControllerSN() {
            return controllerSN;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "OpenDoorResponse{" +
                    "controllerSN=" + controllerSN +
                    ", success=" + success +
                    '}';
        }
    }
}
