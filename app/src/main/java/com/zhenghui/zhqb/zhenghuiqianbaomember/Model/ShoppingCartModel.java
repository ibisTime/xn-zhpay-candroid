package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.util.List;

/**
 * Created by dell1 on 2016/12/15.
 */

public class ShoppingCartModel {


    /**
     * pageNO : 1.0
     * start : 0.0
     * pageSize : 10.0
     * totalCount : 2.0
     * totalPage : 1.0
     * list : [{"code":"GW201612151941205179","userId":"U2016121519373971225","productCode":"CP201612141643503168","quantity":1,"advPic":"广告图","productName":"胶囊咖啡机2"},{"code":"GW201612151938060638","userId":"U2016121519373971225","productCode":"CP201612141529322276","quantity":2,"advPic":"广告图","productName":"胶囊咖啡机update","price1":20,"price2":10,"price3":10}]
     */

    private double pageNO;
    private double start;
    private double pageSize;
    private double totalCount;
    private double totalPage;
    /**
     * code : GW201612151941205179
     * userId : U2016121519373971225
     * productCode : CP201612141643503168
     * quantity : 1.0
     * advPic : 广告图
     * productName : 胶囊咖啡机2
     */

    private List<ListBean> list;

    public double getPageNO() {
        return pageNO;
    }

    public void setPageNO(double pageNO) {
        this.pageNO = pageNO;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public double getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(double totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(double totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String code;
        private String userId;
        private String productCode;
        private double quantity;
        private String advPic;
        private String productName;
        private double price1;
        private Integer price2;
        private Integer price3;
        private boolean isChoose = false;

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

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getPrice1() {
            return price1;
        }

        public void setPrice1(double price1) {
            this.price1 = price1;
        }

        public Integer getPrice2() {
            return price2;
        }

        public void setPrice2(Integer price2) {
            this.price2 = price2;
        }

        public Integer getPrice3() {
            return price3;
        }

        public void setPrice3(Integer price3) {
            this.price3 = price3;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }
    }
}
