package com.rynfixme;

import burp.api.montoya.core.Annotations;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.logging.Logging;


public class PoolHttpHandler implements HttpHandler {
    private final Logging logging;
    private final PoolHttpCapture capture;

    public PoolHttpHandler(PoolHttpCapture capture, Logging logging) {
        this.logging = logging;
        this.capture = capture;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        Annotations annotations = requestToBeSent.annotations();
        return RequestToBeSentAction.continueWith(requestToBeSent, annotations);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        try {
            String req = responseReceived.initiatingRequest().toString();
            String res = responseReceived.toString();
            this.capture.capture(req, res);
        } catch (Exception e) {
            this.logging.logToError(e);
        }
        return ResponseReceivedAction.continueWith(responseReceived);
    }
}

