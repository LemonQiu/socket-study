package org.example.socket.v1;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author qiu
 * @Date 2020/12/3 1:45
 */
public class SocketChannelClient {

    public static void main(String[] args) {
        try {
            InetSocketAddress serverAdd = new InetSocketAddress("192.168.56.66", 8090);

            // 设置发送缓冲区大小
            SocketChannel clientChannel = SocketChannel.open();
            clientChannel.bind(new InetSocketAddress("192.168.56.1", 9090));
            clientChannel.connect(serverAdd);

            // 需要手动输入，则会接收到输入的数据
            if (clientChannel.isConnected()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                while (true) {
                    byteBuffer.clear();

                    InputStream in = System.in;
                    byte[] b = new byte[1024];
                    while(in.read(b) != -1){
                        byteBuffer.put(b, byteBuffer.position(), byteBuffer.limit());
                    }
                    byteBuffer.flip();
                    clientChannel.write(byteBuffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
