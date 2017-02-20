package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ShakeAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShakeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShakeListActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_tree)
    ListView listTree;

    private List<ShakeModel> list;
    private ShakeAdapter adapter;

    private String json;

    public static ShakeListActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_list);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {

        instance = this;

        json = getIntent().getStringExtra("json");
        if (json != null) {
            initJson();
        }
    }

    private void initJson() {
        try {
            JSONArray jsonArray = new JSONArray(json);

            Gson gson = new Gson();
            list = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<ShakeModel>>() {
            }.getType());

            adapter = new ShakeAdapter(this, list);
            listTree.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        listTree.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        startActivity(new Intent(ShakeListActivity.this, ShakeDetailActivity.class).putExtra("model", list.get(i)));
        finish();
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
