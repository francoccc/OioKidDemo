package oio;

import oio.factory.MyThreadFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlainOioServer {

    public void start(int port) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(port);
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1024);
        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS, queue, myThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        for(;;) {
            Socket clientSocket = serverSocket.accept();
            executor.submit(() -> {
                try {
                    OutputStream out = clientSocket.getOutputStream();
                    out.write("Server:Echo".getBytes(Charset.forName("UTF-8")));
                    out.flush();
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
