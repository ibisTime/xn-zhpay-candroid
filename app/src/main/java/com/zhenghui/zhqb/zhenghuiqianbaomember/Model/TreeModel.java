package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/1/8.
 */

public class TreeModel {


    /**
     * code : HZB001
     * name : 汇赚宝
     * pic : http://123.33.33.33/default.jpg
     * description : 摇钱树玩法，摇一摇，摇出你的美
     * price : 2000000
     * currency : CNY
     * systemCode : CD-CZH000001
     */

    private String code;
    private String name;
    private String pic;
    private String description;
    private double price;
    private String currency;
    private String systemCode;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
