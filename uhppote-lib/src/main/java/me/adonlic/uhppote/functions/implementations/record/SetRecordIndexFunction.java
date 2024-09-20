package me.adonlic.uhppote.functions.implementations.record;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to set the record index of the downloaded record.
 */
public class SetRecordIndexFunction implements ProtocolFunction<SetRecordIndexFunction.SetRecordIndexResponse> {

    private static final Logger logger = LogManager.getLogger(SetRecordIndexFunction.class);
    private static final byte FUNCTION_ID = (byte) 0xB2;

    private final int controllerSN;
    private final int recordIndex;

    public SetRecordIndexFunction(int controllerSN, int recordIndex) {
        this.controllerSN = controllerSN;
        this.recordIndex = recordIndex;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;
        data[1] = FUNCTION_ID;

        // Set controller serial number
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        // Set the record index
        byte[] recordIndexBytes = DataConversionUtil.intToBytesLE(recordIndex);
        System.arraycopy(recordIndexBytes, 0, data, 8, 4);

        // Set flags
        data[12] = (byte) 0x55;
        data[13] = (byte) 0xAA;
        data[14] = (byte) 0xAA;
        data[15] = (byte) 0x55;

        return new ProtocolPacket(data);
    }

    @Override
    public SetRecordIndexResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();
        int success = data[8];
        return new SetRecordIndexResponse(success == 1);
    }

    @Override
    public SetRecordIndexResponse execute(UDPClient client) throws IOException {
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        return fromPacket(new ProtocolPacket(response));
    }

    public static class SetRecordIndexResponse {
        private final boolean success;

        public SetRecordIndexResponse(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
