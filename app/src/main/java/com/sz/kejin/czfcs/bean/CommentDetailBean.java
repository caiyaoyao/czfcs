package com.sz.kejin.czfcs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by moos on 2018/4/20.
 */

public class CommentDetailBean implements Serializable {
    private String plid;
    private String plr;
    private String tx;
    private String plnr;
    private String plrid;
    private int replyTotal;
    private String plsj;
    private List<ReplyDetailBean> Ejpl;

    public String getPlid() {
        return plid;
    }

    public void setPlid(String plid) {
        this.plid = plid;
    }

    public String getPlr() {
        return plr;
    }

    public void setPlr(String plr) {
        this.plr = plr;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getPlnr() {
        return plnr;
    }

    public void setPlnr(String plnr) {
        this.plnr = plnr;
    }

    public String getPlrid() {
        return plrid;
    }

    public void setPlrid(String plrid) {
        this.plrid = plrid;
    }

    public int getReplyTotal() {
        return replyTotal;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }

    public String getPlsj() {
        return plsj;
    }

    public void setPlsj(String plsj) {
        this.plsj = plsj;
    }

    public List<ReplyDetailBean> getEjpl() {
        return Ejpl;
    }

    public void setEjpl(List<ReplyDetailBean> ejpl) {
        Ejpl = ejpl;
    }
}
