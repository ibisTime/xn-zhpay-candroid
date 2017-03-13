package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/2/23.
 */

public class MyJewelHistoreModel implements Serializable {


    /**
     * userId : U2017022215423852237
     * jewelCode : J201702221611340432
     * jewel : {"code":"J201702221611340432","templateCode":"JT201702212000179939","periods":3,"currency":"FRB","amount":10000,"totalNum":100,"price":10000,"maxInvestNum":1000,"advText":"宣传文字","advPic":"宣传图","investNum":18,"createDatetime":"Feb 22, 2017 4:11:34 PM","status":"0"}
     * myInvestTimes : 2
     */

    private String userId;
    private String jewelCode;
    /**
     * code : J201702221611340432
     * templateCode : JT201702212000179939
     * periods : 3
     * currency : FRB
     * amount : 10000
     * totalNum : 100
     * price : 10000
     * maxInvestNum : 1000
     * advText : 宣传文字
     * advPic : 宣传图
     * investNum : 18
     * createDatetime : Feb 22, 2017 4:11:34 PM
     * status : 0
     */

    private TargetModel jewel;
    private String myInvestTimes;

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

    public TargetModel getJewel() {
        return jewel;
    }

    public void setJewel(TargetModel jewel) {
        this.jewel = jewel;
    }

    public String getMyInvestTimes() {
        return myInvestTimes;
    }

    public void setMyInvestTimes(String myInvestTimes) {
        this.myInvestTimes = myInvestTimes;
    }
}
