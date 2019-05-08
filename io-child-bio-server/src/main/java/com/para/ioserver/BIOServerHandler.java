package com.para.ioserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BIOServerHandler implements Runnable{

    private Socket socket;
    public BIOServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(this.socket.getOutputStream(), true);

            String body = null;
            while (true) {
                body =  reader.readLine();
                if(null == body) {
                    break;
                }
                System.out.println("server服务端接收参数 : " + body);
                writer.println(body + " = 暂时无法提供计算" );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
