package com.github.wxpay.sdk;

import java.io.InputStream;

public class LinWxPayConfigSamp extends WXPayConfig {
    @Override
    String getAppID() {
        return "wx8adae5e5de01d28b";
    }

    @Override
    String getMchID() {
        return "";
    }

    @Override
    String getKey() {
        return "";
    }


    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
