package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/2/9.
 */

public class EvaluateModel {


    /**
     * code : HD201704051233004431
     * type : 1
     * actionUser : U2017032820273497120
     * actionDatetime : Apr 5, 2017 12:33:00 PM
     * storeCode : SJ201703311944099424
     * systemCode : CD-CCG000007
     * user : {"userId":"U2017032820273497120","loginName":"18868824532","nickname":"73497120","mobile":"18868824534","identityFlag":"0"}
     */

    private String code;
    private String type;
    private String actionUser;
    private String actionDatetime;
    private String storeCode;
    private String systemCode;
    /**
     * userId : U2017032820273497120
     * loginName : 18868824532
     * nickname : 73497120
     * mobile : 18868824534
     * identityFlag : 0
     */

    private UserBean user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActionUser() {
        return actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
    }

    public String getActionDatetime() {
        return actionDatetime;
    }

    public void setActionDatetime(String actionDatetime) {
        this.actionDatetime = actionDatetime;
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

}
