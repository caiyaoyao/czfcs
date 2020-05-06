package com.sz.kejin.czfcs.bean;

public class BasicTableItemBean {
    private int imageRes;
    private String title;
    private String num;

    public BasicTableItemBean(int color, String title) {
        this.imageRes = color;
        this.title = title;
    }

    public BasicTableItemBean(String title) {
        this.title = title;
    }

    public BasicTableItemBean() {
    }

    public int getimageRes() {
        return imageRes;
    }

    public void setimageRes(int color) {
        this.imageRes = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
