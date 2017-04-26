package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/2/23.
 */

public class GiveModel {


    /**
     * code : HM20170401104521574209
     * hzbCode : H201704011045215682
     * slogan : 小小心意
     * owner : U2017032913574410381
     * ownerCurrency : HBYJ
     * ownerAmount : 5000
     * receiveCurrency : GXJL
     * receiveAmount : 5000
     * createDatetime : Apr 1, 2017 12:00:00 AM
     * remark : 该红包已经被领取
     * status : 2
     * receiver : U2017040115062606386
     * receiveDatetime : Apr 1, 2017 3:59:34 PM
     * systemCode : CD-CZH000001
     * companyCode : CD-CZH000001
     * ownerUser : {"userId":"U2017032913574410381","loginName":"18984955240","nickname":"74410381","photo":"ANDROID_1490874687342_0_0.jpg","mobile":"18984955240","identityFlag":"1","userReferee":"U2017010713451027748"}
     * receiverUser : {"userId":"U2017040115062606386","loginName":"18767101909","nickname":"62606386","mobile":"18767101909","identityFlag":"0","userReferee":"U2017032913574410381"}
     */

    private String code;
    private String hzbCode;
    private String slogan;
    private String owner;
    private String ownerCurrency;
    private int ownerAmount;
    private String receiveCurrency;
    private int receiveAmount;
    private String createDatetime;
    private String remark;
    private String status;
    private String receiver;
    private String receiveDatetime;
    private String systemCode;
    private String companyCode;
    /**
     * userId : U2017032913574410381
     * loginName : 18984955240
     * nickname : 74410381
     * photo : ANDROID_1490874687342_0_0.jpg
     * mobile : 18984955240
     * identityFlag : 1
     * userReferee : U2017010713451027748
     */

    private OwnerUserBean ownerUser;
    /**
     * userId : U2017040115062606386
     * loginName : 18767101909
     * nickname : 62606386
     * mobile : 18767101909
     * identityFlag : 0
     * userReferee : U2017032913574410381
     */

    private ReceiverUserBean receiverUser;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHzbCode() {
        return hzbCode;
    }

    public void setHzbCode(String hzbCode) {
        this.hzbCode = hzbCode;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerCurrency() {
        return ownerCurrency;
    }

    public void setOwnerCurrency(String ownerCurrency) {
        this.ownerCurrency = ownerCurrency;
    }

    public int getOwnerAmount() {
        return ownerAmount;
    }

    public void setOwnerAmount(int ownerAmount) {
        this.ownerAmount = ownerAmount;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }

    public int getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(int receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiveDatetime() {
        return receiveDatetime;
    }

    public void setReceiveDatetime(String receiveDatetime) {
        this.receiveDatetime = receiveDatetime;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public OwnerUserBean getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(OwnerUserBean ownerUser) {
        this.ownerUser = ownerUser;
    }

    public ReceiverUserBean getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(ReceiverUserBean receiverUser) {
        this.receiverUser = receiverUser;
    }

    public static class OwnerUserBean {
        private String userId;
        private String loginName;
        private String nickname;
        private String photo;
        private String mobile;
        private String identityFlag;
        private String userReferee;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdentityFlag() {
            return identityFlag;
        }

        public void setIdentityFlag(String identityFlag) {
            this.identityFlag = identityFlag;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }
    }

    public static class ReceiverUserBean {
        private String userId;
        private String loginName;
        private String nickname;
        private String mobile;
        private String identityFlag;
        private String userReferee;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdentityFlag() {
            return identityFlag;
        }

        public void setIdentityFlag(String identityFlag) {
            this.identityFlag = identityFlag;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }
    }
}
