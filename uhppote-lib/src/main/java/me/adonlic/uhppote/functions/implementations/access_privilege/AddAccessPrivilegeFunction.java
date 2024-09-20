package me.adonlic.uhppote.functions.implementations.access_privilege;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Represents the function to add or edit user access rights on a controller.
 */
public class AddAccessPrivilegeFunction
        implements ProtocolFunction<AddAccessPrivilegeFunction.AddAccessPrivilegeResponse> {
    private static final Logger logger = LogManager.getLogger(AddAccessPrivilegeFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x50;

    private final int controllerSN;
    private final int cardNumber;
    private final String activateDate;  // Format: YYYYMMDD
    private final String deactivateDate;  // Format: YYYYMMDD
    private final boolean[] doorAccessRights;

    public AddAccessPrivilegeFunction(
            int controllerSN,
            int cardNumber,
            String activateDate,
            String deactivateDate,
            boolean[] doorAccessRights
    ) {
        this.controllerSN = controllerSN;
        this.cardNumber = cardNumber;
        this.activateDate = activateDate;
        this.deactivateDate = deactivateDate;
        this.doorAccessRights = doorAccessRights;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for updating access rights

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set card number (bytes 8-11)
        byte[] cardBytes = DataConversionUtil.intToBytesLE(cardNumber);
        System.arraycopy(cardBytes, 0, data, 8, 4);

//        // Set activate and deactivate dates
//        byte[] activateBytes = DataConversionUtil.bc(activateDate);
//        System.arraycopy(activateBytes, 0, data, 12, 4);
//
//        byte[] deactivateBytes = DataConversionUtil.intToBytesLE(deactivateDate);
//        System.arraycopy(deactivateBytes, 0, data, 16, 4);
//
//        // Set door access rights (bytes 20-23)
//        System.arraycopy(doorAccessRights, 0, data, 20, 4);

        // Set activate and deactivate dates (BCD encoded)
        byte[] activateBytes = DataConversionUtil.bcdToBytes(activateDate);
        byte[] deactivateBytes = DataConversionUtil.bcdToBytes(deactivateDate);
        System.arraycopy(activateBytes, 0, data, 12, 4);
        System.arraycopy(deactivateBytes, 0, data, 16, 4);

        // Door access rights (1 byte per door, 0x01 for allow, 0x00 for disallow)
        for (int i = 0; i < doorAccessRights.length; i++) {
            data[20 + i] = doorAccessRights[i] ? (byte) 0x01 : (byte) 0x00;
        }

        logger.debug("AddAccessPrivilegeFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public AddAccessPrivilegeResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Byte[8] should contain the success flag (1 for success, 0 for failure)
        boolean success = data[8] == 1;
        return new AddAccessPrivilegeResponse(success);
    }

    @Override
    public AddAccessPrivilegeResponse execute(UDPClient client) throws IOException {
        logger.info("Executing AddAccessPrivilegeFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for AddAccessPrivilegeFunction.");
            return new AddAccessPrivilegeResponse(false); // Indicate failure if no response
        }
    }

    /**
     * Represents the response to the AddAccessPrivilegeFunction.
     */
    public static class AddAccessPrivilegeResponse {
        private final boolean success;

        public AddAccessPrivilegeResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "AddAccessPrivilegeResponse{" +
                    "success=" + success +
                    '}';
        }
    }
}
