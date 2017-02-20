package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by dell1 on 2016/12/23.
 */

public class DuoBaoModel implements Serializable {


    /**
     * code : IW201701102054236452
     * storeCode : SC00000001
     * name : iphone7s一元夺宝
     * slogan : iphone7s
     * advPic : picPath
     * descriptionText : 图文描述
     * price1 : 1000
     * price2 : 0
     * price3 : 0
     * totalNum : 10
     * investNum : 10
     * startDatetime : Jan 10, 2017 8:56:00 PM
     * raiseDays : 2
     * winNumber : 10000002
     * winUserId : U000000000000001
     * status : 7
     * systemCode : CD-CZH000001
     * approver : admin
     * approveDatetime : Jan 10, 2017 8:55:18 PM
     * updater : admin
     * updateDatetime : Jan 10, 2017 8:56:00 PM
     * remark : 开始夺宝
     */

    private String code;
    private String storeCode;
    private String name;
    private String slogan;
    private String advPic;
    private String descriptionText;
    private String descriptionPic;
    private double price1;
    private int price2;
    private int price3;
    private int totalNum = 0;
    private int investNum = 0;
    private String startDatetime;
    private int raiseDays;
    private String winNumber;
    private String winUserId;
    private String status;
    private String systemCode;
    private String approver;
    private String approveDatetime;
    private String updater;
    private String updateDatetime;
    private String remark;

    public String getDescriptionPic() {
        return descriptionPic;
    }

    public void setDescriptionPic(String descriptionPic) {
        this.descriptionPic = descriptionPic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public int getPrice3() {
        return price3;
    }

    public void setPrice3(int price3) {
        this.price3 = price3;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getInvestNum() {
        return investNum;
    }

    public void setInvestNum(int investNum) {
        this.investNum = investNum;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public int getRaiseDays() {
        return raiseDays;
    }

    public void setRaiseDays(int raiseDays) {
        this.raiseDays = raiseDays;
    }

    public String getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(String winNumber) {
        this.winNumber = winNumber;
    }

    public String getWinUserId() {
        return winUserId;
    }

    public void setWinUserId(String winUserId) {
        this.winUserId = winUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
