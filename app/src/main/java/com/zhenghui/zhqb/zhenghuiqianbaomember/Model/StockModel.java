package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by dell1 on 2016/12/23.
 */

public class StockModel {


    /**
     * code : GF201612191707503581
     * name : 股份两千update
     * pic :
     * description : 股份1
     * capital : 200
     * price : 2000
     * currency : CNY
     * backInterval : 1
     * backCount : 10
     * welfare1 : 200
     * welfare2 : 200
     * status : 1
     * systemCode : system001
     */

    private String code;
    private String name;
    private String pic;
    private String description;
    private int capital;
    private int price;
    private String currency;
    private int backInterval;
    private int backCount;
    private int welfare1;
    private int welfare2;
    private String status;
    private String systemCode;
    private boolean choose = false;

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
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

    public int getBackInterval() {
        return backInterval;
    }

    public void setBackInterval(int backInterval) {
        this.backInterval = backInterval;
    }

    public int getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }

    public int getWelfare1() {
        return welfare1;
    }

    public void setWelfare1(int welfare1) {
        this.welfare1 = welfare1;
    }

    public int getWelfare2() {
        return welfare2;
    }

    public void setWelfare2(int welfare2) {
        this.welfare2 = welfare2;
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
