package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/1/11.
 */

public class ScheduleModel {


    /**
     * code : IR201701130324151895
     * userId : U2017010614240498971
     * jewelCode : IW201701122106370580
     * createDatetime : Jan 13, 2017 3:24:15 AM
     * times : 1
     * payAmount : 1000
     * status : 0
     * remark : 已分配夺宝号，待开奖
     * systemCode : CD-CZH000001
     * nickname : 40498971
     * photo : ANDROID_1483698115033_1080_1920.jpg
     */

    private String code;
    private String userId;
    private String jewelCode;
    private String createDatetime;
    private int times;
    private double payAmount1;
    private int payAmount2;
    private int payAmount3;
    private String status;
    private String remark;
    private String systemCode;
    private String nickname;
    private String photo;

    public double getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(double payAmount1) {
        this.payAmount1 = payAmount1;
    }

    public int getPayAmount2() {
        return payAmount2;
    }

    public void setPayAmount2(int payAmount2) {
        this.payAmount2 = payAmount2;
    }

    public int getPayAmount3() {
        return payAmount3;
    }

    public void setPayAmount3(int payAmount3) {
        this.payAmount3 = payAmount3;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJewelCode() {
        return jewelCode;
    }

    public void setJewelCode(String jewelCode) {
        this.jewelCode = jewelCode;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
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
