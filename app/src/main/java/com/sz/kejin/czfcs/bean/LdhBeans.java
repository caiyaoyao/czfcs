package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class LdhBeans implements Serializable {

    /**
     * csqLoginid : 2022515515033937
     * xqbh : 20225155529530976
     * ldh : 1
     */

    private String csqLoginid;
    private String xqbh;
    private int ldh;

    public String getCsqLoginid() {
        return csqLoginid;
    }

    public void setCsqLoginid(String csqLoginid) {
        this.csqLoginid = csqLoginid;
    }

    public String getXqbh() {
        return xqbh;
    }

    public void setXqbh(String xqbh) {
        this.xqbh = xqbh;
    }

    public int getLdh() {
        return ldh;
    }

    public void setLdh(int ldh) {
        this.ldh = ldh;
    }
}
