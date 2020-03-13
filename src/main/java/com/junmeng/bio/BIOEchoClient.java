package com.junmeng.bio;

import java.io.*;
import java.net.Socket;

/**
 * @author james
 * @date 2020/3/12
 */
public class BIOEchoClient {

    public static void main(String[] args) throws IOException {

        int port = 8088;

        String serverIp = "127.0.0.1";

        Socket socket = null;

        BufferedReader reader = null;

        BufferedWriter writer;

        try {
            //创建socket对象，并连接远程主机
            socket = new Socket(serverIp, port);

            //建立连接后，从socket得到输入流与输出流，可以使用这两个流与服务器之间相互发送数据
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //向服务端发送数据
            writer.write("Hello, hahahahahah block io");
            writer.flush();
            System.out.println("向服务端发送数据完成");
            //获取服务端返回的数据
//            String echo = reader.readLine();
//            System.out.println("echo : " + echo);



        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
