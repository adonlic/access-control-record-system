//package me.adonlic;
//
//import me.adonlic.communication.DefaultUDPClient;
//import me.adonlic.communication.UDPClient;
//import me.adonlic.discovery.ControllerDiscoveryService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.function.Predicate;
//
//public class Test {
//    private static Logger logger = LogManager.getLogger(Test.class);
//
//    private void onPong(byte[] commandBuffer) {}
//
//    public static void main(String[] args) {
//        try {
//            // Define packet validation logic externally
//            Predicate<byte[]> packetValidator = packet -> packet[0] != 0 && packet[1] != 0;
//
//            // Create UDPClient instance with host and port
//            UDPClient udpClient = new DefaultUDPClient("localhost", 9876, packetValidator);
//
//            // Send data
//            byte[] dataToSend = "Hello, Server!".getBytes();
//            udpClient.send(dataToSend);
//
//            // Start receiving packets in a new thread with callback
//            udpClient.startReceiving(packet -> {
//                System.out.println("Received packet: " + new String(packet));
//            });
//
//            // Receive data non-blockingly
//            byte[] receivedData = udpClient.receiveNonBlocking(1000L, false);
//            if (receivedData != null) {
//                System.out.println("Non-blocking receive: " + new String(receivedData));
//            }
//
//            // Send data and receive response
//            byte[] responseData = udpClient.sendAndReceive(dataToSend, 1000L, false);
//            if (responseData != null) {
//                System.out.println("Response received: " + new String(responseData));
//            }
//
//            // Pull the next packet from the buffer
//            byte[] nextPacket = udpClient.pullNextData();
//            if (nextPacket != null) {
//                System.out.println("Pulled packet: " + new String(nextPacket));
//            }
//
//            // Retrieve all buffered packets
//            for (byte[] packet : udpClient.retrieveAllBufferedPackets()) {
//                System.out.println("Buffered packet: " + new String(packet));
//            }
//
//            // Broadcast and receive responses
//            udpClient.broadcastAndReceive("Broadcast message".getBytes(), 9876, 5000L, responsePacket -> {
//                System.out.println("Received broadcast response: " + new String(responsePacket));
//            });
//
//            // Stop receiving and close the client
//            udpClient.stopReceiving();
//            udpClient.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//        // ControllerDiscoveryService.ping(new byte[] { (byte) 0x17, (byte) 0x94 }, (commandBuffer) -> {
//        //     // handle "pong"
//        //     logger.info("Controller responded: {}", commandBuffer);
//        //     return null;
//        // });
//        // logger.error("test {}", "podatak");
//
//        try {
//            // UDPClient client = new DefaultUDPClient(6000);
//
//
//            // Example usage of blocking receive with callback
//            // client.startReceiving(packetData -> {
//            //     logger.info("Received packet: {}", new String(packetData));
//            // });
//
//            // Example usage of send and receive in the same function
//            // byte[] response = client.sendAndReceive("Hello Server".getBytes(), 2000L);
//            // if (response != null) {
//            //     logger.info("Response received: {}", new String(response));
//            // }
//
//            // Broadcast and receive responses
//            byte[] commandBuffer = new byte[64];
//            commandBuffer[0] = (byte) 0x17;
//            commandBuffer[1] = (byte) 0x94;
//            // client.broadcastAndReceive("Broadcast message".getBytes(), 6000, 5000, responseData -> {
//            client.broadcastAndReceive(commandBuffer, 60000, 5000, responseData -> {
//                logger.info("Received response: {}", new String(responseData));
//                logger.info("Received response: {}", responseData);
//            });
//
//            // Retrieve buffered packets one by one
//            byte[] nextPacket;
//            while ((nextPacket = client.pullNextData()) != null) {
//                logger.info("Next buffered packet: {}", new String(nextPacket));
//            }
//
//            // Retrieve all buffered packets
//            List<byte[]> allPackets = client.retrieveAllBufferedPackets();
//            for (byte[] packet : allPackets) {
//                logger.info("Buffered packet: {}", new String(packet));
//            }
//
//            client.close();
//
//        } catch (IOException e) {
//            logger.error("IOException occurred: {}", e.getMessage());
//        }
//    }
//}
