package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell1 on 2016/12/15.
 */

public class GoodsModel implements Serializable {


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
     * store : {"code":"SJ2017060416155036789952","name":"安卓店铺2","level":"2","type":"1","slogan":"广告语","advPic":"ANDROID_1496564031759_580_580.jpg","pic":"ANDROID_1496564058384_580_580.jpg||ANDROID_1496564063708_580_580.jpg||ANDROID_1496564073059_580_580.jpg","description":"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈不哈哈哈哈哈哈哈哈哈那就是就是就是计算机技术就","province":"浙江省","city":"杭州市","area":"余杭区","address":"梦想小镇","longitude":"119.998089","latitude":"30.38812","bookMobile":"1234567","smsMobile":"18984955240","uiLocation":"1","uiOrder":"1","legalPersonName":"雷黔","userReferee":"U2017010713451027748","isDefault":"1","status":"91","updater":"U201706041609037734313","updateDatetime":"Jun 12, 2017 4:42:12 PM","remark":"","createDatetime":"Jun 4, 2017 4:15:50 PM","approveUser":"xman","approveDatetime":"Jun 12, 2017 4:42:31 PM","onUser":"xman","onDatetime":"Jun 12, 2017 1:56:53 PM","offUser":"xman","offDatetime":"Jun 12, 2017 1:56:14 PM","owner":"U201706041609037734313","companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     * productSpecsList : [{"code":"PS2017061211263086532140","name":"规格1","productCode":"CP2017061211263085730915","price1":0,"price2":0,"price3":0,"quantity":99,"province":"浙江省","weight":1,"orderNo":0,"companyCode":"U201706041609037734313","systemCode":"CD-CZH000001"}]
     */

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
     * code : PS2017061211263086532140
     * name : 规格1
     * productCode : CP2017061211263085730915
     * price1 : 0
     * price2 : 0
     * price3 : 0
     * quantity : 99
     * province : 浙江省
     * weight : 1
     * orderNo : 0
     * companyCode : U201706041609037734313
     * systemCode : CD-CZH000001
     */

    private List<ProductSpecsListBean> productSpecsList;

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

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public List<ProductSpecsListBean> getProductSpecsList() {
        return productSpecsList;
    }

    public void setProductSpecsList(List<ProductSpecsListBean> productSpecsList) {
        this.productSpecsList = productSpecsList;
    }

    public static class StoreBean implements Serializable {
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

    public static class ProductSpecsListBean implements Serializable {
        private boolean isSelect;
        private String code;
        private String name;
        private String productCode;
        private double price1;
        private double price2;
        private double price3;
        private int quantity;
        private String province;
        private double weight;
        private int orderNo;
        private String companyCode;
        private String systemCode;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
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

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public double getPrice1() {
            return price1;
        }

        public void setPrice1(double price1) {
            this.price1 = price1;
        }

        public double getPrice2() {
            return price2;
        }

        public void setPrice2(double price2) {
            this.price2 = price2;
        }

        public double getPrice3() {
            return price3;
        }

        public void setPrice3(double price3) {
            this.price3 = price3;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
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
