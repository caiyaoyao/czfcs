package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class YcInfoBeans implements Serializable {

    private String sdm;
    private String smc;

    private String djsmc;
    private String djsdm;


    private String qxdm;
    private String qxmc;

    public String getQxdm() {
        return qxdm;
    }

    public void setQxdm(String qxdm) {
        this.qxdm = qxdm;
    }

    public String getQxmc() {
        return qxmc;
    }

    public void setQxmc(String qxmc) {
        this.qxmc = qxmc;
    }

    public String getDjsmc() {
        return djsmc;
    }

    public void setDjsmc(String djsmc) {
        this.djsmc = djsmc;
    }

    public String getDjsdm() {
        return djsdm;
    }

    public void setDjsdm(String djsdm) {
        this.djsdm = djsdm;
    }

    public String getSdm() {
        return sdm;
    }

    public void setSdm(String sdm) {
        this.sdm = sdm;
    }

    public String getSmc() {
        return smc;
    }

    public void setSmc(String smc) {
        this.smc = smc;
    }
}
