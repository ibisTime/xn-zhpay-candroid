package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by dell1 on 2016/12/17.
 */

public class ProductTypeModel {


    /**
     * code : 1
     * parentCode : 0
     * type : 1
     * name : 一元夺宝
     * orderNo : 3
     * systemCode : CD-CZH000001
     */

    private String code;
    private String parentCode;
    private String type;
    private String name;
    private int orderNo;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
