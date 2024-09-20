package me.adonlic.uhppote.functions.implementations.access_privilege;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to query access privilege by card number on a controller.
 */
public class QueryAccessPrivilegeByCardFunction implements ProtocolFunction<QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse> {

    private static final Logger logger = LogManager.getLogger(QueryAccessPrivilegeByCardFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x5A;

    private final int controllerSN;
    private final int cardNumber;

    /**
     * Constructs a new QueryAccessPrivilegeByCardFunction.
     *
     * @param controllerSN The serial number of the controller.
     * @param cardNumber   The card number to query.
     */
    public QueryAccessPrivilegeByCardFunction(int controllerSN, int cardNumber) {
        this.controllerSN = controllerSN;
        this.cardNumber = cardNumber;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for querying access by card number

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set card number (bytes 8-11)
        byte[] cardBytes = DataConversionUtil.intToBytesLE(cardNumber);
        System.arraycopy(cardBytes, 0, data, 8, 4);

        logger.debug("QueryAccessPrivilegeByCardFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public QueryAccessPrivilegeResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // TODO check because it seems it returns invalid number
        int cardNumberResponse = DataConversionUtil.bytesToIntLE(new byte[]{data[8], data[9], data[10], data[11]});
        if (cardNumberResponse == 0) {
            logger.warn("No privileges found for card number.");
            return null;
        }

        // Parse activation and deactivation dates
        String activateDate = DataConversionUtil.parseDate(new byte[]{data[12], data[13], data[14], data[15]});
        String deactivateDate = DataConversionUtil.parseDate(new byte[]{data[16], data[17], data[18], data[19]});

        // Parse door access allowances
        boolean[] doorAccessRights = new boolean[4];
        doorAccessRights[0] = data[20] == 1;
        doorAccessRights[1] = data[21] == 1;
        doorAccessRights[2] = data[22] == 1;
        doorAccessRights[3] = data[23] == 1;

        // Parse user password if available
        // int userPassword = ByteBuffer.wrap(new byte[]{data[24], data[25], data[26]}).getInt(); // Right one?
        // int userPassword = DataConversionUtil.bcdToInt(new byte[]{data[24], data[25], data[26]});
        int userPassword = 0;

        QueryAccessPrivilegeResponse responseObj = new QueryAccessPrivilegeResponse(cardNumberResponse, activateDate, deactivateDate, doorAccessRights, userPassword);
        logger.debug("Parsed QueryAccessPrivilegeResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public QueryAccessPrivilegeResponse execute(UDPClient client) throws IOException {
        logger.info("Executing QueryAccessPrivilegeByCardFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for QueryAccessPrivilegeByCardFunction.");
            return null;
        }
    }

    /**
     * Represents the response to a QueryAccessPrivilegeByCardFunction.
     */
    public static class QueryAccessPrivilegeResponse {
        private final int cardNumber;
        private final String activateDate;
        private final String deactivateDate;
        private final boolean[] doorAccessRights;
        private final int userPassword;

        public QueryAccessPrivilegeResponse(int cardNumber, String activateDate, String deactivateDate, boolean[] doorAccessRights, int userPassword) {
            this.cardNumber = cardNumber;
            this.activateDate = activateDate;
            this.deactivateDate = deactivateDate;
            this.doorAccessRights = doorAccessRights;
            this.userPassword = userPassword;
        }

        public int getCardNumber() {
            return cardNumber;
        }

        public String getActivateDate() {
            return activateDate;
        }

        public String getDeactivateDate() {
            return deactivateDate;
        }

        public boolean[] getDoorAccessRights() {
            return doorAccessRights;
        }

        public int getUserPassword() {
            return userPassword;
        }

        @Override
        public String toString() {
            return "QueryAccessPrivilegeResponse{" +
                    "cardNumber=" + cardNumber +
                    ", activateDate=" + activateDate +
                    ", deactivateDate=" + deactivateDate +
                    ", doorAccessRights=" + doorAccessRights[0] + ", " + doorAccessRights[1] +
                    ", " + doorAccessRights[2] + ", " + doorAccessRights[3] +
                    ", userPassword=" + userPassword +
                    '}';
        }
    }
}
