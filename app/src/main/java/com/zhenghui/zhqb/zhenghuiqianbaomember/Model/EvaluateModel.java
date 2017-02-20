package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/2/9.
 */

public class EvaluateModel {


    /**
     * id : 15
     * type : 2
     * evaluateType : A
     * interacter : U2017011704412811088
     * interactDatetime : Feb 9, 2017 6:19:13 PM
     * jewelCode : CP201701170138164781
     * orderCode : DD201701170305366543604
     * systemCode : CD-CZH000001
     * interacterUser : {"userId":"U2017011704412811088","nickname":"╮(￣▽￣)╭","photo":"ANDROID_1484670115205_1080_1920.jpg"}
     */

    private int id;
    private String type;
    private String evaluateType;
    private String interacter;
    private String interactDatetime;
    private String jewelCode;
    private String orderCode;
    private String systemCode;
    /**
     * userId : U2017011704412811088
     * nickname : ╮(￣▽￣)╭
     * photo : ANDROID_1484670115205_1080_1920.jpg
     */

    private InteracterUserBean interacterUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvaluateType() {
        return evaluateType;
    }

    public void setEvaluateType(String evaluateType) {
        this.evaluateType = evaluateType;
    }

    public String getInteracter() {
        return interacter;
    }

    public void setInteracter(String interacter) {
        this.interacter = interacter;
    }

    public String getInteractDatetime() {
        return interactDatetime;
    }

    public void setInteractDatetime(String interactDatetime) {
        this.interactDatetime = interactDatetime;
    }

    public String getJewelCode() {
        return jewelCode;
    }

    public void setJewelCode(String jewelCode) {
        this.jewelCode = jewelCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public InteracterUserBean getInteracterUser() {
        return interacterUser;
    }

    public void setInteracterUser(InteracterUserBean interacterUser) {
        this.interacterUser = interacterUser;
    }

    public static class InteracterUserBean {
        private String userId;
        private String nickname;
        private String photo;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
    }
}
