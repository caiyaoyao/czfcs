package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class LoginBean implements Serializable {

    /**
     * xm : 130****3235
     * xb : 男
     * sfzh : 411524198910140515
     * lxdh : 13003203235
     * mz : null
     * hdj_s : 河南省
     * hdj_city : 信阳市
     * hdj_x : 商城县
     * gzdw : null
     * zpPath : null
     * kind : 游客
     * shzt : 2
     * posttime : 2020-04-27
     */

    private String xm;
    private String xb;
    private String sfzh;
    private String lxdh;
    private String mz;
    private String hdj_s;
    private String hdj_city;
    private String hdj_x;
    private String gzdw;
    private String zpPath;
    private String kind;
    private int shzt;
    private String posttime;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getHdj_s() {
        return hdj_s;
    }

    public void setHdj_s(String hdj_s) {
        this.hdj_s = hdj_s;
    }

    public String getHdj_city() {
        return hdj_city;
    }

    public void setHdj_city(String hdj_city) {
        this.hdj_city = hdj_city;
    }

    public String getHdj_x() {
        return hdj_x;
    }

    public void setHdj_x(String hdj_x) {
        this.hdj_x = hdj_x;
    }

    public String getGzdw() {
        return gzdw;
    }

    public void setGzdw(String gzdw) {
        this.gzdw = gzdw;
    }

    public String getZpPath() {
        return zpPath;
    }

    public void setZpPath(String zpPath) {
        this.zpPath = zpPath;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getShzt() {
        return shzt;
    }

    public void setShzt(int shzt) {
        this.shzt = shzt;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }
}
