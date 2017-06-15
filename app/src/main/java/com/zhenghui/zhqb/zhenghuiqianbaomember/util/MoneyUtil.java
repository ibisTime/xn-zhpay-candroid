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

    public static String moneyFormatPrice(double rmb, double gwb, double qbb){

        String s1,s2,s3;

        if(rmb == 0){
            s1 = "";
            if(gwb == 0){
                s2 = "";
                if(qbb == 0){
                    s3 = "";
                }else {
                    s3 = moneyFormatDouble(qbb)+"钱包币";
                }
            }else {
                s2 = moneyFormatDouble(gwb)+"购物币";
                if(qbb == 0){
                    s3 = "";
                }else {
                    s3 = "+"+moneyFormatDouble(qbb)+"钱包币";
                }
            }
        }else {
            s1 = moneyFormatDouble(rmb)+"人民币";
            if(gwb == 0){
                s2= "";
                if(qbb == 0){
                    s3 = "";
                }else {
                    s3 = "+"+moneyFormatDouble(qbb)+"钱包币";
                }
            }else {
                s2= "+"+moneyFormatDouble(gwb)+"购物币";
                if(qbb == 0){
                    s3 = "";
                }else {
                    s3 = "+"+moneyFormatDouble(qbb)+"钱包币";
                }
            }
        }

        if(rmb == 0 && gwb == 0 && qbb == 0){
            s1 = "0";
        }

        return s1+s2+s3;
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
