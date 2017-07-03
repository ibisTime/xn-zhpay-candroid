package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

import java.util.List;


public class DuoBaoWinModel {

    private String code;
    private String userId;
    private String jewelCode;
    private String createDatetime;
    private String investDatetime;
    private int times;
    private int payAmount1;
    private int payAmount2;
    private int payAmount3;
    private String status;
    private String receiver;
    private String reMobile;
    private String mobile;
    private String reAddress;
    private String remark;
    private String systemCode;
    private String nickname;
    private JewelBean jewel;
    private String myInvestTimes;
    private List<JewelRecordNumberListBean> jewelRecordNumberList;

    public String getInvestDatetime() {
        return investDatetime;
    }

    public void setInvestDatetime(String investDatetime) {
        this.investDatetime = investDatetime;
    }

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReMobile() {
        return reMobile;
    }

    public void setReMobile(String reMobile) {
        this.reMobile = reMobile;
    }

    public String getReAddress() {
        return reAddress;
    }

    public void setReAddress(String reAddress) {
        this.reAddress = reAddress;
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

    public String getMyInvestTimes() {
        return myInvestTimes;
    }

    public void setMyInvestTimes(String myInvestTimes) {
        this.myInvestTimes = myInvestTimes;
    }

    public List<JewelRecordNumberListBean> getJewelRecordNumberList() {
        return jewelRecordNumberList;
    }

    public void setJewelRecordNumberList(List<JewelRecordNumberListBean> jewelRecordNumberList) {
        this.jewelRecordNumberList = jewelRecordNumberList;
    }

    public static class JewelBean {
        private String code;
        private String name;
        private String slogan;
        private String advPic;
        private String descriptionText;
        private int price1;
        private int price2;
        private int price3;
        private double amount;
        private int totalNum;
        private int investNum;
        private String startDatetime;
        private String lotteryDatetime;
        private int raiseDays;
        private String winNumber;
        private String winUserId;
        private String status;
        private String systemCode;
        private String approver;
        private String approveDatetime;
        private String updater;
        private String updateDatetime;
        private String remark;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
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

        public String getDescriptionText() {
            return descriptionText;
        }

        public void setDescriptionText(String descriptionText) {
            this.descriptionText = descriptionText;
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

        public String getStartDatetime() {
            return startDatetime;
        }

        public void setStartDatetime(String startDatetime) {
            this.startDatetime = startDatetime;
        }

        public String getLotteryDatetime() {
            return lotteryDatetime;
        }

        public void setLotteryDatetime(String lotteryDatetime) {
            this.lotteryDatetime = lotteryDatetime;
        }

        public int getRaiseDays() {
            return raiseDays;
        }

        public void setRaiseDays(int raiseDays) {
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

        public String getApprover() {
            return approver;
        }

        public void setApprover(String approver) {
            this.approver = approver;
        }

        public String getApproveDatetime() {
            return approveDatetime;
        }

        public void setApproveDatetime(String approveDatetime) {
            this.approveDatetime = approveDatetime;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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
