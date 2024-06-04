package com.rynfixme;

import burp.api.montoya.logging.Logging;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class PoolHttpSenderTask implements Runnable {
    private final String request;
    private final String response;
    private final BlockingQueue<String> queue;
    private final Logging logging;

    public PoolHttpSenderTask(String request, String response, BlockingQueue<String> queue, Logging logging) {
        this.request = request;
        this.response = response;
        this.queue = queue;
        this.logging = logging;
    }

    @Override
    public void run() {
        try {
            String message = createMessage();
            this.queue.put(message);
        } catch (InterruptedException e) {
            this.logging.logToError(e);
        }
    }

    private String createMessage() {
        UUID uuid = UUID.randomUUID();
        long generated = System.currentTimeMillis();
        String req = Base64
                .getEncoder()
                .encodeToString(this.request.getBytes());

        String res =  Base64
                .getEncoder()
                .encodeToString(this.response.getBytes());

        String[] messages = {
                uuid.toString(),
                Long.toString(generated),
                req,
                res,
        };

        return String.join(",", messages);
    }
}
