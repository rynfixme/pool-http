package com.rynfixme;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.persistence.PersistedObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolHttpHandler implements HttpHandler {
    private final Logging logging;
    private final BlockingQueue<String> queue;
    private final ExecutorService poolSender;
    private final ExecutorService poolReceiver;
    private final PersistedObject persist;


    public PoolHttpHandler(MontoyaApi api, PersistedObject persist, BlockingQueue<String> queue) {
        this.logging = api.logging();
        this.queue = queue;
        this.poolSender = Executors.newFixedThreadPool(10);
        this.poolReceiver = Executors.newFixedThreadPool(10);
        this.persist = persist;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        Annotations annotations = requestToBeSent.annotations();
        return RequestToBeSentAction.continueWith(requestToBeSent, annotations);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        try {
            // check
            boolean doCapture = this.persist.getBoolean(Main.DO_CAPTURE);
            if (!doCapture) {
                return ResponseReceivedAction.continueWith(responseReceived);
            }

            // record
            String req = responseReceived.initiatingRequest().toString();
            String res = responseReceived.toString();
            poolSender.submit(new PoolHttpSenderTask(req, res, this.queue, this.logging));
            Thread.sleep(200);

            String fileName = this.persist.getString(Main.STORE_FILE_NAME);
            poolReceiver.submit(new PoolHttpReceiverTask(this.queue, fileName, this.logging));
        } catch (Exception e) {
            this.logging.logToError(e);
        }

        return ResponseReceivedAction.continueWith(responseReceived);
    }
}

