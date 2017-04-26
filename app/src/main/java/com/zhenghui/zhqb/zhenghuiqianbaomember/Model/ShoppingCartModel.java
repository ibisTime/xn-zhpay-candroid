package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by dell1 on 2016/12/15.
 */

public class ShoppingCartModel {


    /**
     * code : GW201703291941494563
     * userId : U2017032913574410381
     * productCode : CP201703291727333540
     * quantity : 1
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     * product : {"code":"CP201703291727333540","name":"we bare bears","advPic":"ANDROID_1490779593774_612_344.jpg","price1":10000,"price2":10000,"price3":10000,"companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     */

    private String code;
    private String userId;
    private String productCode;
    private int quantity;
    private String companyCode;
    private String systemCode;
    /**
     * code : CP201703291727333540
     * name : we bare bears
     * advPic : ANDROID_1490779593774_612_344.jpg
     * price1 : 10000
     * price2 : 10000
     * price3 : 10000
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private ProductBean product;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }

    public static class ProductBean {
        private String code;
        private String name;
        private String advPic;
        private double price1;
        private Integer price2;
        private Integer price3;
        private String companyCode;
        private String systemCode;
        private boolean isChoose = false;

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
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

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
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
