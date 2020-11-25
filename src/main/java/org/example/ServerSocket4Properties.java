package org.example;


import com.google.common.base.Strings;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Author qiu
 * @Date 2020/11/25 23:18
 * <p>
 * 配置版的服务端socket
 */
public class ServerSocket4Properties {

    /**
     * 接收缓冲区大小
     */
    private static final int RECEIVE_BUFFER = 10;
    /**
     * 服务端等待客户端连接超时时间，超出时间则会报错
     * 为0表示一直等待
     */
    private static final int SO_TIMEOUT = 0;
    /**
     * 服务端关闭，端口号不会立即释放，为了能够接收到网络中延时的数据包，OS也会延时关闭端口号，如果此时有其他的服务使用了该端口号，则会提示报错
     * 如果该配置为true，则允许其他服务使用该端口号
     */
    private static final boolean REUSE_ADDR = false;
    /**
     * OS中的FIFO等待队列默认大小为50
     */
    private static final int BACK_LOG = 2;

    private static final boolean CLI_KEEPALIVE = false;
    private static final boolean CLI_OOB = false;
    private static final int CLI_REC_BUF = 20;
    private static final boolean CLI_REUSE_ADDR = false;
    private static final int CLI_SEND_BUF = 20;
    private static final boolean CLI_LINGER = true;
    private static final int CLI_LINGER_N = 0;
    private static final int CLI_TIMEOUT = 0;
    private static final boolean CLI_NO_DELAY = false;

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("socket-server-%d").build();
    private static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8090);
            serverSocket.setSoTimeout(SO_TIMEOUT);
            serverSocket.setReceiveBufferSize(RECEIVE_BUFFER);
            serverSocket.setReuseAddress(REUSE_ADDR);
            System.out.println("server socket start, port: 8090");

            while (true) {
                // 从队列中获取等待的客户端的连接
                Socket client = serverSocket.accept();
                client.setKeepAlive(CLI_KEEPALIVE);
                client.setOOBInline(CLI_OOB);
                client.setReceiveBufferSize(CLI_REC_BUF);
                client.setReuseAddress(CLI_REUSE_ADDR);
                client.setSendBufferSize(CLI_SEND_BUF);
                client.setSoLinger(CLI_LINGER, CLI_LINGER_N);
                client.setSoTimeout(CLI_TIMEOUT);
                client.setTcpNoDelay(CLI_NO_DELAY);

                THREAD_POOL_EXECUTOR.execute(() -> {
                    try {
                        InputStream inputStream = client.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        while (true) {
                            String data = reader.readLine();
                            if(Strings.isNullOrEmpty(data)) {
                                client.close();
                                break;
                            }
                            System.out.println("client read some data is :" + data.length() + " val :" + data);
                        }
                    } catch (IOException e) {
                        System.out.println("IOException e:" + e.toString());
                    }
                });
            }
        } catch (IOException e) {
            System.out.println("IOException e:" + e.toString());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("server socket close failed");
                }
            }
        }
    }
}
