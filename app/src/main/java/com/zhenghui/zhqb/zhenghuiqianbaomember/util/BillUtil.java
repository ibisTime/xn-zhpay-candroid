package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

public class BillUtil {


    /**
     * '11','充值'
     '-11','取现'
     '19','蓝补'
     '-19','红冲'
     '-30','购物'
     '30','购物退款'
     '32','确认收货，商户收钱'
     '-40','购买折扣券'
     '-ZH1','正汇O2O支付'
     '-ZH2','正汇分红权分红'
     '-50','购买汇赚宝'
     '51','购买汇赚宝分成'
     '52','汇赚宝摇一摇奖励'
     '53','摇一摇分成'
     '60','发一发得红包'
     '61','领取红包'
     '-70','参与小目标'
     '71','小目标中奖'

     '200','币种兑换'

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
        } else if(bizType.equals("32")){
            return "确认收货，商户收钱";
        } else if(bizType.equals("-40")){
            return "购买折扣券";
        } else if(bizType.equals("-50")){
            return "购买汇赚宝";
        } else if(bizType.equals("51")){
            return "购买汇赚宝分成";
        }else if(bizType.equals("52")){
            return "汇赚宝摇一摇奖励";
        } else if(bizType.equals("53")){
            return "汇赚宝主人摇一摇分成";
        } else if(bizType.equals("54")){
            return "推荐人摇一摇分成";
        } else if(bizType.equals("60")){
            return "发一发得红包";
        } else if(bizType.equals("61")){
            return "领取红包";
        }else if(bizType.equals("-70")){
            return "参与小目标";
        }else if(bizType.equals("71")){
            return "小目标中奖";
        }else if(bizType.equals("200")){
            return "币种兑换";
        }else if(bizType.equals("-ZH1")){
            return "正汇O2O支付";
        }else if(bizType.equals("-ZH2")){
            return "正汇分红权分红";
        }else if(bizType.equals("206")){
            return "C端用户间转账";
        }
        return "";
    }
}
