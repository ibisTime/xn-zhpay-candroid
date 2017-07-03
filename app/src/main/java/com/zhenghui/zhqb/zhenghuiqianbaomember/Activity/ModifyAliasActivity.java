package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.edt_nickname;

public class ModifyAliasActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(edt_nickname)
    EditText edtNickname;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_alias);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    private void inits() {
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        edtNickname.setText(getIntent().getStringExtra("nickname"));
        edtNickname.setSelection(getIntent().getStringExtra("nickname").length());
    }

    @OnClick({R.id.layout_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_confirm:
                if(edtNickname.getText().toString().trim().length() == 0){
                    Toast.makeText(ModifyAliasActivity.this, "请填写昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                modifyAlias();
                break;
        }
    }

    /**
     * 修改昵称
     */
    public void modifyAlias() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId",preferences.getString("userId",null));
            object.put("nickname",edtNickname.getText().toString());
            object.put("token",preferences.getString("token",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805075", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(ModifyAliasActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ModifyAliasActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ModifyAliasActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
