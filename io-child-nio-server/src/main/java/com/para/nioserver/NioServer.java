package com.para.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioServer implements Runnable {

    private final int BUFFER_SIZE = 1024;
    private final int PORT = 8888;
    private Selector selector;
    private ByteBuffer readbuffer = ByteBuffer.allocate(BUFFER_SIZE);

    public NioServer() {
        startServer();
    }

    private void startServer() {
        try {
            //1.开启多路复用器
            selector = Selector.open();
            //2.打开服务器通道
            ServerSocketChannel channel = ServerSocketChannel.open();
            //3.设置服务器通道为非阻塞模式， true为阻塞， false为非阻塞
            channel.configureBlocking(false);
            //4.绑定端口
            channel.socket().bind(new InetSocketAddress(PORT));
            //5.把通道注册到多路复用器上，并监听阻塞时间
            /**
             * SelectionKey.OP_READ         :表示关注读数据就绪事件
             * SelectionKey.OP_WRITE        :表示关注写数据就绪事件
             * SelectionKey.OP_CONNECT      :表示关注socket channel的连接完成事件
             * SelectionKey.OP_ACCEPT       :表示关注server - socket channel的accept事件
             */
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server start >>>>>> port : " + PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //需要一个线程负责Selector的轮询
    @Override
    public void run() {
        while (true) {
            try {
                //1.多路复用器监听阻塞
                selector.select();
                //2.多路复用器已经选择的结果集
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                //3.不停的轮询
                while (selectionKeyIterator.hasNext()) {
                    //4.获取一个选中的key
                    SelectionKey key = selectionKeyIterator.next();
                    //5.获取后便将其从容器中删除
                    selectionKeyIterator.remove();
                    //6.只获取有效的key
                    if (!key.isValid()) {
                       continue;
                    }
                    //7.阻塞状态处理
                    if(key.isAcceptable()) {
                        accpet(key);
                    }
                    if(key.isReadable()) {
                        read(key);
                    }
                    if(key.isWritable()) {
                        write(key);
                    }
                }
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accpet(SelectionKey selectionKey) {
        try {
            //获取通道服务
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            //执行阻塞方法
            SocketChannel socketChannel = serverSocketChannel.accept();
            //3.设置服务器通道为非阻塞模式， true为阻塞， false为非阻塞
            socketChannel.configureBlocking(false);
            //4.把通道注册到多路复用器上，并设置读取标识
            socketChannel.register(selector, selectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey selectionKey){
        try {
            //1. 清空缓冲区数据
            readbuffer.clear();
            //2. 获取在多路复用器上注册的通道
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            //3. 读取数据,返回
            int count = socketChannel.read(readbuffer);
            //4. 返回内容为-1 表示没有数据
            if (-1 == count) {
                selectionKey.channel().close();
                selectionKey.cancel();
                return;
            }
            //5. 有数据则在读数据前进行复位操作
            readbuffer.flip();
            //6. 根据缓冲区大小创建一个响应大小的byte数组，用来获取值
            byte[] bytes = new byte[readbuffer.remaining()];
            //7. 接收缓冲区数据
            readbuffer.get(bytes);
            //8. 打印获取到的数据
            System.out.println("NIO Server : " + new String(bytes)); //不能用bytes.toString()

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        try {
            socketChannel.write(ByteBuffer.wrap("server write".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
