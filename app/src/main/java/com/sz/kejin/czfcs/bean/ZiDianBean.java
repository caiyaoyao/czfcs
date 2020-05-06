package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class ZiDianBean implements Serializable {

    /**
     * text : 不限
     * val :
     */

    private String text;
    private String val;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
