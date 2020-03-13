package com.junmeng.nio.socketchannel;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author james
 * @date 2020/3/12
 */
public class EchoServer {

    //执行服务端业务逻辑线程池
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try {
            //新建服务端serversocketchannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //如果serversocketchannel创建不成功
            if (!serverSocketChannel.isOpen()) {
                System.out.println("server socket channel cannot be opened");
            }
            //设置为阻塞模式
            serverSocketChannel.configureBlocking(true);

            //设置网络传输参数
            serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            //绑定服务端channel端口与本地ip
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8055));

            while (true) {
                try {
                    //等待连接客户端的请求
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //提交给线程池处理业务逻辑
                    executorService.submit(new EchoHandler(socketChannel));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
