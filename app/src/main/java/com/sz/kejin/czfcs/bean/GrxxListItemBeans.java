package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class GrxxListItemBeans implements Serializable {
    private String text;
    private int imgId;


    public GrxxListItemBeans() {
    }

    public GrxxListItemBeans(String text, int imgId) {
        this.text = text;
        this.imgId = imgId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
