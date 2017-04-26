package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.TargetFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TargetActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_fragment)
    FrameLayout layoutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        ButterKnife.inject(this);

        inits();
    }

    private void inits() {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment, new TargetFragment()).commitAllowingStateLoss();
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
