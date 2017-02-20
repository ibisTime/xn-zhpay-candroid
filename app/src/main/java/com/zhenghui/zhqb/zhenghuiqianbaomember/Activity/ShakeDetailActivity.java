package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShakeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ConstantsUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShakeDetailActivity extends MyBaseActivity {


    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_distance)
    TextView txtDistance;
    @InjectView(R.id.web_ad)
    WebView webView;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_award)
    TextView txtAward;
    @InjectView(R.id.layout_award)
    LinearLayout layoutAward;
    @InjectView(R.id.txt_coin)
    TextView txtCoin;

    public static ShakeDetailActivity instance;
    private ShakeModel model;

    // 微信分享实例
    private IWXAPI api;

    private SharedPreferences userInfoSp;

    private String type;
    private String quantity;

    String webURL = "http://www.baidu.com";
    private WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_detail);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inis() {
        instance = this;

        api = WXAPIFactory.createWXAPI(this, ConstantsUtil.APP_ID_WX);

        model = (ShakeModel) getIntent().getSerializableExtra("model");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        setView();


    }

    private void setView() {
        txtName.setText(model.getMobile() + "的");
        if (model.getDistance().length() > 3) {
            txtDistance.setText((Integer.parseInt(model.getDistance()) / 1000) + "KM");
        } else {
            txtDistance.setText(model.getDistance() + "M");
        }
        if (model.getShareUrl() != null) {
            if (model.getShareUrl().indexOf("http://") != -1) {
                webURL = model.getShareUrl();
            } else {
                webURL = "http://" + model.getShareUrl();
            }
        }
        System.out.println("webURL=" + webURL);
        initWebView();
    }

    private void initWebView() {

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            webView.getSettings().setLoadsImagesAutomatically(false);
        }


        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.loadUrl(webURL);

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);

        //JS
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDomStorageEnabled(true);
        setupWebViewClient();

    }

    private void setupWebViewClient() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!view.getSettings().getLoadsImagesAutomatically()) {
                    view.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        };
        webView.setWebViewClient(mWebViewClient);
    }

    @OnClick({R.id.layout_back, R.id.txt_confirm, R.id.layout_award})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_confirm:
                if (WxUtil.check(this)) {
                    share();
                }
                break;

            case R.id.layout_award:
                finish();
                ShakeListActivity.instance.finish();
                break;
        }
    }


    private void share() {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webURL;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "摇一摇";
        msg.description = "摇一摇拿大奖啦";

        try {
            Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
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
     * 构造一个用于请求的唯一标识
     *
     * @param type 分享的内容类型
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public void getAward() {

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();

        if (DEVICE_ID == null) {
            DEVICE_ID = "error_noDeviceId";
        }

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("deviceNo", DEVICE_ID);
            object.put("hzbHoldId", model.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808460", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    type = jsonObject.getString("type");
                    quantity = jsonObject.getString("quantity");

                    setAward();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                finish();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShakeDetailActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShakeDetailActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAward() {
        layoutAward.setVisibility(View.VISIBLE);
        if (type.equals("1")) {
            txtCoin.setText("个钱包币");
            txtAward.setText(quantity + "");
        } else if (type.equals("2")) {
            txtCoin.setText("个购物币");
            txtAward.setText(quantity + "");
        } else if (type.equals("3")) {
            txtCoin.setText("个红包");
            txtAward.setText(quantity + "");
        }
    }

}
