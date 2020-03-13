package com.junmeng.nio.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author james
 * @date 2020/3/12
 */
public class Main {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    //connection accepted 事件
                } else if (selectionKey.isConnectable()) {
                    //connection established 事件
                } else if (selectionKey.isReadable()) {
                    //channel 准备好读数据事件
                } else if (selectionKey.isWritable()) {
                    //channel 准备好些数据事件
                }
                iterator.remove();
            }
        }

    }

}
