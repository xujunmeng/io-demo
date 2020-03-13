package com.junmeng.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author james
 * @date 2020/3/12
 */
public class Main2 {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("D:\\test.txt");
        //从输入流中获取源文件src.txt的通道
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\test-target.txt");
        //从输出流中获取目标文件target.txt的通道
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //文件读取内容buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int bytesRead = fileInputStreamChannel.read(byteBuffer);
        //一次性可能读不完，所以需要循环读取
        while (bytesRead != -1) {
            //翻转buffer，为下面的读取做准备
            byteBuffer.flip();

            while (byteBuffer.hasRemaining()) {
                //将读取到的内容写入test-target.txt
                fileOutputStreamChannel.write(byteBuffer);
            }

            //复位buffer，以便再次复用buffer
            byteBuffer.clear();

            bytesRead = fileInputStreamChannel.read(byteBuffer);
        }

        //关闭文件流及通道
        fileInputStream.close();
        fileInputStreamChannel.close();
        fileOutputStream.close();
        fileOutputStreamChannel.close();
    }

}
