package me.adonlic.communication;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The UDPClient interface defines the contract for a UDP client capable of sending, receiving, and managing UDP packets.
 */
public interface UDPClient {

    /**
     * Sends a UDP packet to the specified host and port.
     *
     * @param data The data to send.
     * @throws IOException If an I/O error occurs while sending the packet.
     */
    void send(byte[] data) throws IOException;

    /**
     * Starts a background thread to receive UDP packets.
     * The received data is stored in an internal buffer.
     */
    void startReceiving();

    /**
     * Starts a background thread to receive UDP packets and process them with the provided consumer.
     *
     * @param dataConsumer A Consumer to process each received packet.
     */
    void startReceiving(Consumer<byte[]> dataConsumer);

    /**
     * Stops the background thread that is receiving UDP packets.
     */
    void stopReceiving();

    /**
     * Receives a single UDP packet.
     *
     * @return The received data as a byte array, or null if no data is received within the timeout period.
     */
    byte[] receive();

    /**
     * Receives a single UDP packet and processes it using the provided consumer.
     *
     * @param dataConsumer A Consumer to process the received packet.
     * @return The received data as a byte array, or null if no data is received within the timeout period.
     */
    byte[] receive(Consumer<byte[]> dataConsumer);

    /**
     * Sends a UDP packet and waits for a response.
     *
     * @param data The data to send.
     * @return The received response data as a byte array, or null if no response is received within the timeout period.
     * @throws IOException If an I/O error occurs while sending or receiving the packet.
     */
    byte[] sendAndReceive(byte[] data) throws IOException;

    /**
     * Sends a UDP packet and waits for a response, then processes the response with the provided consumer.
     *
     * @param data         The data to send.
     * @param dataConsumer A Consumer to process the received response.
     * @return The received response data as a byte array, or null if no response is received within the timeout period.
     * @throws IOException If an I/O error occurs while sending or receiving the packet.
     */
    byte[] sendAndReceive(byte[] data, Consumer<byte[]> dataConsumer) throws IOException;

    /**
     * Pulls the next available buffered data packet.
     *
     * @return The next buffered data packet as a byte array, or null if the buffer is empty.
     */
    byte[] pullNextBufferedData();

    /**
     * Checks if there is any buffered data available.
     *
     * @return True if there is at least one buffered data packet; false otherwise.
     */
    boolean hasNextBufferedData();

    /**
     * Pulls all buffered data packets that match the specified condition.
     *
     * @param condition A Predicate to filter buffered data packets.
     * @return A list of buffered data packets that satisfy the condition.
     */
    List<byte[]> pullBufferedData(Predicate<byte[]> condition);

    /**
     * Pulls all buffered data packets.
     *
     * @return A list of all buffered data packets.
     */
    List<byte[]> pullAllBufferedData();

    /**
     * Closes the UDP client socket and stops any background receiving thread.
     */
    void close();
}