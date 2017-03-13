package com.zhenghui.zhqb.zhenghuiqianbaomember.Model;

/**
 * Created by LeiQ on 2017/2/23.
 */

public class GiveModel {

    /**
     * code : HM201702230010000608702
     * advTitle : 小小心意
     * owner : U2017022215423852237
     * ownerCurrency : HBYJ
     * ownerAmount : 5000000
     * receiveCurrency : GXJL
     * receiveAmount : 5000000
     * receiver
     * receiverMobile
     * receiveDatetime
     * status : 0
     * createDatetime : Feb 23, 2017 12:00:00 AM
     */

    private String code;
    private String advTitle;
    private String owner;
    private String ownerCurrency;
    private int ownerAmount;
    private String receiveCurrency;
    private int receiveAmount;
    private String receiver;
    private String receiverMobile;
    private String receiveDatetime;
    private String status;
    private String createDatetime;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiveDatetime() {
        return receiveDatetime;
    }

    public void setReceiveDatetime(String receiveDatetime) {
        this.receiveDatetime = receiveDatetime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAdvTitle() {
        return advTitle;
    }

    public void setAdvTitle(String advTitle) {
        this.advTitle = advTitle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerCurrency() {
        return ownerCurrency;
    }

    public void setOwnerCurrency(String ownerCurrency) {
        this.ownerCurrency = ownerCurrency;
    }

    public int getOwnerAmount() {
        return ownerAmount;
    }

    public void setOwnerAmount(int ownerAmount) {
        this.ownerAmount = ownerAmount;
    }

    public String getReceiveCurrency() {
        return receiveCurrency;
    }

    public void setReceiveCurrency(String receiveCurrency) {
        this.receiveCurrency = receiveCurrency;
    }

    public int getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(int receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }
}
