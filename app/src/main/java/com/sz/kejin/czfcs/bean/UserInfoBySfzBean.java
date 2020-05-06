package com.sz.kejin.czfcs.bean;

import java.io.Serializable;

public class UserInfoBySfzBean implements Serializable {


    /**
     * province : 河南省
     * birthday : 1989-10-14
     * city : 信阳市
     * county : 商城县
     * sex : 男
     */

    private String province;
    private String birthday;
    private String city;
    private String county;
    private String sex;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
