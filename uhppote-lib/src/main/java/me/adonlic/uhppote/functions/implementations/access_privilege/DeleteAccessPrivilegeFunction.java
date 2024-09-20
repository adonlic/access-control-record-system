package me.adonlic.uhppote.functions.implementations.access_privilege;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to delete an access privilege by card number.
 */
public class DeleteAccessPrivilegeFunction implements ProtocolFunction<DeleteAccessPrivilegeFunction.DeleteAccessPrivilegeResponse> {

    private static final Logger logger = LogManager.getLogger(DeleteAccessPrivilegeFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x52;

    private final int controllerSN;
    private final int cardNumber;

    /**
     * Constructs a new DeleteAccessPrivilegeFunction.
     *
     * @param controllerSN The serial number of the controller.
     * @param cardNumber   The card number to delete access for.
     */
    public DeleteAccessPrivilegeFunction(int controllerSN, int cardNumber) {
        this.controllerSN = controllerSN;
        this.cardNumber = cardNumber;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start byte
        data[1] = FUNCTION_ID;  // Function ID for deleting access privilege

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set card number to be deleted (bytes 8-11)
        byte[] cardBytes = DataConversionUtil.intToBytesLE(cardNumber);
        System.arraycopy(cardBytes, 0, data, 8, 4);

        logger.debug("DeleteAccessPrivilegeFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public DeleteAccessPrivilegeResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Check success flag (Byte 8 contains 1 for success, 0 for failure)
        boolean success = data[8] == 1;
        return new DeleteAccessPrivilegeResponse(success);
    }

    @Override
    public DeleteAccessPrivilegeResponse execute(UDPClient client) throws IOException {
        logger.info("Executing DeleteAccessPrivilegeFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);

        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for DeleteAccessPrivilegeFunction.");
            return new DeleteAccessPrivilegeResponse(false); // Indicate failure if no response
        }
    }

    /**
     * Represents the response to the DeleteAccessPrivilegeFunction.
     */
    public static class DeleteAccessPrivilegeResponse {
        private final boolean success;

        public DeleteAccessPrivilegeResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "DeleteAccessPrivilegeResponse{" +
                    "success=" + success +
                    '}';
        }
    }
}
