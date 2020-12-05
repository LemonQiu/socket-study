package org.example.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;

/**
 * @Author qiu
 * @Date 2020/12/6 2:00
 * NIO Server Socket
 */
public class NIOServerSocket {

    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("localhost", 9090));
            // 设置I/O为非阻塞
            System.out.println("server socket start, port: " + 9090);

            while (true) {
                // NIO中，如果等待队列中有连接，则返回对应的连接对象，否则返回null  对于Linux中实际返回的是FD或者-1
                SocketChannel client = serverSocketChannel.accept();
                if(client == null) {
//                    System.out.println("accept client is null...");
                } else {
                    client.configureBlocking(false);
                    System.out.println("accept client socket, client:" + client.getRemoteAddress());
                    clients.add(client);
                }

                // 由于是非阻塞的IO，所以我们可以在主线程里直接处理IO，但是因为
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                for (SocketChannel socketChannel : clients) {
                    byteBuffer.clear();
                    // 此时因为非阻塞，返回>0, 0, -1
                    int readSize = socketChannel.read(byteBuffer);
                    if(readSize > 0) {
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.limit()];
                        byteBuffer.get(bytes, byteBuffer.position(), byteBuffer.limit());
                        System.out.println("client send msg is: " + new String(bytes, Charset.defaultCharset()));
//                            socketChannel.write(byteBuffer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
