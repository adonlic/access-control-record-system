package me.adonlic.uhppote.functions.implementations.record;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.types.*;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to retrieve the record of a specified index from a controller.
 */
public class GetRecordByIndexFunction implements ProtocolFunction<GetRecordByIndexFunction.GetRecordResponse> {

    public static final int FIRST_RECORD = 0x00000000;
    public static final int LAST_RECORD = 0xFFFFFFFF;

    private static final Logger logger = LogManager.getLogger(GetRecordByIndexFunction.class);
    private static final byte FUNCTION_ID = (byte) 0xB0;

    private final int controllerSN;
    private final int recordIndex;

    /**
     * Constructs a new GetRecordByIndexFunction.
     *
     * @param controllerSN The serial number of the controller.
     * @param recordIndex  The index of the record to retrieve.
     */
    public GetRecordByIndexFunction(int controllerSN, int recordIndex) {
        this.controllerSN = controllerSN;
        this.recordIndex = recordIndex;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for getting record by index

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set record index (bytes 8-11)
        byte[] indexBytes = DataConversionUtil.intToBytesLE(recordIndex);
        System.arraycopy(indexBytes, 0, data, 8, 4);

        logger.debug("GetRecordByIndexFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public GetRecordResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        int snReceived = DataConversionUtil.bytesToIntLE(new byte[]{data[4], data[5], data[6], data[7]});
        int recordIndex = DataConversionUtil.bytesToIntLE(new byte[]{data[8], data[9], data[10], data[11]});
        RecordType recordType = RecordType.fromInt(data[12]);
        Validation validation = Validation.fromInt(data[13]);
        DoorNumber doorNumber = DoorNumber.fromInt(data[14]);
        InOut inOut = InOut.fromInt(data[15]);

        int cardNumber = DataConversionUtil.bytesToIntLE(new byte[]{data[16], data[17], data[18], data[19]});
        String swipeTime = DataConversionUtil.parseSwipeTime(new byte[]{data[20], data[21], data[22], data[23], data[24], data[25], data[26]});
        SwipeReason swipeReason = SwipeReason.fromCode(data[27]);

        GetRecordResponse responseObj = new GetRecordResponse(snReceived, recordIndex, recordType, validation, doorNumber, inOut, cardNumber, swipeTime, swipeReason);
        logger.debug("Parsed GetRecordResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public GetRecordResponse execute(UDPClient client) throws IOException {
        logger.info("Executing GetRecordByIndexFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for GetRecordByIndexFunction.");
            return null;
        }
    }

    /**
     * Represents the response to a GetRecordByIndexFunction.
     */
    public static class GetRecordResponse {
        private final int controllerSN;
        private final int recordIndex;
        private final RecordType recordType;
        private final Validation validation;
        private final DoorNumber doorNumber;
        private final InOut inOut;
        private final int cardNumber;
        private final String swipeTime;
        private final SwipeReason swipeReason;

        public GetRecordResponse(int controllerSN, int recordIndex, RecordType recordType, Validation validation, DoorNumber doorNumber,
                                 InOut inOut, int cardNumber, String swipeTime, SwipeReason swipeReason) {
            this.controllerSN = controllerSN;
            this.recordIndex = recordIndex;
            this.recordType = recordType;
            this.validation = validation;
            this.doorNumber = doorNumber;
            this.inOut = inOut;
            this.cardNumber = cardNumber;
            this.swipeTime = swipeTime;
            this.swipeReason = swipeReason;
        }

        public int getControllerSN() {
            return controllerSN;
        }

        public int getRecordIndex() {
            return recordIndex;
        }

        public RecordType getRecordType() {
            return recordType;
        }

        public Validation getValidation() {
            return validation;
        }

        public DoorNumber getDoorNumber() {
            return doorNumber;
        }

        public InOut getInOut() {
            return inOut;
        }

        public int getCardNumber() {
            return cardNumber;
        }

        public String getSwipeTime() {
            return swipeTime;
        }

        public SwipeReason getSwipeReason() {
            return swipeReason;
        }

        @Override
        public String toString() {
            return "GetRecordResponse{" +
                    "controllerSN=" + controllerSN +
                    ", recordIndex=" + recordIndex +
                    ", recordType=" + recordType +
                    ", validation=" + validation +
                    ", doorNumber=" + doorNumber +
                    ", inOut=" + inOut +
                    ", cardNumber=" + cardNumber +
                    ", swipeTime='" + swipeTime + '\'' +
                    ", swipeReason=" + swipeReason +
                    '}';
        }
    }
}
