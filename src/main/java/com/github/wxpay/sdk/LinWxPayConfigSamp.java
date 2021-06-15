package com.github.wxpay.sdk;

import java.io.InputStream;

public class LinWxPayConfigSamp extends WXPayConfig {

    @Override
    public String getAppID() {
        return "wx8adae5e5de01d28b";
    }

    @Override
    public String getMchID() {
        return "ssss";
    }

    @Override
    public String getKey() {
        return "ssssss";
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
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
