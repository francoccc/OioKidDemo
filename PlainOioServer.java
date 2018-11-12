package com.oio;

import com.oio.factory.MyThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlainOioServer {

    private final static Logger LOG = LoggerFactory.getLogger(PlainOioServer.class);

    public void start(int port) throws Exception {
        LOG.info("Server start at {}", port);
        final ServerSocket serverSocket = new ServerSocket(port);
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1024);
        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS, queue, myThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        for(;;) {
            LOG.info("Server waiting.");
            Socket clientSocket = serverSocket.accept();
            LOG.info("Server create a new {}", clientSocket);
            executor.submit(() -> {
                try {
                    OutputStream out = clientSocket.getOutputStream();
                    out.write("Server Echo".getBytes(Charset.forName("UTF-8")));
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
