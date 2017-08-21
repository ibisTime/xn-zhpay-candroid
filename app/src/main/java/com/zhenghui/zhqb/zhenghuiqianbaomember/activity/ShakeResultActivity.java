package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShakeResultActivity extends AppCompatActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_award)
    TextView txtAward;
    @InjectView(R.id.img_icon)
    ImageView imgIcon;

    String type;
    String quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_result);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        type = getIntent().getStringExtra("type");
        quantity = getIntent().getStringExtra("quantity");

        if (type.equals("0")) {
            imgIcon.setBackgroundResource(R.mipmap.ee_11);
            txtAward.setText("好运用完了，很遗憾，今天你摇一摇次数超限，明天再来哦");
        } else if (type.equals("1")) {
            txtAward.setText("喜得" + quantity + "红包币");
        } else if (type.equals("2")) {
            txtAward.setText("喜得" + quantity + "钱包币");
        } else if (type.equals("3")) {
            txtAward.setText("喜得" + quantity + "购物币");
        }

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
