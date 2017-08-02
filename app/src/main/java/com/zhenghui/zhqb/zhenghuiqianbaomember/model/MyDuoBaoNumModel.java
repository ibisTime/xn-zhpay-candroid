package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

import java.util.List;

public class MyDuoBaoNumModel {

    private String code;
    private String userId;
    private String jewelCode;
    private String createDatetime;
    private int times;
    private int payAmount1;
    private int payAmount2;
    private int payAmount3;
    private String status;
    private String remark;
    private String systemCode;
    private String nickname;

    private JewelBean jewel;

    private List<JewelRecordNumberListBean> jewelRecordNumberList;

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

    public String getJewelCode() {
        return jewelCode;
    }

    public void setJewelCode(String jewelCode) {
        this.jewelCode = jewelCode;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public JewelBean getJewel() {
        return jewel;
    }

    public void setJewel(JewelBean jewel) {
        this.jewel = jewel;
    }

    public List<JewelRecordNumberListBean> getJewelRecordNumberList() {
        return jewelRecordNumberList;
    }

    public void setJewelRecordNumberList(List<JewelRecordNumberListBean> jewelRecordNumberList) {
        this.jewelRecordNumberList = jewelRecordNumberList;
    }

    public static class JewelBean {
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class JewelRecordNumberListBean {
        private int id;
        private String jewelCode;
        private String recordCode;
        private String number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getJewelCode() {
            return jewelCode;
        }

        public void setJewelCode(String jewelCode) {
            this.jewelCode = jewelCode;
        }

        public String getRecordCode() {
            return recordCode;
        }

        public void setRecordCode(String recordCode) {
            this.recordCode = recordCode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}