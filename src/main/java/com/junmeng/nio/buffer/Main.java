package com.junmeng.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author james
 * @date 2020/3/12
 */
public class Main {

    public static void main(String[] args) {

        Buffer buffer = ByteBuffer.allocate(10);
        System.out.println("Capacity : " + buffer.capacity());

        System.out.println("limit : " + buffer.limit());

        System.out.println("position : " + buffer.position());

        //将字符串内容写入buffer
        String content = "你好.alsdjfaklsdfjaslkdf";
        CharBuffer charBuffer = CharBuffer.allocate(50);
        for (int i = 0; i < content.length(); i++) {
            charBuffer.put(content.charAt(i));
        }
        //反转buffer,准备读取buffer内容
        charBuffer.flip();
        //读取buffer中的数据
        while (charBuffer.hasRemaining()) {
            System.out.print(charBuffer.get());
        }
    }

}
