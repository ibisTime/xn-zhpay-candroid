package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/1/9.
 */

public class ShakeModel implements Serializable {


    /**
     * code : H201704011045215682
     * userId : U2017032913574410381
     * templateCode : HT201703311118384902
     * price : 10000
     * currency : CNY
     * periodRockNum : 0
     * totalRockNum : 0
     * backAmount1 : 0
     * backAmount2 : 0
     * backAmount3 : 0
     * createDatetime : Apr 1, 2017 10:45:21 AM
     * status : 1
     * payDatetime : Apr 1, 2017 10:45:21 AM
     * payAmount1 : 10000
     * payAmount2 : 0
     * payAmount3 : 0
     * systemCode : CD-CZH000001
     * companyCode : CD-CZH000001
     * distance : 2
     * shareUrl : http://www.sina.com.cn
     * user : {"userId":"U2017032913574410381","loginName":"18984955240","nickname":"74410381","photo":"ANDROID_1490874687342_0_0.jpg","mobile":"18984955240","identityFlag":"1","userReferee":"U2017010713451027748"}
     */

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
    /**
     * userId : U2017032913574410381
     * loginName : 18984955240
     * nickname : 74410381
     * photo : ANDROID_1490874687342_0_0.jpg
     * mobile : 18984955240
     * identityFlag : 1
     * userReferee : U2017010713451027748
     */

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
