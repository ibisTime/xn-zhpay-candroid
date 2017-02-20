package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/1/6.
 */

public class DiscountModel implements Serializable {


    /**
     * code : UT201701171644446304
     * userId : U2017011704412811088
     * ticketCode : ZKQ201701171616540220
     * createDatetime : Jan 17, 2017 4:44:44 PM
     * status : 0
     * systemCode : CD-CZH000001
     * ticketName : ios测试
     * ticketType : 1
     * ticketKey1 : 100000
     * ticketKey2 : 50000
     * storeTicket : {"code":"ZKQ201701171616540220","name":"ios测试","type":"1","key1":100000,"key2":50000,"description":"听一下咯摸摸摸摸哦哦弄KKK啦啊摸摸摸莫总摸摸摸莫总我摸摸摸呕吐他咯哦full哦哦哦咯Mook咯哦哦咯摩托弄one摩托","price":50000,"currency":"QBB","validateStart":"Jan 17, 2017 12:00:00 AM","validateEnd":"Jan 21, 2017 11:59:59 PM","createDatetime":"Jan 17, 2017 4:16:54 PM","status":"1","storeCode":"SJ201701170429427443","systemCode":"CD-CZH000001"}
     * store : {"code":"SJ201701170429427443","name":"ios店铺","type":"3","legalPersonName":"田磊","userReferee":"U2017010713451027748","rate1":0.99,"rate2":0.95,"slogan":"隐隐约约","adPic":"IOS_1484486218974364_1280_950.jpg","pic":"IOS_1484486219145374_1280_950.jpg||IOS_1484486219181269_950_1280.jpg","description":"恍恍惚惚恍恍惚惚哈哈哈这点上班路公交车专用道","province":"浙江省","city":"湖州市","area":"长兴县","address":"余杭哪里","longitude":"120.00069604342106","latitude":"30.28754643010961","bookMobile":"13868074590","status":"2","approver":"admin","approveDatetime":"Jan 17, 2017 6:57:01 AM","updateDatetime":"Jan 17, 2017 6:59:36 AM","remark":"bu tong guo","owner":"U201701170427197658","totalJfNum":0,"totalDzNum":0,"systemCode":"CD-CZH000001"}
     */

    private String code;
    private String userId;
    private String ticketCode;
    private String createDatetime;
    private String status;
    private String systemCode;
    private String ticketName;
    private String ticketType;
    private int ticketKey1;
    private int ticketKey2;
    /**
     * code : ZKQ201701171616540220
     * name : ios测试
     * type : 1
     * key1 : 100000
     * key2 : 50000
     * description : 听一下咯摸摸摸摸哦哦弄KKK啦啊摸摸摸莫总摸摸摸莫总我摸摸摸呕吐他咯哦full哦哦哦咯Mook咯哦哦咯摩托弄one摩托
     * price : 50000
     * currency : QBB
     * validateStart : Jan 17, 2017 12:00:00 AM
     * validateEnd : Jan 21, 2017 11:59:59 PM
     * createDatetime : Jan 17, 2017 4:16:54 PM
     * status : 1
     * storeCode : SJ201701170429427443
     * systemCode : CD-CZH000001
     */

    private StoreTicketBean storeTicket;
    /**
     * code : SJ201701170429427443
     * name : ios店铺
     * type : 3
     * legalPersonName : 田磊
     * userReferee : U2017010713451027748
     * rate1 : 0.99
     * rate2 : 0.95
     * slogan : 隐隐约约
     * adPic : IOS_1484486218974364_1280_950.jpg
     * pic : IOS_1484486219145374_1280_950.jpg||IOS_1484486219181269_950_1280.jpg
     * description : 恍恍惚惚恍恍惚惚哈哈哈这点上班路公交车专用道
     * province : 浙江省
     * city : 湖州市
     * area : 长兴县
     * address : 余杭哪里
     * longitude : 120.00069604342106
     * latitude : 30.28754643010961
     * bookMobile : 13868074590
     * status : 2
     * approver : admin
     * approveDatetime : Jan 17, 2017 6:57:01 AM
     * updateDatetime : Jan 17, 2017 6:59:36 AM
     * remark : bu tong guo
     * owner : U201701170427197658
     * totalJfNum : 0
     * totalDzNum : 0
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

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public int getTicketKey1() {
        return ticketKey1;
    }

    public void setTicketKey1(int ticketKey1) {
        this.ticketKey1 = ticketKey1;
    }

    public int getTicketKey2() {
        return ticketKey2;
    }

    public void setTicketKey2(int ticketKey2) {
        this.ticketKey2 = ticketKey2;
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

    public static class StoreTicketBean implements Serializable {
        private String code;
        private String name;
        private String type;
        private int key1;
        private int key2;
        private String description;
        private int price;
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

        public int getKey1() {
            return key1;
        }

        public void setKey1(int key1) {
            this.key1 = key1;
        }

        public int getKey2() {
            return key2;
        }

        public void setKey2(int key2) {
            this.key2 = key2;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
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

    public static class StoreBean implements Serializable {
        private String code;
        private String name;
        private String type;
        private String legalPersonName;
        private String userReferee;
        private double rate1;
        private double rate2;
        private String slogan;
        private String adPic;
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
        private String approver;
        private String approveDatetime;
        private String updateDatetime;
        private String remark;
        private String owner;
        private int totalJfNum;
        private int totalDzNum;
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

        public String getAdPic() {
            return adPic;
        }

        public void setAdPic(String adPic) {
            this.adPic = adPic;
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

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
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

        public String getSystemCode() {
            return systemCode;
        }

        public void setSystemCode(String systemCode) {
            this.systemCode = systemCode;
        }
    }
}
