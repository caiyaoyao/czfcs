package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class UpdateAppBean implements Serializable {
    public static final String FORCE_UPDATE = "1";
    private String url;
    private String version;
    private String content;
    private String force_update;

    public UpdateAppBean(String url, String version, String content, String force_update) {
        this.url = url;
        this.version = version;
        this.content = content;
        this.force_update = force_update;
    }

    public String getForce_update() {
        return force_update;
    }

    public void setForce_update(String force_update) {
        this.force_update = force_update;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

