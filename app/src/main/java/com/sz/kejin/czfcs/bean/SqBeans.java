package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class SqBeans implements Serializable {

    /**
     * name : 兵希社区
     * csqid : 2031012405548413
     * num : 2E394D2D57EE22A4B63A3A3036FC47F16A6330416B9E821B
     */

    private String name;
    private String csqid;
    private String num;

    public SqBeans() {
    }


    public SqBeans(String name, String csqid, String num) {
        this.name = name;
        this.csqid = csqid;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCsqid() {
        return csqid;
    }

    public void setCsqid(String csqid) {
        this.csqid = csqid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
