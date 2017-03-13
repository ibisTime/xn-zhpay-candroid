package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/2/22.
 */

public class JewelRecordModel {


    /**
     * code : JR201702221944379478
     * userId : U2017022117434980222
     * jewelCode : J201702221727520758
     * investDatetime : Feb 22, 2017 7:44:37 PM
     * times : 97
     * payAmount : 970000
     * payDatetime : Feb 22, 2017 7:44:37 PM
     * status : 1
     * remark : 已分配号码，待开奖
     * ip : 183.129.227.58
     * systemCode : CD-CZH000001
     * nickname : 34980222
     * mobile : 15888888888
     * photo : IOS_1487751839428025_750_1334.jpg
     * jewel : {"code":"J201702221727520758","templateCode":"JT201702212000179938","periods":4,"currency":"FRB","amount":10000,"totalNum":100,"price":10000,"maxInvestNum":1000,"advText":"宣传文字","advPic":"宣传图","investNum":97,"createDatetime":"Feb 22, 2017 5:27:52 PM","status":"0","systemCode":"CD-CZH000001"}
     */

    private String code;
    private String userId;
    private String jewelCode;
    private String investDatetime;
    private int times;
    private int payAmount;
    private String payDatetime;
    private String status;
    private String remark;
    private String ip;
    private String systemCode;
    private String nickname;
    private String mobile;
    private String photo;
    /**
     * code : J201702221727520758
     * templateCode : JT201702212000179938
     * periods : 4
     * currency : FRB
     * amount : 10000
     * totalNum : 100
     * price : 10000
     * maxInvestNum : 1000
     * advText : 宣传文字
     * advPic : 宣传图
     * investNum : 97
     * createDatetime : Feb 22, 2017 5:27:52 PM
     * status : 0
     * systemCode : CD-CZH000001
     */

    private JewelBean jewel;

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

    public String getInvestDatetime() {
        return investDatetime;
    }

    public void setInvestDatetime(String investDatetime) {
        this.investDatetime = investDatetime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public JewelBean getJewel() {
        return jewel;
    }

    public void setJewel(JewelBean jewel) {
        this.jewel = jewel;
    }

    public static class JewelBean {
        private String code;
        private String templateCode;
        private int periods;
        private String currency;
        private int amount;
        private int totalNum;
        private double price;
        private int maxInvestNum;
        private String advText;
        private String advPic;
        private int investNum;
        private String createDatetime;
        private String status;
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

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getMaxInvestNum() {
            return maxInvestNum;
        }

        public void setMaxInvestNum(int maxInvestNum) {
            this.maxInvestNum = maxInvestNum;
        }

        public String getAdvText() {
            return advText;
        }

        public void setAdvText(String advText) {
            this.advText = advText;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }

        public int getInvestNum() {
            return investNum;
        }

        public void setInvestNum(int investNum) {
            this.investNum = investNum;
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

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }
    }
}
