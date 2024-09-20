package me.adonlic.uhppote.functions.implementations.record;

import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.ProtocolFunction;
import me.adonlic.uhppote.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Function to get the record index of the downloaded record.
 */
public class GetRecordIndexFunction implements ProtocolFunction<GetRecordIndexFunction.GetRecordIndexResponse> {

    private static final Logger logger = LogManager.getLogger(GetRecordIndexFunction.class);
    private static final byte FUNCTION_ID = (byte) 0xB4;

    private final int controllerSN;

    public GetRecordIndexFunction(int controllerSN) {
        this.controllerSN = controllerSN;
    }

    @Override
    public ProtocolPacket toPacket() {
        byte[] data = new byte[64];
        data[0] = (byte) 0x17;
        data[1] = FUNCTION_ID;

        // Set controller serial number
        byte[] snBytes = DataConversionUtil.intToBytesLE(controllerSN);
        System.arraycopy(snBytes, 0, data, 4, 4);

        return new ProtocolPacket(data);
    }

    @Override
    public GetRecordIndexResponse fromPacket(ProtocolPacket packet) {
        byte[] data = packet.getRawData();
        int recordIndex = DataConversionUtil.bytesToIntLE(new byte[]{data[8], data[9], data[10], data[11]});
        return new GetRecordIndexResponse(recordIndex);
    }

    @Override
    public GetRecordIndexResponse execute(UDPClient client) throws IOException {
        byte[] data = toPacket().getRawData();
        byte[] response = client.sendAndReceive(data);
        return fromPacket(new ProtocolPacket(response));
    }

    public static class GetRecordIndexResponse {
        private final int recordIndex;

        public GetRecordIndexResponse(int recordIndex) {
            this.recordIndex = recordIndex;
        }

        public int getRecordIndex() {
            return recordIndex;
        }
    }
}
