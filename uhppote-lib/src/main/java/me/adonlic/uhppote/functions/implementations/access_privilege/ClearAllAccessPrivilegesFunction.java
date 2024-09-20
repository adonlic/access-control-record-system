package me.adonlic.uhppote.functions.implementations.access_privilege;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to clear all access privileges on the controller.
 */
public class ClearAllAccessPrivilegesFunction implements ProtocolFunction<ClearAllAccessPrivilegesFunction.ClearAllAccessPrivilegesResponse> {

    private static final Logger logger = LogManager.getLogger(ClearAllAccessPrivilegesFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x54;

    private final int controllerSN;

    /**
     * Constructs a new ClearAllAccessPrivilegesFunction.
     *
     * @param controllerSN The serial number of the controller.
     */
    public ClearAllAccessPrivilegesFunction(int controllerSN) {
        this.controllerSN = controllerSN;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for clearing all access privileges

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        logger.debug("ClearAllAccessPrivilegesFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public ClearAllAccessPrivilegesResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Byte[8] should contain 1 for success, 0 for failure
        boolean success = data[8] == 1;
        return new ClearAllAccessPrivilegesResponse(success);
    }

    @Override
    public ClearAllAccessPrivilegesResponse execute(UDPClient client) throws IOException {
        logger.info("Executing ClearAllAccessPrivilegesFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);

        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for ClearAllAccessPrivilegesFunction.");
            return new ClearAllAccessPrivilegesResponse(false); // Indicate failure if no response
        }
    }

    /**
     * Represents the response to ClearAllAccessPrivilegesFunction.
     */
    public static class ClearAllAccessPrivilegesResponse {
        private final boolean success;

        public ClearAllAccessPrivilegesResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "ClearAllAccessPrivilegesResponse{" +
                    "success=" + success +
                    '}';
        }
    }
}
