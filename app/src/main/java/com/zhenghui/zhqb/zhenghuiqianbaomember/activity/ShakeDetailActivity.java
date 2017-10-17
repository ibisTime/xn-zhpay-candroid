package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShakeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615120;

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
    @InjectView(R.id.img_QRCode)
    ImageView imgQRCode;

    public static ShakeDetailActivity instance;

    private ShakeModel model;

    // 微信分享实例
    private IWXAPI api;

    private SharedPreferences userInfoSp;

    private String type;
    private String quantity;

    private String shareURL;
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

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX);

        model = (ShakeModel) getIntent().getSerializableExtra("model");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/user/register.html?userReferee=" + userInfoSp.getString("mobile", null);

        setView();


    }

    private void setView() {
        txtName.setText(model.getUser().getNickname() + "的");
        if (model.getDistance().length() > 3) {
            txtDistance.setText((Integer.parseInt(model.getDistance()) / 1000) + "KM");
        } else {
            txtDistance.setText(model.getDistance() + "M");
        }
//        if (model.getShareUrl() != null) {
//            if (model.getShareUrl().indexOf("http://") != -1) {
//                shareURL = model.getShareUrl();
//            } else {
//                shareURL = "http://" + model.getShareUrl();
//            }
//        }
//        System.out.println("webURL=" + webURL);

        Bitmap mBitmap = CodeUtils.createImage(shareURL, 400, 400, null);
        imgQRCode.setImageBitmap(mBitmap);

//        initWebView();
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
        webView.loadUrl(shareURL);

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
                    showShare(view);

                    SharedPreferences.Editor editor = wxShareSp.edit();
                    editor.putString("shareWay", "shake");
                    editor.commit();
                }
                break;

            case R.id.layout_award:
                finish();
                ShakeListActivity.instance.finish();
                break;
        }
    }

    private void showShare(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_share, null);

        ImageView qrCode = (ImageView) mview.findViewById(R.id.img_QRCode);
        ImageView wx = (ImageView) mview.findViewById(R.id.img_wx);
        ImageView pyq = (ImageView) mview.findViewById(R.id.img_pyq);
        TextView qx = (TextView) mview.findViewById(R.id.txt_cancel);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        qrCode.setVisibility(View.GONE);


        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToWX(ShakeDetailActivity.this, shareURL,
                        "花米宝邀您玩转红包",
                        "小目标，发一发，摇一摇，聊一聊各种红包玩法");
                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(ShakeDetailActivity.this, shareURL,
                        "花米宝邀您玩转红包",
                        "小目标，发一发，摇一摇，聊一聊各种红包玩法");
                popupWindow.dismiss();

            }
        });
        qx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

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
            object.put("hzbCode", model.getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_615120, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    type = jsonObject.getString("yyCurrency");
                    quantity = jsonObject.getString("yyAmount");

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
        if (type.equals("QBB")) {
            txtCoin.setText("个钱包币");
            txtAward.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(quantity)) + "");
        } else if (type.equals("GWB")) {
            txtCoin.setText("个购物币");
            txtAward.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(quantity)) + "");
        } else if (type.equals("HBB")) {
            txtCoin.setText("个红包");
            txtAward.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(quantity)) + "");
        }
    }

}
