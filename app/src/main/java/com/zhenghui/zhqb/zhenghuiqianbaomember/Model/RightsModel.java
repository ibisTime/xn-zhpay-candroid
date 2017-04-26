package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/4/1.
 */

public class RightsModel {


    /**
     * code : S201703282124304488
     * userId : U2017032913574410381
     * fundCode : USER_POOL_ZHPAY
     * costAmount : 500000
     * costCurrency : FRB
     * backInterval : 5
     * profitAmount : 1000
     * profitCurrency : FRB
     * backCount : 0
     * backAmount : 0
     * todayAmount : 0
     * nextBackDate : Mar 28, 2017 9:24:30 PM
     * createDatetime : Mar 28, 2017 9:24:30 PM
     * status : 2
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private String code;
    private String userId;
    private String fundCode;
    private int costAmount;
    private String costCurrency;
    private int backInterval;
    private int profitAmount;
    private String profitCurrency;
    private int backCount;
    private int backAmount;
    private int todayAmount;
    private String nextBackDate;
    private String createDatetime;
    private String status;
    private String companyCode;
    private String systemCode;

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

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public int getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(int costAmount) {
        this.costAmount = costAmount;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public void setCostCurrency(String costCurrency) {
        this.costCurrency = costCurrency;
    }

    public int getBackInterval() {
        return backInterval;
    }

    public void setBackInterval(int backInterval) {
        this.backInterval = backInterval;
    }

    public int getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(int profitAmount) {
        this.profitAmount = profitAmount;
    }

    public String getProfitCurrency() {
        return profitCurrency;
    }

    public void setProfitCurrency(String profitCurrency) {
        this.profitCurrency = profitCurrency;
    }

    public int getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }

    public int getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(int backAmount) {
        this.backAmount = backAmount;
    }

    public int getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(int todayAmount) {
        this.todayAmount = todayAmount;
    }

    public String getNextBackDate() {
        return nextBackDate;
    }

    public void setNextBackDate(String nextBackDate) {
        this.nextBackDate = nextBackDate;
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
