package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentFrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.EvaluateAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.EvaluateModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class EvaluateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.list_evaluate)
    ListView listEvaluate;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.layout_evalute)
    PercentFrameLayout layoutEvalute;
    @InjectView(R.id.img_start1)
    ImageView imgStart1;
    @InjectView(R.id.img_start2)
    ImageView imgStart2;
    @InjectView(R.id.img_start3)
    ImageView imgStart3;
    @InjectView(R.id.img_start4)
    ImageView imgStart4;
    @InjectView(R.id.img_start5)
    ImageView imgStart5;

    private View view;

    private String code;

    private int page;
    private int pageSize;

    EvaluateAdapter adapter;
    ArrayList<EvaluateModel> list;


    public static EvaluateFragment newInstance(String code) {
        EvaluateFragment fragment = new EvaluateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_evaluate, null);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        if (args != null) {
            code = args.getString("code");
        }

        inits();
        initListView();
        initRefreshLayout();
        getDatas();

        return view;
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new EvaluateAdapter(getActivity(), list);
    }

    private void initListView() {
        listEvaluate.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 获取评价
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("jewelCode", code);
            object.put("start", "1");
            object.put("limit", "10");
            object.put("orderColumn", "");
            object.put("orderDir", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808325", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    showStart(jsonObject.getString("goodRate"));
                    txtNumber.setText("购买人数(" + jsonObject.getInt("buyNums") + ")");

                    Gson gson = new Gson();
                    ArrayList<EvaluateModel> lists = gson.fromJson(jsonObject.getJSONObject("page").getJSONArray("list").toString(), new TypeToken<ArrayList<EvaluateModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }

                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStart(String rate) {
        Double aDouble = Double.parseDouble(rate);

        if (aDouble > 0 && aDouble < 0.25) {
            imgStart1.setBackgroundResource(R.mipmap.start_orange);
            imgStart2.setBackgroundResource(R.mipmap.start_gray);
            imgStart3.setBackgroundResource(R.mipmap.start_gray);
            imgStart4.setBackgroundResource(R.mipmap.start_gray);
            imgStart5.setBackgroundResource(R.mipmap.start_gray);
        }
        if (aDouble >= 0.25 && aDouble < 0.5) {
            imgStart1.setBackgroundResource(R.mipmap.start_orange);
            imgStart2.setBackgroundResource(R.mipmap.start_orange);
            imgStart3.setBackgroundResource(R.mipmap.start_gray);
            imgStart4.setBackgroundResource(R.mipmap.start_gray);
            imgStart5.setBackgroundResource(R.mipmap.start_gray);
        }
        if (aDouble >= 0.5 && aDouble < 0.75) {
            imgStart1.setBackgroundResource(R.mipmap.start_orange);
            imgStart2.setBackgroundResource(R.mipmap.start_orange);
            imgStart3.setBackgroundResource(R.mipmap.start_orange);
            imgStart4.setBackgroundResource(R.mipmap.start_gray);
            imgStart5.setBackgroundResource(R.mipmap.start_gray);
        }
        if (aDouble >= 0.75 && aDouble < 1) {
            imgStart1.setBackgroundResource(R.mipmap.start_orange);
            imgStart2.setBackgroundResource(R.mipmap.start_orange);
            imgStart3.setBackgroundResource(R.mipmap.start_orange);
            imgStart4.setBackgroundResource(R.mipmap.start_orange);
            imgStart5.setBackgroundResource(R.mipmap.start_gray);
        }
        if (aDouble == 1) {
            imgStart1.setBackgroundResource(R.mipmap.start_orange);
            imgStart2.setBackgroundResource(R.mipmap.start_orange);
            imgStart3.setBackgroundResource(R.mipmap.start_orange);
            imgStart4.setBackgroundResource(R.mipmap.start_orange);
            imgStart5.setBackgroundResource(R.mipmap.start_orange);
        }


    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getDatas();
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getDatas();
            }
        }, 1500);
    }
}
