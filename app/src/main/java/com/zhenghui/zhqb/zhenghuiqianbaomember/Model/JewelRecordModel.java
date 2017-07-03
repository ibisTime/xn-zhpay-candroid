package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

public class JewelRecordModel {


    private String code;
    private String userId;
    private String jewelCode;
    private String investDatetime;
    private int times;
    private String ip;
    private String status;
    private int payAmount;
    private String payDatetime;
    private String companyCode;
    private String systemCode;

    private JewelBean jewel;

    private UserBean user;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public JewelBean getJewel() {
        return jewel;
    }

    public void setJewel(JewelBean jewel) {
        this.jewel = jewel;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class JewelBean {
        private String code;
        private String templateCode;
        private int periods;
        private int toAmount;
        private String toCurrency;
        private int totalNum;
        private int maxNum;
        private int investNum;
        private int fromAmount;
        private String fromCurrency;
        private String slogan;
        private String advPic;
        private String startDatetime;
        private String status;
        private String companyCode;
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

        public int getToAmount() {
            return toAmount;
        }

        public void setToAmount(int toAmount) {
            this.toAmount = toAmount;
        }

        public String getToCurrency() {
            return toCurrency;
        }

        public void setToCurrency(String toCurrency) {
            this.toCurrency = toCurrency;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public int getInvestNum() {
            return investNum;
        }

        public void setInvestNum(int investNum) {
            this.investNum = investNum;
        }

        public int getFromAmount() {
            return fromAmount;
        }

        public void setFromAmount(int fromAmount) {
            this.fromAmount = fromAmount;
        }

        public String getFromCurrency() {
            return fromCurrency;
        }

        public void setFromCurrency(String fromCurrency) {
            this.fromCurrency = fromCurrency;
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

        public String getStartDatetime() {
            return startDatetime;
        }

        public void setStartDatetime(String startDatetime) {
            this.startDatetime = startDatetime;
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


}
