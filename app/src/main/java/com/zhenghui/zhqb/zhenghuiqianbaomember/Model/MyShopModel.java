package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by dell1 on 2016/12/24.
 */

public class MyShopModel implements Serializable {

    /**
     * code : XF201703301408365739
     * userId : U2017032913574410381
     * storeCode : SJ201703291152426359
     * price : 10
     * createDatetime : Mar 30, 2017 2:08:36 PM
     * status : 1
     * payType : 3
     * payGroup : XF201703301408365739
     * payCode : AJ2017033014083659877
     * payAmount1 : 10
     * payDatetime : Mar 30, 2017 2:08:43 PM
     * remark : 支付宝支付O2O消费
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     * store : {"code":"SJ201703291152426359","name":"恍恍惚惚","level":"1","type":"2","slogan":"恍恍惚惚","advPic":"IOS_1490768654437286_950_1280.jpg","pic":"IOS_1490768666050592_800_600.jpg||IOS_1490768666039728_950_1280.jpg","description":"恍恍惚惚","province":"浙江省","city":"杭州市","area":"上城区","address":"隐隐约约","longitude":"120.00161517444184","latitude":"30.28818213827786","bookMobile":"1386855866","smsMobile":"13868074590","legalPersonName":"恍恍惚惚","userReferee":"U2017010713451027748","rate1":0.1,"rate2":0.1,"status":"2","updater":"U2017032911375586527","updateDatetime":"Mar 29, 2017 2:46:45 PM","owner":"U2017032911375586527","totalRmbNum":0,"totalJfNum":0,"totalDzNum":0,"totalScNum":0,"companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     */

    private String code;
    private String userId;
    private String storeCode;
    private int price;
    private String createDatetime;
    private String status;
    private String payType;
    private String payGroup;
    private String payCode;
    private double payAmount1;
    private double payAmount2;
    private double payAmount3;
    private String payDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;
    /**
     * code : SJ201703291152426359
     * name : 恍恍惚惚
     * level : 1
     * type : 2
     * slogan : 恍恍惚惚
     * advPic : IOS_1490768654437286_950_1280.jpg
     * pic : IOS_1490768666050592_800_600.jpg||IOS_1490768666039728_950_1280.jpg
     * description : 恍恍惚惚
     * province : 浙江省
     * city : 杭州市
     * area : 上城区
     * address : 隐隐约约
     * longitude : 120.00161517444184
     * latitude : 30.28818213827786
     * bookMobile : 1386855866
     * smsMobile : 13868074590
     * legalPersonName : 恍恍惚惚
     * userReferee : U2017010713451027748
     * rate1 : 0.1
     * rate2 : 0.1
     * status : 2
     * updater : U2017032911375586527
     * updateDatetime : Mar 29, 2017 2:46:45 PM
     * owner : U2017032911375586527
     * totalRmbNum : 0
     * totalJfNum : 0
     * totalDzNum : 0
     * totalScNum : 0
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private StoreBean store;
    private StoreTicketBean storeTicket;

    public double getPayAmount2() {
        return payAmount2;
    }

    public void setPayAmount2(double payAmount2) {
        this.payAmount2 = payAmount2;
    }

    public double getPayAmount3() {
        return payAmount3;
    }

    public void setPayAmount3(double payAmount3) {
        this.payAmount3 = payAmount3;
    }

    public StoreTicketBean getStoreTicket() {
        return storeTicket;
    }

    public void setStoreTicket(StoreTicketBean storeTicket) {
        this.storeTicket = storeTicket;
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

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayGroup() {
        return payGroup;
    }

    public void setPayGroup(String payGroup) {
        this.payGroup = payGroup;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public double getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(double payAmount1) {
        this.payAmount1 = payAmount1;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public static class StoreTicketBean implements Serializable {
        private String code;
        private String name;
        private String type;
        private double key1;
        private double key2;
        private String description;
        private double price;
        private String currency;
        private String validateStart;
        private String validateEnd;
        private String createDatetime;
        private String status;
        private String storeCode;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getKey1() {
            return key1;
        }

        public void setKey1(double key1) {
            this.key1 = key1;
        }

        public double getKey2() {
            return key2;
        }

        public void setKey2(double key2) {
            this.key2 = key2;
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

        public String getValidateStart() {
            return validateStart;
        }

        public void setValidateStart(String validateStart) {
            this.validateStart = validateStart;
        }

        public String getValidateEnd() {
            return validateEnd;
        }

        public void setValidateEnd(String validateEnd) {
            this.validateEnd = validateEnd;
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

        public String getStoreCode() {
            return storeCode;
        }

        public void setStoreCode(String storeCode) {
            this.storeCode = storeCode;
        }

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }
    }

    public static class StoreBean implements Serializable  {
        private String code;
        private String name;
        private String level;
        private String type;
        private String slogan;
        private String advPic;
        private String pic;
        private String description;
        private String province;
        private String city;
        private String area;
        private String address;
        private String longitude;
        private String latitude;
        private String bookMobile;
        private String smsMobile;
        private String legalPersonName;
        private String userReferee;
        private double rate1;
        private double rate2;
        private String status;
        private String updater;
        private String updateDatetime;
        private String owner;
        private int totalRmbNum;
        private int totalJfNum;
        private int totalDzNum;
        private int totalScNum;
        private String companyCode;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getBookMobile() {
            return bookMobile;
        }

        public void setBookMobile(String bookMobile) {
            this.bookMobile = bookMobile;
        }

        public String getSmsMobile() {
            return smsMobile;
        }

        public void setSmsMobile(String smsMobile) {
            this.smsMobile = smsMobile;
        }

        public String getLegalPersonName() {
            return legalPersonName;
        }

        public void setLegalPersonName(String legalPersonName) {
            this.legalPersonName = legalPersonName;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }

        public double getRate1() {
            return rate1;
        }

        public void setRate1(double rate1) {
            this.rate1 = rate1;
        }

        public double getRate2() {
            return rate2;
        }

        public void setRate2(double rate2) {
            this.rate2 = rate2;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getTotalRmbNum() {
            return totalRmbNum;
        }

        public void setTotalRmbNum(int totalRmbNum) {
            this.totalRmbNum = totalRmbNum;
        }

        public int getTotalJfNum() {
            return totalJfNum;
        }

        public void setTotalJfNum(int totalJfNum) {
            this.totalJfNum = totalJfNum;
        }

        public int getTotalDzNum() {
            return totalDzNum;
        }

        public void setTotalDzNum(int totalDzNum) {
            this.totalDzNum = totalDzNum;
        }

        public int getTotalScNum() {
            return totalScNum;
        }

        public void setTotalScNum(int totalScNum) {
            this.totalScNum = totalScNum;
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
