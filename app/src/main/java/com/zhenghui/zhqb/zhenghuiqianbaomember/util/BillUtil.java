package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

/**
 * Created by LeiQ on 2017/1/7.
 */

public class BillUtil {


    /**
     * AJ_CZ("11", "充值"),
     * AJ_QX("-11", "取现"),
     * AJ_LB("19", "蓝补"),
     * AJ_HC("-19", "红冲"),
     * AJ_GW("-30", "购物"),
     * AJ_DPXF("-31", "店铺消费"),
     * AJ_GMZKQ("-32", "购买折扣券"),
     * AJ_GMFLYK("-33", "购买福利月卡"),
     * AJ_FLYKFC("34","福利月卡分成"),
     * AJ_FLYKHH("35", "福利月卡返还"),
     * AJ_GMHZB("-36", "购买汇赚宝"),
     * AJ_GMHZBFC("37", "购买汇赚宝分成"),
     * AJ_YYJL("38", "汇赚宝摇一摇奖励"),
     * AJ_YYFC("39", "摇一摇分成"),
     * AJ_DUOBAO("-40", "一元夺宝"),
     * AJ_DBFLOW("41", "夺宝流标"),
     * AJ_QRSH("42", "确认收货，商户收钱"),
     * AJ_HB2FR("50", "红包兑分润"),
     * AJ_HBYJ2FR("52", "红包业绩兑分润"),
     * AJ_HBYJ2GXJL("54", "红包业绩兑贡献奖励"),
     * AJ_FR2RMB("56","分润兑人民币"),
     * AJ_GXJL2RMB("58", "贡献奖励兑人民币");
     * @param bizType
     * @return
     */

    public static String getBillType(String bizType){

        if(bizType.equals("11")){
            return "充值";
        } else if(bizType.equals("-11")){
            return "取现";
        } else if(bizType.equals("19")){
            return "蓝补";
        } else if(bizType.equals("-19")){
            return "红冲";
        } else if(bizType.equals("-30")){
            return "购物";
        } else if(bizType.equals("30")){
            return "购物退款";
        } else if(bizType.equals("-31")){
            return "店铺消费";
        } else if(bizType.equals("-32")){
            return "购买折扣券";
        } else if(bizType.equals("32")){
            return "销售折扣券";
        } else if(bizType.equals("-33")){
            return "购买福利月卡";
        } else if(bizType.equals("34")){
            return "福利月卡分成";
        } else if(bizType.equals("35")){
            return "福利月卡返还";
        } else if(bizType.equals("-36")){
            return "购买汇赚宝";
        } else if(bizType.equals("37")){
            return "购买汇赚宝分成";
        } else if(bizType.equals("38")){
            return "汇赚宝摇一摇奖励";
        } else if(bizType.equals("39")){
            return "摇一摇分成";
        } else if(bizType.equals("-40")){
            return "一元夺宝";
        } else if(bizType.equals("41")){
            return "夺宝流标";
        } else if(bizType.equals("42")) {
            return "确认收货，商户收钱";
        } else if(bizType.equals("50")){
            return "红包兑分润";
        } else if(bizType.equals("52")){
            return "红包业绩兑分润";
        } else if(bizType.equals("54")){
            return "红包业绩兑贡献奖励";
        } else if(bizType.equals("56")){
            return "分润兑人民币";
        } else if(bizType.equals("58")){
            return "贡献奖励兑人民币";
        }
        return "";
    }
}
