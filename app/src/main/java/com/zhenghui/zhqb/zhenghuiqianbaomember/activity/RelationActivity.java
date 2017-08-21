package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805800;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808220;

public class RelationActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.img_photo)
    CircleImageView imgPhoto;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_referrer)
    TextView txtReferrer;
    @InjectView(R.id.txt_time)
    TextView txtTime;
    @InjectView(R.id.layout_level1)
    LinearLayout layoutLevel1;
    @InjectView(R.id.layout_level2)
    LinearLayout layoutLevel2;
    @InjectView(R.id.layout_level3)
    LinearLayout layoutLevel3;
    @InjectView(R.id.txt_level1)
    TextView txtLevel1;
    @InjectView(R.id.txt_level2)
    TextView txtLevel2;
    @InjectView(R.id.txt_level3)
    TextView txtLevel3;
    @InjectView(R.id.txt_relation_store)
    TextView txtStore;
    @InjectView(R.id.layout_relation_store)
    LinearLayout layoutRelationStore;

    String photo;
    String userName;
    String userReferee;
    String updateDatetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);
        ButterKnife.inject(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReferee();
        getStoreNumber();
    }

    private void init() {
        photo = getIntent().getStringExtra("photo");
        userName = getIntent().getStringExtra("nickName");
        userReferee = getIntent().getStringExtra("userReferee");
        updateDatetime = getIntent().getStringExtra("updateDatetime");


        txtName.setText(userName);
        txtReferrer.setText("推荐人: "+userReferee);
        ImageUtil.photo(photo, imgPhoto, this);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(updateDatetime);
        txtTime.setText("加入时间: "+format.format(date));
    }


    @OnClick({R.id.layout_back, R.id.layout_level1, R.id.layout_level2, R.id.layout_level3, R.id.layout_relation_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_level1:
                startActivity(new Intent(RelationActivity.this, ReferrerActivity.class).putExtra("level", "1"));
                break;

            case R.id.layout_level2:
                startActivity(new Intent(RelationActivity.this, ReferrerActivity.class).putExtra("level", "2"));
                break;

            case R.id.layout_level3:
                startActivity(new Intent(RelationActivity.this, ReferrerActivity.class).putExtra("level", "3"));
                break;

            case R.id.layout_relation_store:
                startActivity(new Intent(RelationActivity.this, RelationStoreActivity.class).putExtra("userReferee", userInfoSp.getString("userId", null)));
                break;
        }
    }

    public void getReferee() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805800, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtLevel1.setText("下一级(" + jsonObject.getString("oneRefCount") + ")");
                    txtLevel2.setText("下二级(" + jsonObject.getString("twoRefCount") + ")");
                    txtLevel3.setText("下三级(" + jsonObject.getString("threeRefCount") + ")");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RelationActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RelationActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getStoreNumber() {

        JSONObject object = new JSONObject();
        try {
            object.put("userReferee", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808220, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtStore.setText("推荐商家(" + jsonObject.getString("totalCount") + ")");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RelationActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RelationActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
