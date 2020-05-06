package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class VersionBean implements Serializable {


    /**
     * bbh : 0
     * url : http://202.102.43.44:8001/kskfq.apk
     */

    private String gxxx;

    public String getGxxx() {
        return gxxx;
    }

    public void setGxxx(String gxxx) {
        this.gxxx = gxxx;
    }

    private String bbh;
    private String url;

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
