package com.sz.kejin.czfcs.bean;

import java.io.Serializable;
import java.util.List;

public class ImageUploadBean implements Serializable {


    /**
     * code : 1
     * info : [{"ext":".jpg","name":"Screenshot_20200327_095205_com.sz.kejin.kskfq.jpg","path":"/upload/2020/3/30/114925/Screenshot_20200327_095205_com.sz.kejin.kskfq.jpg","size":"133004"}]
     * msg : 上传成功
     */

    private int code;
    private String msg;
    private List<InfoBean> info;

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

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * ext : .jpg
         * name : Screenshot_20200327_095205_com.sz.kejin.kskfq.jpg
         * path : /upload/2020/3/30/114925/Screenshot_20200327_095205_com.sz.kejin.kskfq.jpg
         * size : 133004
         */

        private String ext;
        private String name;
        private String path;
        private String size;

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
