package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_807717;

public class RichTextActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_about)
    TextView txtAbout;
    @InjectView(R.id.txt_title)
    TextView txtTitle;

    private String cKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        setTitle();
        getDatas();
    }

    private void inits() {
        cKey = getIntent().getStringExtra("cKey");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void setTitle() {

        switch (cKey) {

            case "aboutus":
                txtTitle.setText("关于我们");
                break;

            case "reg_protocol":
                txtTitle.setText("注册协议");
                break;

            case "treasure_rule":
                txtTitle.setText("夺宝玩法介绍");
                break;

            case "fyf_rule":
                txtTitle.setText("汇赚宝玩法介绍");
                break;

            case "yyy_rule":
                txtTitle.setText("摇一摇玩法介绍");
                break;

        }

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", cKey);
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_807717, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    RichText.from(jsonObject.getString("note")).into(txtAbout);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RichTextActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RichTextActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(RichTextActivity.this);
        MyApplication.getInstance().removeActivity(this);
    }
}
