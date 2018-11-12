package com.oio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientDemo {

    private final static Logger LOG = LoggerFactory.getLogger(ClientDemo.class);

    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        for(int i = 1; i <= 5; i++) {
            new Thread(() -> {
                try {
                    Socket socket = new Socket(address, 8080);
                    LOG.info("Client create a new {}", socket);
                    InputStream in = socket.getInputStream();
                    byte[] bytes = new byte[1 << 10];
                    int len = in.read(bytes);
                    System.out.println("Client get message:" + new String(bytes, 0, len));
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, "Thread-"+ i).start();
        }
    }
}
