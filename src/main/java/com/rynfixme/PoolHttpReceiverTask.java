package com.rynfixme;

import burp.api.montoya.logging.Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class PoolHttpReceiverTask implements Runnable {
    final private BlockingQueue<String> queue;
    final private String  fileName;
    final private Logging logging;

    public PoolHttpReceiverTask(BlockingQueue<String> queue, String fileName, Logging logging) {
        this.queue = queue;
        this.fileName = fileName;
        this.logging = logging;
    }

    @Override
    public void run() {
        synchronized (this.fileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.fileName, true))) {
                String line;

                while ((line = queue.poll()) != null) {
                    Thread.sleep(300);
                    writer.append(line);
                    writer.newLine();
                }
            } catch (IOException | InterruptedException e) {
                logging.logToError(e);
            }
        }
    }
}
