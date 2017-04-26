package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/4/1.
 */

public class RightsListModel {


    /**
     * id : 1
     * fundCode : USER_POOL_ZHPAY
     * stockCode : S201703282124304488
     * toUser : S201703282124304488
     * toAmount : 1000
     * toCurrency : FRB
     * createDatetime : Mar 28, 2017 9:28:45 PM
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private int id;
    private String fundCode;
    private String stockCode;
    private String toUser;
    private double toAmount;
    private String toCurrency;
    private String createDatetime;
    private String companyCode;
    private String systemCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
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

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
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
