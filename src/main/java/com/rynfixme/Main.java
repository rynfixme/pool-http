package com.rynfixme;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.PersistedObject;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main implements BurpExtension {

    static final String DO_CAPTURE = "POOL_HTTP_SKIP_RECORD";
    static final String STORE_FILE_NAME = "POOL_HTTP_STORE_FILE_NAME";

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("pool HTTP");
        BlockingDeque<String> queue = new LinkedBlockingDeque<String>();

        PersistedObject persist = api.persistence().extensionData();
        persist.setBoolean(DO_CAPTURE, true);
        PoolHttpFileUtil fileUtil = new PoolHttpFileUtil();
        String fileName = fileUtil.createFileName();
        persist.setString(STORE_FILE_NAME, fileName);

        api.http().registerHttpHandler(new PoolHttpHandler(api, persist, queue));
        api.userInterface().registerSuiteTab("Pool HTTP", new PoolHttpGUI(api.logging(), persist));
    }
}