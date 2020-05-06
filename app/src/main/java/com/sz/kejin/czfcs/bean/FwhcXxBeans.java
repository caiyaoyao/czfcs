package com.sz.kejin.czfcs.bean;

import java.io.Serializable;
import java.util.List;

public class FwhcXxBeans implements Serializable {

    private List<FjxxBean> fjxx;
    private List<ZhxxBean> zhxx;

    public List<FjxxBean> getFjxx() {
        return fjxx;
    }

    public void setFjxx(List<FjxxBean> fjxx) {
        this.fjxx = fjxx;
    }

    public List<ZhxxBean> getZhxx() {
        return zhxx;
    }

    public void setZhxx(List<ZhxxBean> zhxx) {
        this.zhxx = zhxx;
    }

    public static class FjxxBean {
        /**
         * id : 483223
         * loginid : 2022611104024849
         * realName : 孔巷社区
         * bh : CDDF5CC4-9837-4561-8102-B08FC9A2BC0F
         * ldh : 2
         * xqbh : 2022611181674456
         * csqLoginid : 2022611104024849
         * bscLoginid : 20310124539567251
         * sh : 201
         * jzmj : 87
         * hx : 二室一厅
         * jztype : 出租
         * jztype1 : 整租
         * fdxm : 测试
         * fdsfzh : 411524198910140515
         * fdsjh : 13003283235
         * yzxm :
         * yzsfzh : null
         * yzsjh :
         * posttime : 2020-03-16 18:50:00
         * del : false
         * ifgl : false
         * glmj : 0
         * ifck : true
         * ckh : null
         * ckbz : 40
         * syqk : 达标
         * ifjh : true
         * jhLoginid : null
         * jhrealName : null
         * kzrs : 7
         * ifaddfd : 0
         * zpbh_mq : 4BC3230A-57DB-4A10-A920-EC3EF71EA603
         * zpbh_kt : 47AA0B6D-67C7-44BF-9EED-F9F6F2CBF707
         * zpbh_xfss : 655159CF-3ECA-4799-9E0B-5B14A4A2C658
         * zpbh_cf : B4653656-A8E6-49C0-B996-5762B2A731C4
         * ckzpbh_mq : F7ECB43C-4AA1-444A-AEAE-44A71E36A385
         * ckzpbh_pn : B7741AE7-E1AF-4523-B0EF-CB79F673562E
         * ckzpbh_xfss : CD56569D-EB9E-4ED2-8E46-F6A085D7BD89
         * fjzpbh : 61F30687-6012-4A63-9DAD-7D76BCA78FF6
         * ckzpbh : 0F601015-596B-436F-8B9D-46D19BE2759C
         * cksfzr : false
         * fczh : null
         * fczzpbh : F256BD05-8CA7-46F7-8EE3-2B3848271DE1
         * ifhg331 : 0
         * grzpbh : 054ECC9A-9DC1-4678-879A-6077AB37602E
         * rlsj : 2020-04-14 14:43:50
         * xqName : 春江佳苑
         * csqName : 孔巷社区
         * zksl : 1
         * bid : null
         * zj : null
         * czfs : null
         * mj : null
         * cx : null
         * sdxz : null
         * zq : null
         * rzxx : null
         * fjqk : null
         * zflx : null
         * fjzp : null
         */

        private int id;
        private String loginid;
        private String realName;
        private String bh;
        private int ldh;
        private String xqbh;
        private String csqLoginid;
        private String bscLoginid;
        private int sh;
        private String jzmj;
        private String hx;
        private String jztype;
        private String jztype1;
        private String fdxm;
        private String fdsfzh;
        private String fdsjh;
        private String yzxm;
        private String yzsfzh;
        private String yzsjh;
        private String posttime;
        private boolean del;
        private boolean ifgl;
        private int glmj;
        private boolean ifck;
        private String ckh;
        private String ckbz;
        private String syqk;
        private boolean ifjh;
        private String jhLoginid;
        private String jhrealName;
        private int kzrs;
        private int ifaddfd;
        private String zpbh_mq;
        private String zpbh_kt;
        private String zpbh_xfss;
        private String zpbh_cf;
        private String ckzpbh_mq;
        private String ckzpbh_pn;
        private String ckzpbh_xfss;
        private String fjzpbh;
        private String ckzpbh;
        private boolean cksfzr;
        private String fczh;
        private String fczzpbh;
        private int ifhg331;
        private String grzpbh;
        private String rlsj;
        private String xqName;
        private String csqName;
        private int zksl;
        private String bid;
        private String zj;
        private String czfs;
        private String mj;
        private String cx;
        private String sdxz;
        private String zq;
        private String rzxx;
        private String fjqk;
        private String zflx;
        private String fjzp;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLoginid() {
            return loginid;
        }

        public void setLoginid(String loginid) {
            this.loginid = loginid;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getBh() {
            return bh;
        }

        public void setBh(String bh) {
            this.bh = bh;
        }

        public int getLdh() {
            return ldh;
        }

        public void setLdh(int ldh) {
            this.ldh = ldh;
        }

        public String getXqbh() {
            return xqbh;
        }

        public void setXqbh(String xqbh) {
            this.xqbh = xqbh;
        }

        public String getCsqLoginid() {
            return csqLoginid;
        }

        public void setCsqLoginid(String csqLoginid) {
            this.csqLoginid = csqLoginid;
        }

        public String getBscLoginid() {
            return bscLoginid;
        }

        public void setBscLoginid(String bscLoginid) {
            this.bscLoginid = bscLoginid;
        }

        public int getSh() {
            return sh;
        }

        public void setSh(int sh) {
            this.sh = sh;
        }

        public String getJzmj() {
            return jzmj;
        }

        public void setJzmj(String jzmj) {
            this.jzmj = jzmj;
        }

        public String getHx() {
            return hx;
        }

        public void setHx(String hx) {
            this.hx = hx;
        }

        public String getJztype() {
            return jztype;
        }

        public void setJztype(String jztype) {
            this.jztype = jztype;
        }

        public String getJztype1() {
            return jztype1;
        }

        public void setJztype1(String jztype1) {
            this.jztype1 = jztype1;
        }

        public String getFdxm() {
            return fdxm;
        }

        public void setFdxm(String fdxm) {
            this.fdxm = fdxm;
        }

        public String getFdsfzh() {
            return fdsfzh;
        }

        public void setFdsfzh(String fdsfzh) {
            this.fdsfzh = fdsfzh;
        }

        public String getFdsjh() {
            return fdsjh;
        }

        public void setFdsjh(String fdsjh) {
            this.fdsjh = fdsjh;
        }

        public String getYzxm() {
            return yzxm;
        }

        public void setYzxm(String yzxm) {
            this.yzxm = yzxm;
        }

        public String getYzsfzh() {
            return yzsfzh;
        }

        public void setYzsfzh(String yzsfzh) {
            this.yzsfzh = yzsfzh;
        }

        public String getYzsjh() {
            return yzsjh;
        }

        public void setYzsjh(String yzsjh) {
            this.yzsjh = yzsjh;
        }

        public String getPosttime() {
            return posttime;
        }

        public void setPosttime(String posttime) {
            this.posttime = posttime;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public boolean isIfgl() {
            return ifgl;
        }

        public void setIfgl(boolean ifgl) {
            this.ifgl = ifgl;
        }

        public int getGlmj() {
            return glmj;
        }

        public void setGlmj(int glmj) {
            this.glmj = glmj;
        }

        public boolean isIfck() {
            return ifck;
        }

        public void setIfck(boolean ifck) {
            this.ifck = ifck;
        }

        public String getCkh() {
            return ckh;
        }

        public void setCkh(String ckh) {
            this.ckh = ckh;
        }

        public String getCkbz() {
            return ckbz;
        }

        public void setCkbz(String ckbz) {
            this.ckbz = ckbz;
        }

        public String getSyqk() {
            return syqk;
        }

        public void setSyqk(String syqk) {
            this.syqk = syqk;
        }

        public boolean isIfjh() {
            return ifjh;
        }

        public void setIfjh(boolean ifjh) {
            this.ifjh = ifjh;
        }

        public String getJhLoginid() {
            return jhLoginid;
        }

        public void setJhLoginid(String jhLoginid) {
            this.jhLoginid = jhLoginid;
        }

        public String getJhrealName() {
            return jhrealName;
        }

        public void setJhrealName(String jhrealName) {
            this.jhrealName = jhrealName;
        }

        public int getKzrs() {
            return kzrs;
        }

        public void setKzrs(int kzrs) {
            this.kzrs = kzrs;
        }

        public int getIfaddfd() {
            return ifaddfd;
        }

        public void setIfaddfd(int ifaddfd) {
            this.ifaddfd = ifaddfd;
        }

        public String getZpbh_mq() {
            return zpbh_mq;
        }

        public void setZpbh_mq(String zpbh_mq) {
            this.zpbh_mq = zpbh_mq;
        }

        public String getZpbh_kt() {
            return zpbh_kt;
        }

        public void setZpbh_kt(String zpbh_kt) {
            this.zpbh_kt = zpbh_kt;
        }

        public String getZpbh_xfss() {
            return zpbh_xfss;
        }

        public void setZpbh_xfss(String zpbh_xfss) {
            this.zpbh_xfss = zpbh_xfss;
        }

        public String getZpbh_cf() {
            return zpbh_cf;
        }

        public void setZpbh_cf(String zpbh_cf) {
            this.zpbh_cf = zpbh_cf;
        }

        public String getCkzpbh_mq() {
            return ckzpbh_mq;
        }

        public void setCkzpbh_mq(String ckzpbh_mq) {
            this.ckzpbh_mq = ckzpbh_mq;
        }

        public String getCkzpbh_pn() {
            return ckzpbh_pn;
        }

        public void setCkzpbh_pn(String ckzpbh_pn) {
            this.ckzpbh_pn = ckzpbh_pn;
        }

        public String getCkzpbh_xfss() {
            return ckzpbh_xfss;
        }

        public void setCkzpbh_xfss(String ckzpbh_xfss) {
            this.ckzpbh_xfss = ckzpbh_xfss;
        }

        public String getFjzpbh() {
            return fjzpbh;
        }

        public void setFjzpbh(String fjzpbh) {
            this.fjzpbh = fjzpbh;
        }

        public String getCkzpbh() {
            return ckzpbh;
        }

        public void setCkzpbh(String ckzpbh) {
            this.ckzpbh = ckzpbh;
        }

        public boolean isCksfzr() {
            return cksfzr;
        }

        public void setCksfzr(boolean cksfzr) {
            this.cksfzr = cksfzr;
        }

        public String getFczh() {
            return fczh;
        }

        public void setFczh(String fczh) {
            this.fczh = fczh;
        }

        public String getFczzpbh() {
            return fczzpbh;
        }

        public void setFczzpbh(String fczzpbh) {
            this.fczzpbh = fczzpbh;
        }

        public int getIfhg331() {
            return ifhg331;
        }

        public void setIfhg331(int ifhg331) {
            this.ifhg331 = ifhg331;
        }

        public String getGrzpbh() {
            return grzpbh;
        }

        public void setGrzpbh(String grzpbh) {
            this.grzpbh = grzpbh;
        }

        public String getRlsj() {
            return rlsj;
        }

        public void setRlsj(String rlsj) {
            this.rlsj = rlsj;
        }

        public String getXqName() {
            return xqName;
        }

        public void setXqName(String xqName) {
            this.xqName = xqName;
        }

        public String getCsqName() {
            return csqName;
        }

        public void setCsqName(String csqName) {
            this.csqName = csqName;
        }

        public int getZksl() {
            return zksl;
        }

        public void setZksl(int zksl) {
            this.zksl = zksl;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getZj() {
            return zj;
        }

        public void setZj(String zj) {
            this.zj = zj;
        }

        public String getCzfs() {
            return czfs;
        }

        public void setCzfs(String czfs) {
            this.czfs = czfs;
        }

        public String getMj() {
            return mj;
        }

        public void setMj(String mj) {
            this.mj = mj;
        }

        public String getCx() {
            return cx;
        }

        public void setCx(String cx) {
            this.cx = cx;
        }

        public String getSdxz() {
            return sdxz;
        }

        public void setSdxz(String sdxz) {
            this.sdxz = sdxz;
        }

        public String getZq() {
            return zq;
        }

        public void setZq(String zq) {
            this.zq = zq;
        }

        public String getRzxx() {
            return rzxx;
        }

        public void setRzxx(String rzxx) {
            this.rzxx = rzxx;
        }

        public String getFjqk() {
            return fjqk;
        }

        public void setFjqk(String fjqk) {
            this.fjqk = fjqk;
        }

        public String getZflx() {
            return zflx;
        }

        public void setZflx(String zflx) {
            this.zflx = zflx;
        }

        public String getFjzp() {
            return fjzp;
        }

        public void setFjzp(String fjzp) {
            this.fjzp = fjzp;
        }
    }

    public static class ZhxxBean {
        /**
         * id : 232355
         * loginid : 11557
         * realName : 测试
         * bh : d428bc98-0fee-411b-92e9-229ddaf08a55
         * xm : 租户
         * xb : 男
         * sfzh : 411524198910140515
         * hjd : null
         * lxdh : 13003203235
         * fdlx : null
         * shzt : 0
         * shnr : null
         * shr : null
         * shrLoginid : null
         * shsj : null
         * del : false
         * posttime : 2020-04-14 18:02:10
         * updatetime : 2020-04-15 13:25:37
         * roomid : 483223
         * csqLoginid : 2022611104024849
         * xqbh : 2022611181674456
         * ldh : 2
         * sh : 201
         * gzdw :
         * cph : null
         * xzz : null
         * src : 租户
         * drbh : null
         * zpbh : null
         * birthday : null
         * headbase64 : null
         * mz : 汉
         * zlhtzpbh : 21e78767-5af3-41d1-8634-43b41ca599ac
         * hdj_s : 浙江省
         * hdj_city : 杭州市
         * hdj_x : 上城区
         * bdzpbase64 : null
         * ifqr : null
         * startTime : null
         * endTime : null
         * czfs : null
         * bz : null
         */

        private int id;
        private String loginid;
        private String realName;
        private String bh;
        private String xm;
        private String xb;
        private String sfzh;
        private String hjd;
        private String lxdh;
        private String fdlx;
        private int shzt;
        private String shnr;
        private String shr;
        private String shrLoginid;
        private String shsj;
        private boolean del;
        private String posttime;
        private String updatetime;
        private int roomid;
        private String csqLoginid;
        private String xqbh;
        private int ldh;
        private int sh;
        private String gzdw;
        private String cph;
        private String xzz;
        private String src;
        private String drbh;
        private String zpbh;
        private String birthday;
        private String headbase64;
        private String mz;
        private String zlhtzpbh;
        private String hdj_s;
        private String hdj_city;
        private String hdj_x;
        private String bdzpbase64;
        private Object ifqr;
        private String startTime;
        private String endTime;
        private String czfs;
        private String bz;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLoginid() {
            return loginid;
        }

        public void setLoginid(String loginid) {
            this.loginid = loginid;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getBh() {
            return bh;
        }

        public void setBh(String bh) {
            this.bh = bh;
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

        public String getHjd() {
            return hjd;
        }

        public void setHjd(String hjd) {
            this.hjd = hjd;
        }

        public String getLxdh() {
            return lxdh;
        }

        public void setLxdh(String lxdh) {
            this.lxdh = lxdh;
        }

        public String getFdlx() {
            return fdlx;
        }

        public void setFdlx(String fdlx) {
            this.fdlx = fdlx;
        }

        public int getShzt() {
            return shzt;
        }

        public void setShzt(int shzt) {
            this.shzt = shzt;
        }

        public String getShnr() {
            return shnr;
        }

        public void setShnr(String shnr) {
            this.shnr = shnr;
        }

        public String getShr() {
            return shr;
        }

        public void setShr(String shr) {
            this.shr = shr;
        }

        public String getShrLoginid() {
            return shrLoginid;
        }

        public void setShrLoginid(String shrLoginid) {
            this.shrLoginid = shrLoginid;
        }

        public String getShsj() {
            return shsj;
        }

        public void setShsj(String shsj) {
            this.shsj = shsj;
        }

        public boolean isDel() {
            return del;
        }

        public void setDel(boolean del) {
            this.del = del;
        }

        public String getPosttime() {
            return posttime;
        }

        public void setPosttime(String posttime) {
            this.posttime = posttime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getRoomid() {
            return roomid;
        }

        public void setRoomid(int roomid) {
            this.roomid = roomid;
        }

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

        public int getSh() {
            return sh;
        }

        public void setSh(int sh) {
            this.sh = sh;
        }

        public String getGzdw() {
            return gzdw;
        }

        public void setGzdw(String gzdw) {
            this.gzdw = gzdw;
        }

        public String getCph() {
            return cph;
        }

        public void setCph(String cph) {
            this.cph = cph;
        }

        public String getXzz() {
            return xzz;
        }

        public void setXzz(String xzz) {
            this.xzz = xzz;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDrbh() {
            return drbh;
        }

        public void setDrbh(String drbh) {
            this.drbh = drbh;
        }

        public String getZpbh() {
            return zpbh;
        }

        public void setZpbh(String zpbh) {
            this.zpbh = zpbh;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeadbase64() {
            return headbase64;
        }

        public void setHeadbase64(String headbase64) {
            this.headbase64 = headbase64;
        }

        public String getMz() {
            return mz;
        }

        public void setMz(String mz) {
            this.mz = mz;
        }

        public String getZlhtzpbh() {
            return zlhtzpbh;
        }

        public void setZlhtzpbh(String zlhtzpbh) {
            this.zlhtzpbh = zlhtzpbh;
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

        public String getBdzpbase64() {
            return bdzpbase64;
        }

        public void setBdzpbase64(String bdzpbase64) {
            this.bdzpbase64 = bdzpbase64;
        }

        public Object getIfqr() {
            return ifqr;
        }

        public void setIfqr(Object ifqr) {
            this.ifqr = ifqr;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCzfs() {
            return czfs;
        }

        public void setCzfs(String czfs) {
            this.czfs = czfs;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }
    }
}
