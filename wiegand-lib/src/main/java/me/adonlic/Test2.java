package me.adonlic;

import me.adonlic.communication.DefaultUDPClient;
import me.adonlic.communication.UDPClient;
import me.adonlic.wiegand.WiegandPacket;
import me.adonlic.wiegand.commands.ConfigureIPCommand;
import me.adonlic.wiegand.commands.QueryControllerCommand;
import me.adonlic.wiegand.commands.SearchCommand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import static me.adonlic.wiegand.util.DataConversionUtil.bytesToHex;

public class Test2 {
    public static void main(String[] args) throws IOException {
        try {
            UDPClient udpClient2 = new DefaultUDPClient("192.168.1.105", 60000);
            QueryControllerCommand queryControllerCommand = new QueryControllerCommand(122227447);
            QueryControllerCommand.QueryControllerResponse queryControllerResponse = queryControllerCommand.execute(udpClient2);
            System.out.println(queryControllerResponse);


            /*// Configure IP
            DefaultUDPClient udpClient2 = new DefaultUDPClient("192.168.1.105", 60000); // Example IP and port
            InetAddress ipAddress = InetAddress.getByName("192.168.1.101");
            InetAddress subnetMask = InetAddress.getByName("255.255.255.0");
            InetAddress gateway = InetAddress.getByName("192.168.1.1");

            ConfigureIPCommand command = new ConfigureIPCommand(122227447, ipAddress, subnetMask, gateway);
            command.execute(udpClient2);

            udpClient2.close();*/

            /*// Search
            SearchCommand searchCommand = new SearchCommand();
            byte[] searchCommandBinary = searchCommand.toPacket().getRawData();
            DefaultUDPClient.broadcastAndReceive(
                    searchCommandBinary,
                60000,
                5000,
                (d) -> {
                    WiegandPacket wiegandPacket = new WiegandPacket(d);
                    SearchCommand.SearchResponse searchResponse = searchCommand.fromPacket(wiegandPacket);
                    System.out.println(searchResponse);
                }
            );*/

            // Create an instance of DefaultUDPClient with custom settings
            DefaultUDPClient udpClient = new DefaultUDPClient("localhost", 12345, 2000, 512, 100);

            // Example data to send
            byte[] commandBuffer = new byte[64];
            commandBuffer[0] = (byte) 0x17;
            commandBuffer[1] = (byte) 0x94;

            // Send data
            udpClient.send(commandBuffer);

            // Start receiving data
            udpClient.startReceiving(data -> {
                System.out.println("Received data: " + bytesToHex(data));
            });

            // Send and receive data in a single call
            byte[] response = udpClient.sendAndReceive(commandBuffer);
            System.out.println("Received response: " + bytesToHex(response));

            // Pull the next buffered data
            byte[] nextBufferedData = udpClient.pullNextBufferedData();
            if (nextBufferedData != null) {
                System.out.println("Pulled next buffered data: " + bytesToHex(nextBufferedData));
            }

            // Pull all buffered data
            List<byte[]> allBufferedData = udpClient.pullAllBufferedData();
            System.out.println("Pulled all buffered data:");
            for (byte[] data : allBufferedData) {
                System.out.println(bytesToHex(data));
            }

            // Pull buffered data with a condition
            List<byte[]> filteredData = udpClient.pullBufferedData(data -> data[0] == (byte) 0x17);
            System.out.println("Filtered data:");
            for (byte[] data : filteredData) {
                System.out.println(bytesToHex(data));
            }

            // Broadcast data and listen for responses
            DefaultUDPClient.broadcastAndReceive(commandBuffer, 60000, 5000, responseData -> {
                System.out.println("Broadcast response: " + bytesToHex(responseData));
            });

            // Stop receiving
            udpClient.stopReceiving();

            // Close the UDP client
            udpClient.close();

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception occurred: " + e.getMessage());
        }
    }
}
