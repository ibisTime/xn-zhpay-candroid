package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dell1 on 2016/12/17.
 */

public class OrderStatusUtil {


    public static String getOrderStatus(String code){

        JSONObject object = new JSONObject();
        String status = "";

        try {
            object.put("1","待支付");
            object.put("2","待发货");
            object.put("3","待收货");
            object.put("4","已收货");
            object.put("91","用户取消");
            object.put("92","商户取消");
            object.put("93","快递异常");

            status =  object.get(code).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status;
    }

}
