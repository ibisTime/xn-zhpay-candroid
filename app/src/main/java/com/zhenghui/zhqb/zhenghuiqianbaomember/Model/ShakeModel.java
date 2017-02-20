package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/1/9.
 */

public class ShakeModel implements Serializable {


    /**
     * id : 2
     * userId : U2017010713451027748
     * hzbCode : HZB001
     * status : 2
     * price : 2000000
     * currency : CNY
     * periodRockNum : 0
     * totalRockNum : 0
     * systemCode : CD-CZH000001
     * distance : 25
     * shareUrl : http://www.sina.com.cn
     * mobile : 18767101909
     */

    private int id;
    private String userId;
    private String hzbCode;
    private String status;
    private int price;
    private String currency;
    private int periodRockNum;
    private int totalRockNum;
    private String systemCode;
    private String distance;
    private String shareUrl;
    private String mobile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHzbCode() {
        return hzbCode;
    }

    public void setHzbCode(String hzbCode) {
        this.hzbCode = hzbCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
