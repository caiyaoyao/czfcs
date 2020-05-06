package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class TestBean implements Serializable {


    /**
     * code : 0
     * msg : 1334
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
