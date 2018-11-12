package com.oio.factory;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {

    private static int index = 0;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, "Thread" + index++);
        return thread;
    }
}
