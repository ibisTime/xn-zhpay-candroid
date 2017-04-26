package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/2/23.
 */

public class MyJewelHistoreModel implements Serializable {


    /**
     * code : J201703241514036317
     * templateCode : JT201703241513366804
     * periods : 1
     * toAmount : 10000
     * toCurrency : CGB
     * totalNum : 100
     * maxNum : 1000
     * investNum : 100
     * fromAmount : 10000
     * fromCurrency : CGB
     * slogan : 宣传文字
     * advPic : 6e2dc159-ff20-4d91-b8bf-8a62d95c97d7_1490339142375.jpg
     * startDatetime : Mar 24, 2017 3:14:03 PM
     * status : 1
     * winNumber : 10000060
     * winUser : U2017032820273497120
     * winDatetime : Mar 24, 2017 3:23:39 PM
     * companyCode : CD-CCG000007
     * systemCode : CD-CCG000007
     */

    private String code;
    private String templateCode;
    private int periods;
    private double toAmount;
    private String toCurrency;
    private int totalNum;
    private int maxNum;
    private int investNum;
    private double fromAmount;
    private String fromCurrency;
    private String slogan;
    private String advPic;
    private String startDatetime;
    private String status;
    private String winNumber;
    private String winUser;
    private String winDatetime;
    private String companyCode;
    private String systemCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getInvestNum() {
        return investNum;
    }

    public void setInvestNum(int investNum) {
        this.investNum = investNum;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
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

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(String winNumber) {
        this.winNumber = winNumber;
    }

    public String getWinUser() {
        return winUser;
    }

    public void setWinUser(String winUser) {
        this.winUser = winUser;
    }

    public String getWinDatetime() {
        return winDatetime;
    }

    public void setWinDatetime(String winDatetime) {
        this.winDatetime = winDatetime;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
