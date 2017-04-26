package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import java.text.DecimalFormat;

/**
 * Created by dell1 on 2016/12/20.
 */

public class MoneyUtil {

    public static String moneyFormatDouble(double money){
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format((money/1000));

        return showMoney.substring(0,showMoney.length()-1);
    }

    public static String getCurrency(String currency){
        switch (currency){
            case "CNY":
                return "人民币";

            case "FRB":
                return "分润";

            case "HBB":
                return "红包";

            case "GWB":
                return "购物币";

            case "QBB":
                return "钱包币";

            case "GXJL":
                return "贡献奖励";

            case "HBYJ":
                return "红包业绩";
        }
        return "";
    }

}
