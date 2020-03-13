package com.junmeng.nio.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author james
 * @date 2020/3/12
 */
public class Main {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("D:\\test.txt");
        //从输入流中获取源文件src.txt的通道
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\test-target.txt");
        //从输出流中获取目标文件target.txt的通道
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //使用transferTo API将文件src.txt内容写入target.txt
        fileInputStreamChannel.transferTo(0, fileInputStreamChannel.size(), fileOutputStreamChannel);

        //关闭文件流及通道
        fileInputStream.close();
        fileInputStreamChannel.close();

        fileOutputStream.close();
        fileOutputStreamChannel.close();

    }

}
