package com.rynfixme;

import burp.api.montoya.logging.Logging;

import java.util.concurrent.BlockingQueue;

public class PoolHttpSenderTask implements Runnable {
    private final String message;
    private final BlockingQueue<String> queue;
    private final Logging logging;

    public PoolHttpSenderTask(String message, BlockingQueue<String> queue, Logging logging) {
        this.message = message;
        this.queue = queue;
        this.logging = logging;
    }

    @Override
    public void run() {
        try {
            this.queue.put(this.message);
        } catch (InterruptedException e) {
            this.logging.logToError(e);
        }
    }
}
