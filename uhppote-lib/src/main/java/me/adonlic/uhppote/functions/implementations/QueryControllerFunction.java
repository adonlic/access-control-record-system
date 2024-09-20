package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.types.*;
import me.adonlic.uhppote.util.ByteConversionUtil;
import me.adonlic.uhppote.util.BCDUtil;
import me.adonlic.uhppote.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * Represents the function to query a controller for its status and latest record.
 */
public class QueryControllerFunction implements ProtocolFunction<QueryControllerFunction.QueryControllerResponse> {

    private static final Logger logger = LogManager.getLogger(QueryControllerFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x20;

    private final int controllerSN;

    /**
     * Constructs a new QueryControllerFunction.
     *
     * @param controllerSN The serial number of the controller to query.
     */
    public QueryControllerFunction(int controllerSN) {
        this.controllerSN = controllerSN;
    }

    /**
     * Constructs the packet for the query controller function.
     *
     * @return The ProtocolPacket representing the query controller function.
     */
    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for querying controller

        // Set controller serial number (bytes 4-7, low to high byte for little-endian)
        System.arraycopy(ByteConversionUtil.intToBytesLE(controllerSN), 0, data, 4, 4);

        logger.debug("QueryControllerFunction packet created: {}", ByteConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    /**
     * Parses the response packet from the controller to extract the status and latest record information.
     *
     * @param packet The response packet.
     * @return A QueryControllerResponse object containing parsed data.
     */
    @Override
    public QueryControllerResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Parsing controller serial number
        int snReceived = ByteConversionUtil.bytesToIntLE(Arrays.copyOfRange(data, 4, 8));
        if (snReceived != controllerSN) {
            logger.error("Received SN does not match the requested controller SN.");
            return null;
        }

        // Parsing the response data
        int latestRecordIndex = ByteConversionUtil.bytesToIntLE(Arrays.copyOfRange(data, 8, 12));
        RecordType recordType = RecordType.fromInt(data[12]);
        Validation validation = Validation.fromInt(data[13]);
        DoorNumber doorNumber = DoorNumber.fromInt(data[14]);
        InOut inOut = InOut.fromInt(data[15]);
        int cardNumber = ByteConversionUtil.bytesToIntLE(Arrays.copyOfRange(data, 16, 20));
        String swipeTime = DateUtil.parseSwipeTime(Arrays.copyOfRange(data, 20, 27));
        SwipeReason swipeReason = SwipeReason.fromCode(data[27]);

        // Parsing door and push button statuses
        DoorStatus[] doorStatuses = {
                DoorStatus.fromInt(data[28]),
                DoorStatus.fromInt(data[29]),
                DoorStatus.fromInt(data[30]),
                DoorStatus.fromInt(data[31])
        };
        DoorStatus[] pushButtonStatuses = {
                DoorStatus.fromInt(data[32]),
                DoorStatus.fromInt(data[33]),
                DoorStatus.fromInt(data[34]),
                DoorStatus.fromInt(data[35])
        };

        // Parsing controller time and status details
        ErrorCode errorCode = ErrorCode.fromInt(data[36]);
        int controllerHour = BCDUtil.bcdToInt(data[37]);
        int controllerMinute = BCDUtil.bcdToInt(data[38]);
        int controllerSecond = BCDUtil.bcdToInt(data[39]);
        int sequenceId = ByteConversionUtil.bytesToIntLE(Arrays.copyOfRange(data, 40, 44));
        RelayStatus relayStatus = RelayStatus.fromInt(data[49]);
        InputStatus inputStatus = InputStatus.fromInt(data[50]);
        int controllerYear = BCDUtil.bcdToInt(data[51]);
        int controllerMonth = BCDUtil.bcdToInt(data[52]);
        int controllerDay = BCDUtil.bcdToInt(data[53]);

        // Construct and return a result object
        QueryControllerResponse responseObj = new QueryControllerResponse(
                snReceived, latestRecordIndex, recordType, validation, doorNumber, inOut, cardNumber, swipeTime,
                swipeReason, doorStatuses, pushButtonStatuses, errorCode, controllerHour, controllerMinute,
                controllerSecond, sequenceId, relayStatus, inputStatus, controllerYear, controllerMonth, controllerDay
        );

        logger.debug("Parsed QueryControllerResponse: {}", responseObj);
        return responseObj;
    }

    @Override
    public QueryControllerResponse execute(UDPClient client) throws IOException {
        logger.info("Executing QueryControllerFunction with UDP client.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", ByteConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received from QueryControllerFunction.");
            return null;
        }
    }

    /**
     * Represents the result of a QueryControllerFunction operation.
     */
    public static class QueryControllerResponse {
        private final int controllerSN;
        private final int latestRecordIndex;
        private final RecordType recordType;
        private final Validation validation;
        private final DoorNumber doorNumber;
        private final InOut inOut;
        private final int cardNumber;
        private final String swipeTime;
        private final SwipeReason swipeReason;
        private final DoorStatus[] doorStatuses;
        private final DoorStatus[] pushButtonStatuses;
        private final ErrorCode errorCode;
        private final int controllerHour;
        private final int controllerMinute;
        private final int controllerSecond;
        private final int sequenceId;
        private final RelayStatus relayStatus;
        private final InputStatus inputStatus;
        private final int controllerYear;
        private final int controllerMonth;
        private final int controllerDay;

        public QueryControllerResponse(int controllerSN, int latestRecordIndex, RecordType recordType, Validation validation,
                                       DoorNumber doorNumber, InOut inOut, int cardNumber, String swipeTime, SwipeReason swipeReason,
                                       DoorStatus[] doorStatuses, DoorStatus[] pushButtonStatuses, ErrorCode errorCode, int controllerHour,
                                       int controllerMinute, int controllerSecond, int sequenceId, RelayStatus relayStatus,
                                       InputStatus inputStatus, int controllerYear, int controllerMonth, int controllerDay) {
            this.controllerSN = controllerSN;
            this.latestRecordIndex = latestRecordIndex;
            this.recordType = recordType;
            this.validation = validation;
            this.doorNumber = doorNumber;
            this.inOut = inOut;
            this.cardNumber = cardNumber;
            this.swipeTime = swipeTime;
            this.swipeReason = swipeReason;
            this.doorStatuses = doorStatuses;
            this.pushButtonStatuses = pushButtonStatuses;
            this.errorCode = errorCode;
            this.controllerHour = controllerHour;
            this.controllerMinute = controllerMinute;
            this.controllerSecond = controllerSecond;
            this.sequenceId = sequenceId;
            this.relayStatus = relayStatus;
            this.inputStatus = inputStatus;
            this.controllerYear = controllerYear;
            this.controllerMonth = controllerMonth;
            this.controllerDay = controllerDay;
        }

        public int getControllerSN() {
            return controllerSN;
        }

        public int getLatestRecordIndex() {
            return latestRecordIndex;
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

        public DoorStatus[] getDoorStatuses() {
            return doorStatuses;
        }

        public DoorStatus[] getPushButtonStatuses() {
            return pushButtonStatuses;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }

        public int getControllerHour() {
            return controllerHour;
        }

        public int getControllerMinute() {
            return controllerMinute;
        }

        public int getControllerSecond() {
            return controllerSecond;
        }

        public int getSequenceId() {
            return sequenceId;
        }

        public RelayStatus getRelayStatus() {
            return relayStatus;
        }

        public InputStatus getInputStatus() {
            return inputStatus;
        }

        public int getControllerYear() {
            return controllerYear;
        }

        public int getControllerMonth() {
            return controllerMonth;
        }

        public int getControllerDay() {
            return controllerDay;
        }

        @Override
        public String toString() {
            return "QueryControllerResponse{" +
                    "controllerSN=" + controllerSN +
                    ", latestRecordIndex=" + latestRecordIndex +
                    ", recordType=" + recordType +
                    ", validation=" + validation +
                    ", doorNumber=" + doorNumber +
                    ", inOut=" + inOut +
                    ", cardNumber=" + cardNumber +
                    ", swipeTime='" + swipeTime + '\'' +
                    ", swipeReason=" + swipeReason +
                    ", doorStatuses=" + Arrays.toString(doorStatuses) +
                    ", pushButtonStatuses=" + Arrays.toString(pushButtonStatuses) +
                    ", errorCode=" + errorCode +
                    ", controllerHour=" + controllerHour +
                    ", controllerMinute=" + controllerMinute +
                    ", controllerSecond=" + controllerSecond +
                    ", sequenceId=" + sequenceId +
                    ", relayStatus=" + relayStatus +
                    ", inputStatus=" + inputStatus +
                    ", controllerYear=" + controllerYear +
                    ", controllerMonth=" + controllerMonth +
                    ", controllerDay=" + controllerDay +
                    '}';
        }
    }
}
