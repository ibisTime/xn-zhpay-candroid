package com.zhenghui.zhqb.zhenghuiqianbaomember.wxapi;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MyBaseActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShakeDetailActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ConstantsUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends MyBaseActivity implements IWXAPIEventHandler {
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

	private SharedPreferences wxShareSp;
	private SharedPreferences userInfoSp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        initWx();
        inits();


    }

    private void initWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX, false);
		api.handleIntent(getIntent(), this);
    }

    private void inits() {

    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq baseReq) {
		System.out.print("onReq.getType()="+baseReq.getType());
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		wxShareSp = getSharedPreferences("wxShare",Context.MODE_PRIVATE);
		userInfoSp = getSharedPreferences("userInfo",Context.MODE_PRIVATE);

        System.out.print("resp.getType()="+resp.getType());

		if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {// 分享
			String result = "";
			System.out.println("resp.errCode="+resp.errCode);
			switch (resp.errCode) {

				case BaseResp.ErrCode.ERR_OK:
					result = "分享成功";

					try {
						switch (wxShareSp.getString("shareWay","")){

							case "shake":
								ShakeDetailActivity.instance.getAward();
								break;

							case "give":
								give();
								break;

						}

					}catch (Exception e){
						e.printStackTrace();
					}


					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result = "分享取消";
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					result = "分享拒绝";
					break;
				default:
					result = "分享失败";
					break;
			}
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();
			return;
		}

	}

	/**
	 * 发送红包
	 */
	public void give() {
		JSONObject object = new JSONObject();
		try {
			object.put("tokne", userInfoSp.getString("tokne", ""));
			object.put("code", wxShareSp.getString("giveCode", ""));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		new Xutil().post("615130", object.toString(), new Xutil.XUtils3CallBackPost() {
			@Override
			public void onSuccess(String result) {
				Toast.makeText(WXEntryActivity.this, "红包发送成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTip(String tip) {

				Toast.makeText(WXEntryActivity.this, tip, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(String error, boolean isOnCallback) {
				Toast.makeText(WXEntryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
			}
		});
	}

}