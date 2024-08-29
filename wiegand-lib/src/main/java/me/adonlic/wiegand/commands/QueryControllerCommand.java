package me.adonlic.wiegand.commands;

import me.adonlic.communication.UDPClient;
import me.adonlic.wiegand.WiegandCommand;
import me.adonlic.wiegand.WiegandPacket;
import me.adonlic.wiegand.util.DataConversionUtil;
import me.adonlic.wiegand.util.Enums.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Represents the command to query a Wiegand controller for its status and latest record.
 */
public class QueryControllerCommand implements WiegandCommand {

    private static final Logger logger = LogManager.getLogger(QueryControllerCommand.class);
    private static final byte FUNCTION_ID = (byte) 0x20;

    private final int controllerSN;

    /**
     * Constructs a new QueryControllerCommand.
     *
     * @param controllerSN The serial number of the controller to query.
     */
    public QueryControllerCommand(int controllerSN) {
        this.controllerSN = controllerSN;
    }

    /**
     * Constructs the Wiegand packet for the query controller command.
     *
     * @return The WiegandPacket representing the query controller command.
     */
    @Override
    public WiegandPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Command start
        data[1] = FUNCTION_ID;  // Function ID for querying controller

        // Set controller serial number (bytes 4-7, low to high byte for little-endian)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        logger.debug("QueryControllerCommand packet created: {}", DataConversionUtil.bytesToHex(data));
        return new WiegandPacket(data);
    }

    /**
     * Parses the response packet from the controller to extract the status and latest record information.
     *
     * @param packet The response packet.
     * @return A QueryControllerResponse object containing parsed data.
     */
    @Override
    public QueryControllerResponse fromPacket(WiegandPacket packet) {
        byte[] data = packet.getRawData();
        int snReceived = DataConversionUtil.bytesToIntLE(new byte[]{data[4], data[5], data[6], data[7]});
        if (snReceived != controllerSN) {
            logger.error("Received SN does not match the requested controller SN.");
            return null;
        }

        int latestRecordIndex = DataConversionUtil.bytesToIntLE(new byte[]{data[8], data[9], data[10], data[11]});
        RecordType recordType = RecordType.fromInt(data[12]);
        Validation validation = Validation.fromInt(data[13]);
        DoorNumber doorNumber = DoorNumber.fromInt(data[14]);
        InOut inOut = InOut.fromInt(data[15]);

        // Other data parsing follows...
        int cardNumber = DataConversionUtil.bytesToIntLE(new byte[]{data[16], data[17], data[18], data[19]});
        String swipeTime = DataConversionUtil.parseSwipeTime(new byte[]{data[20], data[21], data[22], data[23], data[24], data[25], data[26]});
        int swipeReason = data[27];
        Status[] doorStatuses = {
                Status.fromInt(data[28]),
                Status.fromInt(data[29]),
                Status.fromInt(data[30]),
                Status.fromInt(data[31])
        };
        Status[] pushButtonStatuses = {
                Status.fromInt(data[32]),
                Status.fromInt(data[33]),
                Status.fromInt(data[34]),
                Status.fromInt(data[35])
        };
        ErrorCode errorCode = ErrorCode.fromInt(data[36]);
        int controllerHour = DataConversionUtil.bcdToInt(data[37]);
        int controllerMinute = DataConversionUtil.bcdToInt(data[38]);
        int controllerSecond = DataConversionUtil.bcdToInt(data[39]);
        int sequenceId = DataConversionUtil.bytesToIntLE(new byte[]{data[40], data[41], data[42], data[43]});
        RelayStatus relayStatus = RelayStatus.fromInt(data[49]);
        InputStatus inputStatus = InputStatus.fromInt(data[50]);
        int controllerYear = DataConversionUtil.bcdToInt(data[51]);
        int controllerMonth = DataConversionUtil.bcdToInt(data[52]);
        int controllerDay = DataConversionUtil.bcdToInt(data[53]);

        // Construct and return a result object
        QueryControllerResponse responseObj = new QueryControllerResponse(snReceived, latestRecordIndex, recordType, validation, doorNumber, inOut, cardNumber,
                swipeTime, swipeReason, doorStatuses, pushButtonStatuses, errorCode, controllerHour, controllerMinute,
                controllerSecond, sequenceId, relayStatus, inputStatus, controllerYear, controllerMonth, controllerDay);
        logger.debug("Parsed QueryControllerResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public QueryControllerResponse execute(UDPClient client) throws IOException {
        logger.info("Executing QueryControllerCommand with UDP client.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new WiegandPacket(response));
        } else {
            logger.warn("No response received from QueryControllerCommand.");
            return null;
        }
    }

    /**
     * Represents the result of a QueryControllerCommand operation.
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
        private final int swipeReason;
        private final Status[] doorStatuses;
        private final Status[] pushButtonStatuses;
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
                      DoorNumber doorNumber, InOut inOut, int cardNumber, String swipeTime, int swipeReason,
                      Status[] doorStatuses, Status[] pushButtonStatuses, ErrorCode errorCode, int controllerHour,
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

        public int getSwipeReason() {
            return swipeReason;
        }

        public Status[] getDoorStatuses() {
            return doorStatuses;
        }

        public Status[] getPushButtonStatuses() {
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
