package me.adonlic.communication;

import me.adonlic.wiegand.util.DataConversionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * DefaultUDPClient is a standard implementation of the UDPClient interface.
 * It provides methods to send and receive UDP packets, handle broadcasts, and manage buffered data.
 */
public class DefaultUDPClient implements UDPClient {

    private static final Logger logger = LogManager.getLogger(DefaultUDPClient.class);

    private static final int DEFAULT_TIMEOUT_MILLIS = 1000;
    private static final int DEFAULT_RECEIVE_BUFFER_SIZE = 1024;
    private static final int DEFAULT_QUEUE_SIZE = 1024;

    private final String host;
    private final int port;
    private final InetAddress address;
    private final DatagramSocket socket;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final LinkedList<byte[]> receivedDataQueue;
    private final int receiveBufferSize;
    private final int queueSize;
    private Thread receiveThread;

    /**
     * Constructs a DefaultUDPClient with a specified host, port, and default settings.
     *
     * @param host The host address to use for the socket.
     * @param port The port number to bind the socket.
     * @throws SocketException     If the socket could not be opened or bound.
     * @throws UnknownHostException If the host address is invalid.
     */
    public DefaultUDPClient(String host, int port) throws SocketException, UnknownHostException {
        this(host, port, DEFAULT_TIMEOUT_MILLIS, DEFAULT_RECEIVE_BUFFER_SIZE, DEFAULT_QUEUE_SIZE);
    }

    /**
     * Constructs a DefaultUDPClient with a specified host, port, and custom settings.
     *
     * @param host              The host address to use for the socket.
     * @param port              The port number to bind the socket.
     * @param timeoutMillis     The timeout for socket operations in milliseconds.
     * @param receiveBufferSize The size of the receive buffer in bytes.
     * @param queueSize         The maximum number of data to store in the buffer.
     * @throws SocketException     If the socket could not be opened or bound.
     * @throws UnknownHostException If the host address is invalid.
     */
    public DefaultUDPClient(String host, int port, Integer timeoutMillis, Integer receiveBufferSize, Integer queueSize)
            throws SocketException, UnknownHostException {
        this.host = host;
        this.port = port;
        this.receiveBufferSize = receiveBufferSize != null ? receiveBufferSize : DEFAULT_RECEIVE_BUFFER_SIZE;
        this.queueSize = queueSize != null ? queueSize : DEFAULT_QUEUE_SIZE;
        this.address = InetAddress.getByName(host);
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(timeoutMillis != null ? timeoutMillis : DEFAULT_TIMEOUT_MILLIS);
        this.receivedDataQueue = new LinkedList<>();
    }

    @Override
    public void send(byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
        logger.info("Sent data to {}:{} - {}", host, port, DataConversionUtil.bytesToHex(data));
    }

    @Override
    public void startReceiving() {
        startReceiving(null);
    }

    @Override
    public void startReceiving(Consumer<byte[]> dataConsumer) {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Receiving thread is already running.");
            return;
        }

        receiveThread = new Thread(() -> {
            logger.info("Receiving thread started.");
            while (running.get()) {
                receive(dataConsumer);
            }
            logger.debug("Receiving thread stopped.");
        }, String.format("%s:%s", host, port)); // String.format("UDP-Client-%d", hashCode()) looks good too
        receiveThread.start();
    }

    @Override
    public void stopReceiving() {
        running.set(false);
        if (receiveThread != null) {
            receiveThread.interrupt();
            logger.info("Receiving thread interrupted.");
        }
    }

    @Override
    public byte[] receive() {
        return receive(null);
    }

    @Override
    public byte[] receive(Consumer<byte[]> dataConsumer) {
        byte[] buffer = new byte[receiveBufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
            logger.debug("Received data from {}:{} - {}", packet.getAddress(), packet.getPort(), DataConversionUtil.bytesToHex(data));

            if (dataConsumer != null) {
                dataConsumer.accept(data);
            } else {
                storeData(data);
            }

            return data;
        } catch (SocketTimeoutException e) {
            // logger.debug("Socket timeout reached while waiting for packet.");
        } catch (IOException e) {
            logger.error("Error during packet receiving: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public byte[] sendAndReceive(byte[] data) throws IOException {
        return sendAndReceive(data, null);
    }

    @Override
    public byte[] sendAndReceive(byte[] data, Consumer<byte[]> dataConsumer) throws IOException {
        send(data);
        return receive(dataConsumer);
    }

    @Override
    public synchronized byte[] pullNextBufferedData() {
        return receivedDataQueue.poll();
    }

    @Override
    public synchronized boolean hasNextBufferedData() {
        return !receivedDataQueue.isEmpty();
    }

    @Override
    public synchronized List<byte[]> pullBufferedData(Predicate<byte[]> condition) {
        List<byte[]> result = new ArrayList<>();
        receivedDataQueue.removeIf(data -> {
            if (condition.test(data)) {
                result.add(data);
                return true;
            }
            return false;
        });
        return result;
    }

    @Override
    public synchronized List<byte[]> pullAllBufferedData() {
        List<byte[]> data = new ArrayList<>(receivedDataQueue);
        receivedDataQueue.clear();
        return data;
    }

    @Override
    public void close() {
        stopReceiving();
        if (socket != null && !socket.isClosed()) {
            socket.close();
            logger.info("Socket closed.");
        }
    }

    /**
     * Stores a packet in the buffer if the buffer is not full.
     *
     * @param packetData The packet data to store.
     */
    private synchronized void storeData(byte[] packetData) {
        if (receivedDataQueue.size() == queueSize) {
            receivedDataQueue.poll();
        }
        receivedDataQueue.add(packetData);
        logger.debug("Stored data in buffer: {}", DataConversionUtil.bytesToHex(packetData));
    }

    /**
     * Broadcasts a UDP packet and listens for responses for a specified duration.
     *
     * @param data                  The data to broadcast.
     * @param port                  The port to broadcast on.
     * @param listeningDurationMillis The duration in milliseconds to listen for responses.
     * @param responseCallback      A callback to handle each response received.
     * @throws IOException If an I/O error occurs while sending or receiving the packet.
     */
    public static void broadcastAndReceive(byte[] data, int port, long listeningDurationMillis, Consumer<byte[]> responseCallback) throws IOException {
        InetAddress broadcastAddress = InetAddress.getByName("192.168.1.255"); // 192.168.1.255
        DatagramSocket broadcastSocket = new DatagramSocket();
        broadcastSocket.setBroadcast(true);
        broadcastSocket.setSoTimeout(DEFAULT_TIMEOUT_MILLIS);

        DatagramPacket packet = new DatagramPacket(data, data.length, broadcastAddress, port);
        broadcastSocket.send(packet);
        logger.info("Broadcasted data on port {} - {}", port, DataConversionUtil.bytesToHex(data));

        long endTime = System.currentTimeMillis() + listeningDurationMillis;
        byte[] receiveBuffer = new byte[DEFAULT_RECEIVE_BUFFER_SIZE];

        while (System.currentTimeMillis() < endTime) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                broadcastSocket.receive(receivePacket);

                if (!receivePacket.getAddress().isLoopbackAddress()) {
                    byte[] packetData = new byte[receivePacket.getLength()];
                    System.arraycopy(receivePacket.getData(), 0, packetData, 0, receivePacket.getLength());
                    responseCallback.accept(packetData);
                    logger.debug("Received broadcast response from {}:{} - {}", receivePacket.getAddress(), receivePacket.getPort(), DataConversionUtil.bytesToHex(packetData));
                }
            } catch (SocketTimeoutException e) {
                // logger.debug("Socket timeout reached while waiting for broadcast responses.");
            }
        }

        broadcastSocket.close();
        logger.info("Broadcast socket closed.");
    }
}