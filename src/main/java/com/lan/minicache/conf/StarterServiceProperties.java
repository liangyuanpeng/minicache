package com.lan.minicache.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("lan.minicache")
public class StarterServiceProperties {
    private String dbpack;

    public String getDbpack() {
        return dbpack;
    }

    public void setDbpack(String dbpack) {
        this.dbpack = dbpack;
    }
}