package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

import java.io.Serializable;


public class DuoBaoDetailsModel implements Serializable {

    private String code;
    private String storeCode;
    private String name;
    private String slogan;
    private String advPic;
    private String description;
    private int investNum;
    private int investAmount;
    private int totalNum;
    private int price;
    private String startDatetime;
    private String raiseDays;
    private String winNumber;
    private String winUserId;
    private String lotAlg;
    private String status;
    private String systemCode;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInvestNum() {
        return investNum;
    }

    public void setInvestNum(int investNum) {
        this.investNum = investNum;
    }

    public int getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(int investAmount) {
        this.investAmount = investAmount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getRaiseDays() {
        return raiseDays;
    }

    public void setRaiseDays(String raiseDays) {
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

    public String getLotAlg() {
        return lotAlg;
    }

    public void setLotAlg(String lotAlg) {
        this.lotAlg = lotAlg;
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
}
