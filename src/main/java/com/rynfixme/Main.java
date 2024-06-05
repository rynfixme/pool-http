package com.rynfixme;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.PersistedObject;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main implements BurpExtension {

    static final String DO_CAPTURE = "POOL_HTTP_SKIP_RECORD";
    static final String STORE_DIR_NAME = "POOL_HTTP_STORE_DIR_NAME";
    static final String STORE_FILE_NAME = "POOL_HTTP_STORE_FILE_NAME";

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("pool HTTP");
        PersistedObject persist = api.persistence().extensionData();
        persist.setBoolean(DO_CAPTURE, true);

        PoolHttpFileUtil fileUtil = new PoolHttpFileUtil(persist);
        PoolHttpCapture capture = new PoolHttpCapture(persist, api.logging());

        api.http().registerHttpHandler(new PoolHttpHandler(capture, api.logging()));
        api.userInterface().registerSuiteTab("Pool HTTP", new PoolHttpGUI(persist, fileUtil, api.logging()));
    }
}