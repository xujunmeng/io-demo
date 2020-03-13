package com.junmeng.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author james
 * @date 2020/3/12
 */
public class BIOEchoServerHandler implements Runnable {

    private Socket socket;

    public BIOEchoServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
           //通过socket对象的getInputStream()与getOutputStream()方法获得与客户端通信的输入流与输出流
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                //获取客户端的数据
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                System.out.println("BIOEchoServerHandler : " + line);

                //将客户端获取到的数据再写入
                writer.write(line + " 433434 answer");
                writer.flush();

            }
        } catch (Exception e){
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }
}
