package com.para.ioclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    private final static int PORT = 8888;
    private final static int BUFFER_SIZE = 1024;
    private final static String IP_ADDRESS = "127.0.0.1";

    public static void clientReq() {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(IP_ADDRESS, PORT);
        SocketChannel socketChannel = null;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            socketChannel = SocketChannel.open();
            socketChannel.connect(inetSocketAddress);
            while (true) {
                byte[] bytes = new byte[BUFFER_SIZE];
                System.out.println("start input: ");
                System.in.read(bytes);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
