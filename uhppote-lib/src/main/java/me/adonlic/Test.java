package me.adonlic;

import me.adonlic.communication.DefaultUDPClient;
import me.adonlic.communication.UDPClient;
import me.adonlic.uhppote.ProtocolPacket;
import me.adonlic.uhppote.functions.implementations.*;
import me.adonlic.uhppote.functions.implementations.access_privilege.AddAccessPrivilegeFunction;
import me.adonlic.uhppote.functions.implementations.access_privilege.QueryAccessPrivilegeByCardFunction;
import me.adonlic.uhppote.functions.implementations.record.GetRecordByIndexFunction;
import me.adonlic.uhppote.types.DoorControlState;
import me.adonlic.uhppote.types.DoorNumber;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Test {
    private static void searchFunction() throws IOException {
        SearchFunction searchFunction = new SearchFunction();
        byte[] searchFunctionBinary = searchFunction.toPacket().getRawData();
        DefaultUDPClient.broadcastAndReceive(
                searchFunctionBinary,
                60000,
                5000,
                (d) -> {
                    ProtocolPacket protocolPacket = new ProtocolPacket(d);
                    SearchFunction.SearchResponse searchResponse = searchFunction.fromPacket(protocolPacket);
                    // System.out.println(searchResponse);
                }
        );
    }

    private static void configureIPFunction(
            UDPClient udpClient,
            int controllerSN,
            InetAddress ip,
            InetAddress mask,
            InetAddress gateway
    )
            throws IOException {
        ConfigureIPFunction configureIPFunction = new ConfigureIPFunction(controllerSN, ip, mask, gateway);
        configureIPFunction.execute(udpClient);
    }

    private static void queryControllerFunction(UDPClient udpClient, int controllerSN) throws IOException {
        QueryControllerFunction queryControllerFunction = new QueryControllerFunction(controllerSN);
        QueryControllerFunction.QueryControllerResponse queryControllerResponse = queryControllerFunction
                .execute(udpClient);
    }

    private static void remoteOpenDoorFunction(UDPClient udpClient, int controllerSN, int doorNumber)
            throws IOException {
        OpenDoorFunction openDoorFunction = new OpenDoorFunction(controllerSN, doorNumber);
        OpenDoorFunction.OpenDoorResponse openDoorResponse = openDoorFunction.execute(udpClient);
        // System.out.println(openDoorResponse);
    }

    private static void adjustTimeFunction() {
        throw new Error("Not implemented yet");
    }

    private static void getRecordAtSpecifiedIndexFunction(UDPClient udpClient, int controllerSN, int recordIndex)
            throws IOException {
        GetRecordByIndexFunction getRecordFunction = new GetRecordByIndexFunction(controllerSN, recordIndex);
        GetRecordByIndexFunction.GetRecordResponse getRecordResponse = getRecordFunction.execute(udpClient);
        // System.out.println(getRecordResponse);
    }

    private static void setRecordIndexFunction() {
        throw new Error("Not implemented yet");
    }

    private static void getRecordIndexFunction() {
        throw new Error("Not implemented yet");
    }

    private static void addAccessPrivilegeFunction(
            UDPClient udpClient,
            int controllerSN,
            int cardNumber,
            String activateDate,
            String deactivateDate,
            boolean[] doorAccessRights
    )
            throws IOException {
        AddAccessPrivilegeFunction addAccessPrivilegeFunction = new AddAccessPrivilegeFunction(
                controllerSN,
                cardNumber,
                activateDate,
                deactivateDate,
                doorAccessRights
        );
        AddAccessPrivilegeFunction.AddAccessPrivilegeResponse addAccessPrivilegeResponse = addAccessPrivilegeFunction
                .execute(udpClient);
        System.out.println(addAccessPrivilegeResponse);
    }

    private static void queryAccessPrivilegeByCardFunction(UDPClient udpClient, int controllerSN, int cardNumber)
            throws IOException {
        QueryAccessPrivilegeByCardFunction queryAccessPrivilegeByCardFunction = new QueryAccessPrivilegeByCardFunction(
                controllerSN,
                cardNumber
        );
        QueryAccessPrivilegeByCardFunction.QueryAccessPrivilegeResponse queryAccessPrivilegeByCardResponse = queryAccessPrivilegeByCardFunction
                .execute(udpClient);
        // System.out.println(queryAccessPrivilegeByCardResponse);
    }

    private static void setDoorControlStateFunction(
            UDPClient udpClient,
            int controllerSN,
            int doorNumber,
            DoorControlState doorControlState
    )
            throws IOException {
        SetDoorControlStateFunction setDoorStateFunction = new SetDoorControlStateFunction(
                controllerSN,
                doorNumber,
                doorControlState
        );
        SetDoorControlStateFunction.SetDoorControlStateResponse setDoorStateResponse = setDoorStateFunction
                .execute(udpClient);
    }

    private static void getDataServerInformationFunction(UDPClient udpClient, int controllerSN) throws IOException {
        GetDataServerInfoFunction getDataServerInfoFunction = new GetDataServerInfoFunction(controllerSN);
        GetDataServerInfoFunction.GetDataServerInfoResponse getDataServerInfoResponse = getDataServerInfoFunction
                .execute(udpClient);
        // System.out.println(getDataServerInfoResponse);
    }

    private static void listenAsDataServer(UDPClient udpClient, int controllerSN) {
        // DataServer (it is receiving 0x20 function, eg. Query Controller)
        udpClient.startReceiving((data) -> {
            QueryControllerFunction.QueryControllerResponse controllerResponse = new QueryControllerFunction(
                    controllerSN
            ).fromPacket(
                    new ProtocolPacket(data)
            );
            // System.out.println(controllerResponse);
        });
    }

    public static void main(String[] args) throws IOException {
        try {
            // String controllerIP = "192.168.1.105";
            String controllerIP = "169.254.161.154";
            int controllerPort = 60000;
            String dataServerHost = "localhost";
            int dataServerPort = 61005;
            int controllerSN = 122227447;
            int doorNumber = DoorNumber.DOOR_1.getValue();
            int cardNumber1 = 1016142883;

            UDPClient udpClient = new DefaultUDPClient(controllerIP, controllerPort); // Active
            UDPClient dataServerUDPClient = new DefaultUDPClient(dataServerHost, dataServerPort); // Passive

            // #########################################
            // ### UNCOMMENT FUNCTION CALLS TO TEST: ###
            // #########################################

            // Search
            // searchFunction();

            // Configure IP
            /*InetAddress ipAddress = InetAddress.getByName("192.168.1.101");
            InetAddress subnetMask = InetAddress.getByName("255.255.255.0");
            InetAddress gateway = InetAddress.getByName("192.168.1.1");
            configureIPFunction(udpClient, controllerSN, ipAddress, subnetMask, gateway);*/

            // Query Controller
            queryControllerFunction(udpClient, controllerSN);

            // Open Door
            // remoteOpenDoorFunction(udpClient, controllerSN, doorNumber);

            // Retrieve the latest record (use index 0xFFFFFFFF)
            // getRecordAtSpecifiedIndexFunction(udpClient, controllerSN, LAST_RECORD);

            // Query Access Privilege By Card
            // queryAccessPrivilegeByCardFunction(udpClient, controllerSN, cardNumber1);
            // queryAccessPrivilegeByCardFunction(udpClient, controllerSN, cardNumber1);

            // Add/Edit Access Privilege
            /*boolean[] doorRights = { true, false, false, false };
            addAccessPrivilegeFunction(
                    udpClient,
                    controllerSN,
                    cardNumber1,
                    "20240101",
                    "20241231",
                    doorRights
            );*/

            // Set Door 1 to be controlled automatically
            // setDoorControlStateFunction(udpClient, controllerSN, doorNumber, DoorControlState.CONTROLLED_AUTOMATICALLY);

            // Get DataServer Information
            // getDataServerInformationFunction(udpClient, controllerSN);

            // listen as DataServer
            listenAsDataServer(dataServerUDPClient, controllerSN);

            // If you uncomment this the program will end
            // udpClient.close();
            // dataServerUDPClient.close();









            // ####################################
            // ############### OLD: ###############
            // ####################################

            /*// Configure IP
            DefaultUDPClient udpClient2 = new DefaultUDPClient("192.168.1.105", 60000); // Example IP and port
            InetAddress ipAddress = InetAddress.getByName("192.168.1.101");
            InetAddress subnetMask = InetAddress.getByName("255.255.255.0");
            InetAddress gateway = InetAddress.getByName("192.168.1.1");

            ConfigureIPFunction command = new ConfigureIPFunction(122227447, ipAddress, subnetMask, gateway);
            command.execute(udpClient2);

            udpClient2.close();*/

            /*// Search
            SearchFunction searchCommand = new SearchFunction();
            byte[] searchCommandBinary = searchCommand.toPacket().getRawData();
            DefaultUDPClient.broadcastAndReceive(
                    searchCommandBinary,
                60000,
                5000,
                (d) -> {
                    WiegandPacket wiegandPacket = new WiegandPacket(d);
                    SearchFunction.SearchResponse searchResponse = searchCommand.fromPacket(wiegandPacket);
                    System.out.println(searchResponse);
                }
            );*/

//            // Create an instance of DefaultUDPClient with custom settings
//            DefaultUDPClient udpClient = new DefaultUDPClient("localhost", 12345, 2000, 512, 100);
//
//            // Example data to send
//            byte[] commandBuffer = new byte[64];
//            commandBuffer[0] = (byte) 0x17;
//            commandBuffer[1] = (byte) 0x94;
//
//            // Send data
//            udpClient.send(commandBuffer);
//
//            // Start receiving data
//            udpClient.startReceiving(data -> {
//                System.out.println("Received data: " + bytesToHex(data));
//            });
//
//            // Send and receive data in a single call
//            byte[] response = udpClient.sendAndReceive(commandBuffer);
//            System.out.println("Received response: " + bytesToHex(response));
//
//            // Pull the next buffered data
//            byte[] nextBufferedData = udpClient.pullNextBufferedData();
//            if (nextBufferedData != null) {
//                System.out.println("Pulled next buffered data: " + bytesToHex(nextBufferedData));
//            }
//
//            // Pull all buffered data
//            List<byte[]> allBufferedData = udpClient.pullAllBufferedData();
//            System.out.println("Pulled all buffered data:");
//            for (byte[] data : allBufferedData) {
//                System.out.println(bytesToHex(data));
//            }
//
//            // Pull buffered data with a condition
//            List<byte[]> filteredData = udpClient.pullBufferedData(data -> data[0] == (byte) 0x17);
//            System.out.println("Filtered data:");
//            for (byte[] data : filteredData) {
//                System.out.println(bytesToHex(data));
//            }
//
//            // Broadcast data and listen for responses
//            DefaultUDPClient.broadcastAndReceive(commandBuffer, 60000, 5000, responseData -> {
//                System.out.println("Broadcast response: " + bytesToHex(responseData));
//            });
//
//            // Stop receiving
//            udpClient.stopReceiving();
//
//            // Close the UDP client
//            udpClient.close();

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception occurred: " + e.getMessage());
        }
    }
}
