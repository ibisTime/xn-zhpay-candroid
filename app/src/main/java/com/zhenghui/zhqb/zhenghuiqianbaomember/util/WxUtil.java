package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;


public class WxUtil {

    private static IWXAPI api;

    public static IWXAPI registToWx(Context context){
        api = WXAPIFactory.createWXAPI(context, Constants.APP_ID_WX, false);
        api.registerApp(Constants.APP_ID_WX);

        return api;
    }

    /**
     *  检测是否有微信与是否支持微信支付
     * @return
     */
    public static boolean check(Context context) {

        api = registToWx(context);

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        boolean isPaySupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if(!api.isWXAppInstalled())
        {
            Toast.makeText(context,"没有安装微信，不能分享到朋友圈",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!api.isWXAppSupportAPI())
        {
            Toast.makeText(context,"你使用的微信版本不支持微信支付！",Toast.LENGTH_SHORT).show();
            return false;
        }
        return isPaySupported;
    }

    /**
     *  分享到朋友圈
     * @param
     */
    public static void shareToPYQ(Context context, String url, String title, String description) {
        System.out.println("shareURL="+url);
        api = registToWx(context);

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     *  分享微信聊天界面
     * @param
     */
    public static void shareToWX(Context context, String url, String title, String description) {
        System.out.println("shareURL="+url);
        api = registToWx(context);

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp1, 100, 100, true);
            msg.thumbData = Bitmap2Bytes(thumbBmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("图文链接");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 微信 支付
     * @param object
     * @return
     */
    public static void pay(Context context,JSONObject object){

        api = registToWx(context);

        String sign = "";
        String appid = "";
        String noncestr = "";
        String timestamp = "";
        String partnerid = "";
        String prepayid = "";
        String wechatPackage ="";

        try {
            sign = object.getString("sign");
            appid = object.getString("appId");
            prepayid = object.getString("prepayId");
            noncestr = object.getString("nonceStr");
            timestamp = object.getString("timeStamp");
            partnerid = object.getString("partnerid");
            wechatPackage = object.getString("wechatPackage");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PayReq req = new PayReq();
        req.sign = sign;
        req.appId = appid;
        req.nonceStr = noncestr;
        req.prepayId = prepayid;
        req.partnerId = partnerid;
        req.timeStamp = timestamp;
        req.packageValue = wechatPackage;

        api.sendReq(req);

    }

    /**
     * 微信 支付签名
     * @param params
     * @return
     */
    public static String createSign(Map<String,String> params){
        Set<String> keysSet = params.keySet();
        //对参数进行排序
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        //签名参数字符串
        String signStr = "";
        int index = 0;
        for (Object key : keys){
            if(key.equals("sign"))
                continue;
            String value = params.get(key);
            signStr += key + "=" + value;
            index ++ ;
            if(keys.length > index)
                signStr += "&";
        }
        String sign = signStr + "&key=7fx5r0n7j8k1tvnzpn55c3ef0zo9a7be";
        return MD5Util.MD5Encode(sign, "utf-8").toUpperCase();
    }

}
