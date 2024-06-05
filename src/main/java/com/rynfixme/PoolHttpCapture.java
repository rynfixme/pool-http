package com.rynfixme;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.persistence.PersistedObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.*;

public class PoolHttpCapture {
    private final ExecutorService threadPoolSender;
    private final ExecutorService threadPoolReceiver;
    private final PersistedObject persist;
    private final BlockingQueue<String> queue;
    private final Logging logging;
    private final PoolHttpFileUtil fileUtil;

    public PoolHttpCapture(PersistedObject persist, Logging logging) {
        this.persist = persist;
        this.threadPoolSender = Executors.newFixedThreadPool(10);
        this.threadPoolReceiver = Executors.newFixedThreadPool(10);
        this.queue = new LinkedBlockingQueue<>();
        this.logging = logging;
        this.fileUtil = new PoolHttpFileUtil(persist);
    }

    private boolean doCapture() {
        boolean doCapture = this.persist.getBoolean(Main.DO_CAPTURE);
        return doCapture;
    }

    private String encode(String raw) {
        String encoded1 = URLEncoder.encode(raw, StandardCharsets.UTF_8);
        String encoded2 = Base64.getEncoder().encodeToString(encoded1.getBytes());
        return encoded2;
    }

    private String createMessage(String request, String response) {
        UUID uuid = UUID.randomUUID();
        long generated = System.currentTimeMillis();
        String req = this.encode(request);
        String res = this.encode(response);

        String[] message = {
                uuid.toString(),
                Long.toString(generated),
                req,
                res,
        };

        return String.join(",", message);
    }

    public void capture(String request, String response) {
        if (!this.doCapture()) {
            return;
        }

        try {
            // Sender
            String message = this.createMessage(request, response);
            this.threadPoolSender.submit(new PoolHttpSenderTask(message, this.queue, this.logging));

            Thread.sleep(200);

            // Receiver
            String fileName = this.fileUtil.getFullFilePath();
            this.threadPoolReceiver.submit(new PoolHttpReceiverTask(this.queue, fileName, this.logging));
        } catch (InterruptedException e) {
            this.logging.logToError(e);
        }
    }
}
