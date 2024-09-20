package me.adonlic.uhppote.functions.implementations;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Function to set the current time on the controller.
 */
public class SetTimeFunction implements ProtocolFunction<SetTimeFunction.SetTimeResponse> {

    private static final Logger logger = LogManager.getLogger(SetTimeFunction.class);
    private static final byte FUNCTION_ID = (byte) 0x30;

    private final int controllerSN;
    private final Date currentTime;

    /**
     * Constructs a new SetTimeFunction.
     *
     * @param controllerSN The serial number of the controller.
     * @param currentTime  The current time to set on the controller.
     */
    public SetTimeFunction(int controllerSN, Date currentTime) {
        this.controllerSN = controllerSN;
        this.currentTime = currentTime;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;  // Function start
        data[1] = FUNCTION_ID;  // Function ID for setting time

        // Set controller serial number (bytes 4-7)
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set current time (BCD-encoded)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = dateFormat.format(currentTime);
        byte[] timeBytes = DataConversionUtil.bcdToBytes(formattedDate);
        System.arraycopy(timeBytes, 0, data, 8, 7);  // Store year, month, day, hour, minute, second

        logger.debug("SetTimeFunction packet created: {}", DataConversionUtil.bytesToHex(data));
        return new ProtocolPacket(data);
    }

    @Override
    public SetTimeResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();

        // Byte[8] should contain 1 for success, 0 for failure
        boolean success = data[8] == 1;
        return new SetTimeResponse(success);
    }

    @Override
    public SetTimeResponse execute(UDPClient client) throws IOException {
        logger.info("Executing SetTimeFunction.");
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);

        if (response != null) {
            logger.debug("Response received: {}", DataConversionUtil.bytesToHex(response));
            return fromPacket(new ProtocolPacket(response));
        } else {
            logger.warn("No response received for SetTimeFunction.");
            return new SetTimeResponse(false); // Indicate failure if no response
        }
    }

    /**
     * Represents the response to SetTimeFunction.
     */
    public static class SetTimeResponse {
        private final boolean success;

        public SetTimeResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "SetTimeResponse{" +
                    "success=" + success +
                    '}';
        }
    }
}
