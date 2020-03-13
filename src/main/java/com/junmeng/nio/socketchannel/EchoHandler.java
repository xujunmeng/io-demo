package com.junmeng.nio.socketchannel;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author james
 * @date 2020/3/12
 */
public class EchoHandler implements Runnable {

    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public EchoHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
            try {
                //读取客户端传输的数据，并原样写入返回给客户端
                while (socketChannel.read(byteBuffer) != -1) {

                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);

                    if (byteBuffer.hasRemaining()) {
                        byteBuffer.compact();
                    } else {
                        byteBuffer.clear();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
