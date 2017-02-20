package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/1/11.
 */

public class MyDuoBaoModel implements Serializable {


    /**
     * code : IR201701112201300306
     * userId : U2017010614240498971
     * jewelCode : IW201701121834162328
     * createDatetime : Jan 11, 2017 10:01:30 PM
     * times : 1
     * payAmount1 : 10000
     * payAmount2 : 100000
     * payAmount3 : 1000000
     * status : 1
     * remark : 已分配夺宝号，待开奖
     * systemCode : CD-CZH000001
     * jewel : {"code":"IW201701121834162328","name":"To yuan duo nap","slogan":"Moment hao","advPic":"IOS_1484104999321146_3000_2002.jpg","price1":10000,"price2":100000,"price3":1000000,"totalNum":1000,"investNum":1,"raiseDays":4,"status":"3","remark":"拖过"}
     * myInvestTimes : 1
     */

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
    /**
     * code : IW201701121834162328
     * name : To yuan duo nap
     * slogan : Moment hao
     * advPic : IOS_1484104999321146_3000_2002.jpg
     * price1 : 10000
     * price2 : 100000
     * price3 : 1000000
     * totalNum : 1000
     * investNum : 1
     * raiseDays : 4
     * status : 3
     * remark : 拖过
     */

    private JewelBean jewel;
    private String myInvestTimes;

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

    public JewelBean getJewel() {
        return jewel;
    }

    public void setJewel(JewelBean jewel) {
        this.jewel = jewel;
    }

    public String getMyInvestTimes() {
        return myInvestTimes;
    }

    public void setMyInvestTimes(String myInvestTimes) {
        this.myInvestTimes = myInvestTimes;
    }

    public static class JewelBean implements Serializable {
        private String code;
        private String name;
        private String slogan;
        private String advPic;
        private int price1;
        private int price2;
        private int price3;
        private int totalNum;
        private int investNum;
        private int raiseDays;
        private String winUserId;
        private String winNumber;
        private String status;
        private String remark;

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

        public int getPrice1() {
            return price1;
        }

        public void setPrice1(int price1) {
            this.price1 = price1;
        }

        public int getPrice2() {
            return price2;
        }

        public void setPrice2(int price2) {
            this.price2 = price2;
        }

        public int getPrice3() {
            return price3;
        }

        public void setPrice3(int price3) {
            this.price3 = price3;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getInvestNum() {
            return investNum;
        }

        public void setInvestNum(int investNum) {
            this.investNum = investNum;
        }

        public int getRaiseDays() {
            return raiseDays;
        }

        public void setRaiseDays(int raiseDays) {
            this.raiseDays = raiseDays;
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
    }
}
