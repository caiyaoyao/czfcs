package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class YzmBeans implements Serializable {

    /**
     * code : 200
     * msg : success
     * msgId : 158796463412294219008
     * contNum : 1
     */

    private int code;
    private String msg;
    private String msgId;
    private int contNum;

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

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getContNum() {
        return contNum;
    }

    public void setContNum(int contNum) {
        this.contNum = contNum;
    }
}
