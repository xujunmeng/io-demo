package com.junmeng.nio.socketchannel;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author james
 * @date 2020/3/12
 */
public class EchoClient {

    public static void main(String[] args) {

        String msg = "asdgdfasdfasdf艾尔但是如果是大法官";
        ByteBuffer clientBuffer = ByteBuffer.wrap(msg.getBytes());

        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder charsetDecoder = charset.newDecoder();

        try {
            //创建客户端socketchannel
            SocketChannel socketChannel = SocketChannel.open();
            //如果客户端socketchannel创建不成功
            if (!socketChannel.isOpen()) {
                throw new RuntimeException("server socket channel cannot be opened");
            }

            //设置为阻塞模式i
            socketChannel.configureBlocking(true);
            //设置网络传输参数
            socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 128*1024);
            socketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 128*1024);
            socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
            socketChannel.setOption(StandardSocketOptions.SO_LINGER, 5);

            //连接服务器
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8055));

            //如果连接服务端不成功
            if (!socketChannel.isConnected()) {
                throw new RuntimeException("server socket connection cannot be established!");
            }

            //向服务端发送数据
            socketChannel.write(clientBuffer);

            //创建接收服务端返回的数据bytebuffer
            ByteBuffer serverBuffer = ByteBuffer.allocate(1024);
            while (socketChannel.read(serverBuffer) != -1) {
                serverBuffer.flip();
                charBuffer = charsetDecoder.decode(serverBuffer);
                System.out.println(charBuffer.toString());

                if (serverBuffer.hasRemaining()) {
                    serverBuffer.compact();
                } else {
                    serverBuffer.clear();
                }

            }

            socketChannel.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
