package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/2/22.
 */

public class TargetModel implements Serializable {


    /**
     * code : J201703221508346783
     * templateCode : JT201703221427246662
     * periods : 1
     * toAmount : 10
     * toCurrency : FRB
     * totalNum : 10
     * maxNum : 10
     * investNum : 10
     * fromAmount : 10
     * fromCurrency : FRB
     * slogan : 宣传文字update
     * advPic : 宣传图update
     * startDatetime : Mar 22, 2017 3:08:34 PM
     * status : 1
     * winNumber : 10000008
     * winUser : U2017032216121358915
     * winDatetime : Mar 22, 2017 5:10:00 PM
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     * user : {"userId":"U2017032216121358915","loginName":"18984955240","nickname":"21358915","mobile":"18984955240"}
     */

    private String code;
    private String templateCode;
    private int periods;
    private int toAmount;
    private String toCurrency;
    private int totalNum;
    private int maxNum;
    private int investNum;
    private int fromAmount;
    private String fromCurrency;
    private String slogan;
    private String advPic;
    private String startDatetime;
    private String status;
    private String winNumber = "";
    private String winUser;
    private String winDatetime;
    private String companyCode;
    private String systemCode;
    /**
     * userId : U2017032216121358915
     * loginName : 18984955240
     * nickname : 21358915
     * mobile : 18984955240
     */

    private UserBean user;

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

    public int getToAmount() {
        return toAmount;
    }

    public void setToAmount(int toAmount) {
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

    public int getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(int fromAmount) {
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

}
