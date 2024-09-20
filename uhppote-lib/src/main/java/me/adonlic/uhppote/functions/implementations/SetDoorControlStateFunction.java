package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.ByteConversionUtil;
import me.adonlic.uhppote.util.DataConversionUtil;
import me.adonlic.uhppote.types.DoorControlState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * Function to set the control state of a door on a controller.
 */
public class SetDoorControlStateFunction implements ProtocolFunction<SetDoorControlStateFunction.SetDoorControlStateResponse> {

    private static final Logger logger = LogManager.getLogger(SetDoorControlStateFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x80;

    private final int controllerSN;
    private final int doorNumber;
    private final DoorControlState controlState;

    /**
     * Constructs a new SetDoorControlStateFunction.
     *
     * @param controllerSN The serial number of the controller.
     * @param doorNumber   The door number to set the control state for (1, 2, 3, or 4).
     * @param controlState The desired control state (Normally Open, Normally Closed, Controlled Automatically).
     */
    public SetDoorControlStateFunction(int controllerSN, int doorNumber, DoorControlState controlState) {
        this.controllerSN = controllerSN;
        this.doorNumber = doorNumber;
        this.controlState = controlState;
    }

    /**
     * Constructs the packet for setting the door control state.
     *
     * @return The ProtocolPacket containing the necessary data.
     */
    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for setting door control state

        // Set controller serial number (bytes 4-7)
        System.arraycopy(ByteConversionUtil.intToBytesLE(controllerSN), 0, data, 4, 4);

        // Set door number (byte 8)
        data[8] = (byte) doorNumber;

        // Set control state (byte 9)
        data[9] = (byte) controlState.getValue();

        logger.debug("SetDoorControlStateFunction packet created: {}", ByteConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    /**
     * Parses the response packet received from the controller.
     *
     * @param packet The response packet.
     * @return A response object containing the result of the command.
     */
    @Override
    public SetDoorControlStateResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Extract controller serial number and success flag
        int controllerSN = ByteConversionUtil.bytesToIntLE(Arrays.copyOfRange(data, 4, 8));
        int success = data[8];  // Success flag (1 = success, 0 = failure)

        SetDoorControlStateResponse responseObj = new SetDoorControlStateResponse(controllerSN, success == 1);
        logger.debug("Parsed SetDoorControlStateResponse: {}", responseObj);

        return responseObj;
    }

    @Override
    public SetDoorControlStateResponse execute(UDPClient client) throws IOException {
        logger.info("Executing SetDoorControlStateFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        if (response != null) {
            logger.debug("Response received: {}", ByteConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for SetDoorControlStateFunction.");
            return null;
        }
    }

    /**
     * Represents the response to a SetDoorControlStateFunction.
     */
    public static class SetDoorControlStateResponse {
        private final int controllerSN;
        private final boolean success;

        public SetDoorControlStateResponse(int controllerSN, boolean success) {
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
            return "SetDoorControlStateResponse{" +
                    "controllerSN=" + controllerSN +
                    ", success=" + success +
                    '}';
        }
    }
}
