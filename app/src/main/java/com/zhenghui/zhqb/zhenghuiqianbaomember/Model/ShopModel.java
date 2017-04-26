package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.util.List;

/**
 * Created by dell1 on 2016/12/19.
 */

public class ShopModel {


    /**
     * code : SJ201701031437067246
     * name : ios测试
     * level : 2
     * type : 2
     * legalPersonName : 田磊
     * userReferee : 18767101909
     * rate1 : 10.0
     * rate2 : 10.0
     * slogan : 好的
     * adPic : IOS_1483425431406767_1280_950.jpg
     * pic : IOS_1483425444916138_950_1280.jpg
     * description : 你们好啊
     * province : 河南省
     * city : 平顶山市
     * area : 卫东区
     * address : 666号
     * longitude : 120.00076055720312
     * latitude : 30.28846874728093
     * bookMobile : 15737935617
     * status : 2
     * updateDatetime : Jan 3, 2017 2:37:06 PM
     * owner : U2017010314210015658
     * totalJfNum : 0.0
     * totalDzNum : 0.0
     * systemCode : CD-CZH000001
     * storeTickets : [{"code":"ZKQ201701052053127929","name":"iOS","type":"1","key1":100000,"key2":30000,"description":"1221221","price":30000,"currency":"CNY","validateStart":"Jan 5, 2017 12:00:00 AM","validateEnd":"May 9, 2017 11:59:59 PM","createDatetime":"Jan 5, 2017 8:53:12 PM","status":"1","storeCode":"SJ201701031437067246","systemCode":"CD-CZH000001"},{"code":"ZKQ201701052055232041","name":"Iosios--2-2","type":"1","key1":100000,"key2":10000,"description":"Second zqq","price":10000,"currency":"CNY","validateStart":"Jan 5, 2017 12:00:00 AM","validateEnd":"Mar 5, 2017 11:59:59 PM","createDatetime":"Jan 5, 2017 8:55:23 PM","status":"1","storeCode":"SJ201701031437067246","systemCode":"CD-CZH000001"},{"code":"ZKQ201701052111384742","name":"iOS zhen","key1":100000,"key2":50000,"description":"1111---zhekouquan","price":50000,"currency":"CNY","validateStart":"Jan 5, 2017 12:00:00 AM","validateEnd":"Jan 26, 2017 11:59:59 PM","createDatetime":"Jan 5, 2017 9:11:38 PM","status":"1","storeCode":"SJ201701031437067246","systemCode":"CD-CZH000001"}]
     * distance : 74
     */

    private String code;
    private String name;
    private String level;
    private String type;
    private String legalPersonName;
    private String userReferee;
    private double rate1;
    private double rate2;
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
    private String status;
    private String updateDatetime;
    private String owner;
    private double totalJfNum;
    private double totalDzNum;
    private String systemCode;
    private String distance;
    /**
     * code : ZKQ201701052053127929
     * name : iOS
     * type : 1
     * key1 : 100000.0
     * key2 : 30000.0
     * description : 1221221
     * price : 30000.0
     * currency : CNY
     * validateStart : Jan 5, 2017 12:00:00 AM
     * validateEnd : May 9, 2017 11:59:59 PM
     * createDatetime : Jan 5, 2017 8:53:12 PM
     * status : 1
     * storeCode : SJ201701031437067246
     * systemCode : CD-CZH000001
     */

    private List<StoreTicketsBean> storeTickets;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String adPic) {
        this.advPic = adPic;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public double getTotalJfNum() {
        return totalJfNum;
    }

    public void setTotalJfNum(double totalJfNum) {
        this.totalJfNum = totalJfNum;
    }

    public double getTotalDzNum() {
        return totalDzNum;
    }

    public void setTotalDzNum(double totalDzNum) {
        this.totalDzNum = totalDzNum;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<StoreTicketsBean> getStoreTickets() {
        return storeTickets;
    }

    public void setStoreTickets(List<StoreTicketsBean> storeTickets) {
        this.storeTickets = storeTickets;
    }

    public static class StoreTicketsBean {
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
}
