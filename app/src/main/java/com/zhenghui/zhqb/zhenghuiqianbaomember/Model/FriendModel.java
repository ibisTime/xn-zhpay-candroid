package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by dell1 on 2016/12/22.
 */

public class FriendModel {


    /**
     * userId : U2017010713451027748
     * loginName : 18767101909
     * nickname : 51027748
     * loginPwdStrength : 1
     * kind : f1
     * level : 0
     * userReferee : U2017011704242588411
     * mobile : 18767101909
     * idKind : 1
     * idNo : 1
     * realName : 谢延径
     * tradePwdStrength : 1
     * status : 0
     * updater :
     * updateDatetime : Jan 17, 2017 4:20:38 AM
     * amount : 0
     * ljAmount : 0
     * systemCode : CD-CZH000001
     * userExt : {"userId":"U2017010713451027748","province":"浙江省","city":"杭州市","area":"余杭区","systemCode":"CD-CZH000001","loginName":"18767101909","mobile":"18767101909"}
     * refeereLevel : -1
     */

    private String userId;
    private String loginName;
    private String nickname = "未知";
    private String loginPwdStrength;
    private String kind;
    private String level;
    private String userReferee;
    private String mobile;
    private String idKind;
    private String idNo;
    private String realName;
    private String tradePwdStrength;
    private String status;
    private String updater;
    private String updateDatetime;
    private int amount;
    private int ljAmount;
    private String systemCode;
    /**
     * userId : U2017010713451027748
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * systemCode : CD-CZH000001
     * loginName : 18767101909
     * mobile : 18767101909
     */

    private UserExtBean userExt;
    private int refeereLevel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLoginPwdStrength() {
        return loginPwdStrength;
    }

    public void setLoginPwdStrength(String loginPwdStrength) {
        this.loginPwdStrength = loginPwdStrength;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUserReferee() {
        return userReferee;
    }

    public void setUserReferee(String userReferee) {
        this.userReferee = userReferee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdKind() {
        return idKind;
    }

    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTradePwdStrength() {
        return tradePwdStrength;
    }

    public void setTradePwdStrength(String tradePwdStrength) {
        this.tradePwdStrength = tradePwdStrength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLjAmount() {
        return ljAmount;
    }

    public void setLjAmount(int ljAmount) {
        this.ljAmount = ljAmount;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public UserExtBean getUserExt() {
        return userExt;
    }

    public void setUserExt(UserExtBean userExt) {
        this.userExt = userExt;
    }

    public int getRefeereLevel() {
        return refeereLevel;
    }

    public void setRefeereLevel(int refeereLevel) {
        this.refeereLevel = refeereLevel;
    }

    public static class UserExtBean {
        private String userId;
        private String province;
        private String city;
        private String area;
        private String photo = "";
        private String systemCode;
        private String loginName;
        private String mobile;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
