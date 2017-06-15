package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by dell1 on 2016/12/17.
 */

public class OrderModel {


    /**
     * code : DD2017061213454225660472
     * receiver : 雷黔
     * reMobile : 18984955240
     * reAddress : 浙江省 杭州市 余杭区 梦想小镇
     * applyUser : U201706071406231322783
     * applyNote :
     * applyDatetime : Jun 12, 2017 1:45:42 PM
     * productCode : CP2017061211263085730915
     * productName : 这是一个良心会痛的商品
     * productSpecsCode : PS2017061211263086532140
     * productSpecsName : 规格1
     * quantity : 1
     * price1 : 0
     * price2 : 0
     * price3 : 0
     * amount1 : 0
     * amount2 : 0
     * amount3 : 0
     * yunfei : 0
     * status : 2
     * payType : 1
     * payDatetime : Jun 12, 2017 1:45:52 PM
     * payAmount1 : 0
     * payAmount11 : 0
     * payAmount2 : 0
     * payAmount3 : 0
     * promptTimes : 0
     * logisticsCompany : 0
     * logisticsCode : 0
     * companyCode : U201706041609037734313
     * systemCode : CD-CZH000001
     * store : {"code":"SJ2017060416155036789952","name":"安卓店铺2","level":"2","type":"1","slogan":"广告语","advPic":"ANDROID_1496564031759_580_580.jpg","pic":"ANDROID_1496564058384_580_580.jpg||ANDROID_1496564063708_580_580.jpg||ANDROID_1496564073059_580_580.jpg","description":"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈不哈哈哈哈哈哈哈哈哈那就是就是就是计算机技术就","province":"浙江省","city":"杭州市","area":"余杭区","address":"梦想小镇","longitude":"119.998089","latitude":"30.38812","bookMobile":"1234567","smsMobile":"18984955240","uiLocation":"1","uiOrder":"1","legalPersonName":"雷黔","userReferee":"U2017010713451027748","isDefault":"1","status":"91","updater":"U201706041609037734313","updateDatetime":"Jun 12, 2017 4:42:12 PM","remark":"","createDatetime":"Jun 4, 2017 4:15:50 PM","approveUser":"xman","approveDatetime":"Jun 12, 2017 4:42:31 PM","onUser":"xman","onDatetime":"Jun 12, 2017 1:56:53 PM","offUser":"xman","offDatetime":"Jun 12, 2017 1:56:14 PM","owner":"U201706041609037734313","companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     * product : {"code":"CP2017061211263085730915","category":"FL201700000000000001","type":"FL201700000000000003","name":"这是一个良心会痛的商品","slogan":"广告标语","advPic":"ANDROID_1497237954074_580_580.jpg","pic":"ANDROID_1497237969404_580_580.jpg","description":"哈哈哈哈哈哈哈哈计算机等级你放假烦恼叠加金额u付洋洋我怕热可能性此间少年尿道结石那我呢就","status":"3","updater":"U201706041609037734313","updateDatetime":"Jun 12, 2017 11:26:30 AM","boughtCount":0,"companyCode":"U201706041609037734313","systemCode":"CD-CZH000001"}
     */

    private String code;
    private String receiver;
    private String reMobile;
    private String reAddress;
    private String applyUser;
    private String applyNote;
    private String applyDatetime;
    private String productCode;
    private String productName;
    private String productSpecsCode;
    private String productSpecsName;
    private int quantity;
    private int price1;
    private int price2;
    private int price3;
    private double amount1;
    private double amount2;
    private double amount3;
    private int yunfei;
    private String status;
    private String payType;
    private String payDatetime;
    private int payAmount1;
    private int payAmount11;
    private int payAmount2;
    private int payAmount3;
    private int promptTimes;
    private String logisticsCompany;
    private String logisticsCode;
    private String companyCode;
    private String systemCode;
    /**
     * code : SJ2017060416155036789952
     * name : 安卓店铺2
     * level : 2
     * type : 1
     * slogan : 广告语
     * advPic : ANDROID_1496564031759_580_580.jpg
     * pic : ANDROID_1496564058384_580_580.jpg||ANDROID_1496564063708_580_580.jpg||ANDROID_1496564073059_580_580.jpg
     * description : 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈不哈哈哈哈哈哈哈哈哈那就是就是就是计算机技术就
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * address : 梦想小镇
     * longitude : 119.998089
     * latitude : 30.38812
     * bookMobile : 1234567
     * smsMobile : 18984955240
     * uiLocation : 1
     * uiOrder : 1
     * legalPersonName : 雷黔
     * userReferee : U2017010713451027748
     * isDefault : 1
     * status : 91
     * updater : U201706041609037734313
     * updateDatetime : Jun 12, 2017 4:42:12 PM
     * remark :
     * createDatetime : Jun 4, 2017 4:15:50 PM
     * approveUser : xman
     * approveDatetime : Jun 12, 2017 4:42:31 PM
     * onUser : xman
     * onDatetime : Jun 12, 2017 1:56:53 PM
     * offUser : xman
     * offDatetime : Jun 12, 2017 1:56:14 PM
     * owner : U201706041609037734313
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private StoreBean store;
    /**
     * code : CP2017061211263085730915
     * category : FL201700000000000001
     * type : FL201700000000000003
     * name : 这是一个良心会痛的商品
     * slogan : 广告标语
     * advPic : ANDROID_1497237954074_580_580.jpg
     * pic : ANDROID_1497237969404_580_580.jpg
     * description : 哈哈哈哈哈哈哈哈计算机等级你放假烦恼叠加金额u付洋洋我怕热可能性此间少年尿道结石那我呢就
     * status : 3
     * updater : U201706041609037734313
     * updateDatetime : Jun 12, 2017 11:26:30 AM
     * boughtCount : 0
     * companyCode : U201706041609037734313
     * systemCode : CD-CZH000001
     */

    private ProductBean product;

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSpecsCode() {
        return productSpecsCode;
    }

    public void setProductSpecsCode(String productSpecsCode) {
        this.productSpecsCode = productSpecsCode;
    }

    public String getProductSpecsName() {
        return productSpecsName;
    }

    public void setProductSpecsName(String productSpecsName) {
        this.productSpecsName = productSpecsName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getAmount1() {
        return amount1;
    }

    public void setAmount1(double amount1) {
        this.amount1 = amount1;
    }

    public double getAmount2() {
        return amount2;
    }

    public void setAmount2(double amount2) {
        this.amount2 = amount2;
    }

    public double getAmount3() {
        return amount3;
    }

    public void setAmount3(double amount3) {
        this.amount3 = amount3;
    }

    public int getYunfei() {
        return yunfei;
    }

    public void setYunfei(int yunfei) {
        this.yunfei = yunfei;
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

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public int getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(int payAmount1) {
        this.payAmount1 = payAmount1;
    }

    public int getPayAmount11() {
        return payAmount11;
    }

    public void setPayAmount11(int payAmount11) {
        this.payAmount11 = payAmount11;
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

    public int getPromptTimes() {
        return promptTimes;
    }

    public void setPromptTimes(int promptTimes) {
        this.promptTimes = promptTimes;
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

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
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
        private String isDefault;
        private String status;
        private String updater;
        private String updateDatetime;
        private String remark;
        private String createDatetime;
        private String approveUser;
        private String approveDatetime;
        private String onUser;
        private String onDatetime;
        private String offUser;
        private String offDatetime;
        private String owner;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getApproveUser() {
            return approveUser;
        }

        public void setApproveUser(String approveUser) {
            this.approveUser = approveUser;
        }

        public String getApproveDatetime() {
            return approveDatetime;
        }

        public void setApproveDatetime(String approveDatetime) {
            this.approveDatetime = approveDatetime;
        }

        public String getOnUser() {
            return onUser;
        }

        public void setOnUser(String onUser) {
            this.onUser = onUser;
        }

        public String getOnDatetime() {
            return onDatetime;
        }

        public void setOnDatetime(String onDatetime) {
            this.onDatetime = onDatetime;
        }

        public String getOffUser() {
            return offUser;
        }

        public void setOffUser(String offUser) {
            this.offUser = offUser;
        }

        public String getOffDatetime() {
            return offDatetime;
        }

        public void setOffDatetime(String offDatetime) {
            this.offDatetime = offDatetime;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
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

    public static class ProductBean {
        private String code;
        private String category;
        private String type;
        private String name;
        private String slogan;
        private String advPic;
        private String pic;
        private String description;
        private String status;
        private String updater;
        private String updateDatetime;
        private int boughtCount;
        private String companyCode;
        private String systemCode;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
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

        public int getBoughtCount() {
            return boughtCount;
        }

        public void setBoughtCount(int boughtCount) {
            this.boughtCount = boughtCount;
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
