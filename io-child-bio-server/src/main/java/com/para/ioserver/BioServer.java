package com.para.ioserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {
    private static final Integer PORT = 8888;

    public static void bioServerResp() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        ThreadPoolExecutor executor = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("BIO Server 服务器启动......");

            executor = new ThreadPoolExecutor(10, 100, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
            while (true) {
                socket = serverSocket.accept();
                BIOServerHandler bioServerHandler = new BIOServerHandler(socket);
                executor.execute(bioServerHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != socket) {
                    socket.close();
                    socket = null;
                }

                if(null != serverSocket) {
                    serverSocket.close();
                    serverSocket = null;
                    System.out.println("BIO Server 服务器关闭了！！！！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
