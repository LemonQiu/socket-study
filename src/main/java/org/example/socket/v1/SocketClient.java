package org.example.socket.v1;

import java.io.*;
import java.net.Socket;

/**
 * @Author qiu
 * @Date 2020/11/26 1:013
 *
 * socket客户端
 */
public class SocketClient {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("192.168.56.66",9090);

            // 设置发送缓冲区大小
            client.setSendBufferSize(20);
            // 设置接收缓冲区大小
            client.setReceiveBufferSize(20);
            client.setTcpNoDelay(false);
            // 获取输出流
            OutputStream out = client.getOutputStream();

            // 需要手动输入，则会接收到输入的数据
            InputStream in = System.in;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            while(true){
                String line = reader.readLine();
                if(line != null ){
                    byte[] bb = line.getBytes();
                    for (byte b : bb) {
                        out.write(b);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
