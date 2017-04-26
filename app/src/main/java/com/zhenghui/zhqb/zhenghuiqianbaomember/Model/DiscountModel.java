package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/1/6.
 */

public class DiscountModel implements Serializable {


    /**
     * code : UT201704041609383483
     * userId : U2017032913574410381
     * ticketCode : ZKQ201704041602292069
     * createDatetime : Apr 4, 2017 4:09:38 PM
     * status : 0
     * systemCode : CD-CZH000001
     * storeTicket : {"code":"ZKQ201704041602292069","name":"满减","type":"1","key1":200000,"key2":20000,"description":"让我给你姓名一言一行您look哦咯破ing民工why","price":20000,"currency":"QBB","validateStart":"Apr 4, 2017 12:00:00 AM","validateEnd":"Apr 6, 2017 12:00:00 AM","createDatetime":"Apr 4, 2017 4:02:29 PM","status":"1","storeCode":"SJ201704041559165775","companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     * store : {"code":"SJ201704041559165775","name":"公益行商家","level":"2","type":"FL201703301952165460","slogan":"过敏共鸣哦嘻嘻嘻XP一起我","advPic":"ANDROID_1491292374197_459_816.jpg","pic":"ANDROID_1491292758800_144_192.jpg","description":"您whyXP心你好也行名字一名嘻嘻YY你移民嘻嘻嘻嘻嘻嘻晚自习嘻嘻嘻嘻嘻嘻","province":"浙江省","city":"杭州市","area":"余杭区","address":"梦想战神","longitude":"119.998089","latitude":"30.38812","bookMobile":"18177777777","smsMobile":"18177777777","uiLocation":"1","uiOrder":"1","legalPersonName":"工艺","userReferee":"U2017040415165912175","rate1":0.01,"rate2":0.1,"rate3":0,"isDefault":"0","status":"2","updater":"admin","updateDatetime":"Apr 4, 2017 4:00:08 PM","owner":"U2017040415521614218","totalRmbNum":0,"totalJfNum":0,"totalDzNum":0,"totalScNum":0,"companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     */

    private String code;
    private String userId;
    private String ticketCode;
    private String createDatetime;
    private String status;
    private String systemCode;
    /**
     * code : ZKQ201704041602292069
     * name : 满减
     * type : 1
     * key1 : 200000
     * key2 : 20000
     * description : 让我给你姓名一言一行您look哦咯破ing民工why
     * price : 20000
     * currency : QBB
     * validateStart : Apr 4, 2017 12:00:00 AM
     * validateEnd : Apr 6, 2017 12:00:00 AM
     * createDatetime : Apr 4, 2017 4:02:29 PM
     * status : 1
     * storeCode : SJ201704041559165775
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private StoreTicketBean storeTicket;
    /**
     * code : SJ201704041559165775
     * name : 公益行商家
     * level : 2
     * type : FL201703301952165460
     * slogan : 过敏共鸣哦嘻嘻嘻XP一起我
     * advPic : ANDROID_1491292374197_459_816.jpg
     * pic : ANDROID_1491292758800_144_192.jpg
     * description : 您whyXP心你好也行名字一名嘻嘻YY你移民嘻嘻嘻嘻嘻嘻晚自习嘻嘻嘻嘻嘻嘻
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * address : 梦想战神
     * longitude : 119.998089
     * latitude : 30.38812
     * bookMobile : 18177777777
     * smsMobile : 18177777777
     * uiLocation : 1
     * uiOrder : 1
     * legalPersonName : 工艺
     * userReferee : U2017040415165912175
     * rate1 : 0.01
     * rate2 : 0.1
     * rate3 : 0
     * isDefault : 0
     * status : 2
     * updater : admin
     * updateDatetime : Apr 4, 2017 4:00:08 PM
     * owner : U2017040415521614218
     * totalRmbNum : 0
     * totalJfNum : 0
     * totalDzNum : 0
     * totalScNum : 0
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private StoreBean store;

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

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
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

    public StoreTicketBean getStoreTicket() {
        return storeTicket;
    }

    public void setStoreTicket(StoreTicketBean storeTicket) {
        this.storeTicket = storeTicket;
    }

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public static class StoreTicketBean {
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

    public static class StoreBean {
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
        private String uiLocation;
        private String uiOrder;
        private String legalPersonName;
        private String userReferee;
        private double rate1;
        private double rate2;
        private double rate3;
        private String isDefault;
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

        public String getUiLocation() {
            return uiLocation;
        }

        public void setUiLocation(String uiLocation) {
            this.uiLocation = uiLocation;
        }

        public String getUiOrder() {
            return uiOrder;
        }

        public void setUiOrder(String uiOrder) {
            this.uiOrder = uiOrder;
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

        public double getRate3() {
            return rate3;
        }

        public void setRate3(double rate3) {
            this.rate3 = rate3;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
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
