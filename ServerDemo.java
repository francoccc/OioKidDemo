package com.oio;

/**
 * @author Franco
 */
public class ServerDemo {

    public static void main(String[] args) throws Exception {
        PlainOioServer server = new PlainOioServer();
        server.start(8080);
    }
}
