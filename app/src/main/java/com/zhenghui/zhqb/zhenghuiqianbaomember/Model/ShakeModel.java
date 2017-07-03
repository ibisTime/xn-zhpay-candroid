package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

import java.io.Serializable;

public class ShakeModel implements Serializable {

    private String code;
    private String userId;
    private String templateCode;
    private int price;
    private String currency;
    private int periodRockNum;
    private int totalRockNum;
    private int backAmount1;
    private int backAmount2;
    private int backAmount3;
    private String createDatetime;
    private String status;
    private String payDatetime;
    private int payAmount1;
    private int payAmount2;
    private int payAmount3;
    private String systemCode;
    private String companyCode;
    private String distance;
    private String shareUrl;

    private UserBean user;

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

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPeriodRockNum() {
        return periodRockNum;
    }

    public void setPeriodRockNum(int periodRockNum) {
        this.periodRockNum = periodRockNum;
    }

    public int getTotalRockNum() {
        return totalRockNum;
    }

    public void setTotalRockNum(int totalRockNum) {
        this.totalRockNum = totalRockNum;
    }

    public int getBackAmount1() {
        return backAmount1;
    }

    public void setBackAmount1(int backAmount1) {
        this.backAmount1 = backAmount1;
    }

    public int getBackAmount2() {
        return backAmount2;
    }

    public void setBackAmount2(int backAmount2) {
        this.backAmount2 = backAmount2;
    }

    public int getBackAmount3() {
        return backAmount3;
    }

    public void setBackAmount3(int backAmount3) {
        this.backAmount3 = backAmount3;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public int getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(int payAmount1) {
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }


}
