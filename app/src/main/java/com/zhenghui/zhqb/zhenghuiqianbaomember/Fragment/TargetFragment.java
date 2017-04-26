package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.JewelActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.JewelRecordActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.JewelRecordAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.TagetAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.JewelRecordModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.TargetModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.JewelRecordService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/2/21.
 */

public class TargetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {

    @InjectView(R.id.list_target)
    ListView listTarget;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    // Fragment主视图
    private View view;

    private View headView;

    private TextView txtStatus1;
    private TextView txtPhone1;
    private TextView txtContent1;

    private TextView txtStatus2;
    private TextView txtPhone2;
    private TextView txtContent2;

    private TextView txtStatus3;
    private TextView txtPhone3;
    private TextView txtContent3;

    private ListView listRecord;
    private LinearLayout layoutRecord;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private TagetAdapter adapter;
    private JewelRecordAdapter recordAdapter;
    private List<TargetModel> list;
    private List<JewelRecordModel> recorList;
    private int page = 1;
    private int pageSize = 10;

    private JewelRecordReceiver receiver = null;

    private int k = 1;
    private boolean isRun;

    private Timer timer;
    private TimerTask task;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10086) {

                //执行滚动逻辑
                if (k == recorList.size() - 1) {//当k=mList.size()-1 时先"滚动到".size() 再马上跳转到0位置
                    listRecord.smoothScrollToPosition(recorList.size());
                    listRecord.setSelection(1);
                    k = 1;
                } else {
                    k++;
                    listRecord.smoothScrollToPosition(k);
                }
                startTime();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_target, null);
        ButterKnife.inject(this, view);

        inits();
        initHeadView(inflater);
        initRefreshLayout();
        initListView();

        initReceiver();

        getData();
        getJewelRecord();

        isRun = true;

        return view;
    }

    private void initReceiver() {
        //启动服务
//        getActivity().startService(new Intent(getActivity(), JewelRecordService.class));

        //注册广播接收器
        receiver = new JewelRecordReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zhenghui.zhqb.zhenghuiqianbaomember.service.JewelRecordService");
        getActivity().registerReceiver(receiver,filter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("hidden="+hidden);
        if (hidden){
            timer.cancel();
            getActivity().stopService(new Intent(getActivity(), JewelRecordService.class));
        }else{
            startTime();
            getActivity().startService(new Intent(getActivity(), JewelRecordService.class));
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        startTime();
        getActivity().startService(new Intent(getActivity(), JewelRecordService.class));
    }

    private void startTime() {

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 10086;
                mHandler.sendMessage(message);
            }

        };

        timer.schedule(task, 1500);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        getActivity().stopService(new Intent(getActivity(), JewelRecordService.class));
    }

    private void inits() {
        list = new ArrayList<>();
        recorList = new ArrayList<>();
        adapter = new TagetAdapter(getActivity(),list);
        recordAdapter = new JewelRecordAdapter(getActivity(),recorList);

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
//        swipeContainer.setOnLoadListener(this);
    }


    private void initHeadView(LayoutInflater inflater) {
        headView = inflater.inflate(R.layout.head_target, null);

        layoutRecord = (LinearLayout) headView.findViewById(R.id.layout_record);

        listRecord = (ListView) headView.findViewById(R.id.list_record);

        txtPhone1 = (TextView) headView.findViewById(R.id.txt_phone1);
        txtStatus1 = (TextView) headView.findViewById(R.id.txt_status1);
        txtContent1 = (TextView) headView.findViewById(R.id.txt_content1);

        txtPhone2 = (TextView) headView.findViewById(R.id.txt_phone2);
        txtStatus2 = (TextView) headView.findViewById(R.id.txt_status2);
        txtContent2 = (TextView) headView.findViewById(R.id.txt_content2);

        txtPhone3 = (TextView) headView.findViewById(R.id.txt_phone3);
        txtStatus3 = (TextView) headView.findViewById(R.id.txt_status3);
        txtContent3 = (TextView) headView.findViewById(R.id.txt_content3);

        listRecord.setAdapter(recordAdapter);
        layoutRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), JewelRecordActivity.class));
            }
        });

    }

    private void initListView() {
        listTarget.addHeaderView(headView);
        listTarget.setAdapter(adapter);
        listTarget.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(getActivity(), JewelActivity.class).putExtra("code",list.get(i-1).getCode()));
    }

    /**
     * 获取广播数据
     */
    public class JewelRecordReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String result = bundle.getString("result",null);
            if(null != result){
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<JewelRecordModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<JewelRecordModel>>() {
                    }.getType());
                    recorList.clear();
                    recorList.addAll(lists);

                    recordAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            context.unregisterReceiver(this);
        }
    }

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("status", "0");
            object.put("templateCode", "");
            object.put("dateStart", "");
            object.put("dateEnd", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615015", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<TargetModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<TargetModel>>() {
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

    private void getJewelRecord() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("jewelCode", "");
            object.put("start", "1");
            object.put("limit", "10");
            object.put("orderDir", "");
            object.put("status","123");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615025", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<JewelRecordModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<JewelRecordModel>>() {
                    }.getType());
                    recorList.clear();
                    recorList.addAll(lists);
                    recordAdapter.notifyDataSetChanged();


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

//    private void setJewelRecord() {
//
//        for(int i=0; i<recorList.size();i++ ){
//
//            if(i == 0){
//                txtStatus1.setVisibility(View.VISIBLE);
//                if(recorList.get(0).getStatus().equals("2")){
//                    txtStatus1.setText("中奖");
//                    txtContent1.setText(Html.fromHtml("中得<html><font color=\"#fe4332\">"+
//                            (recorList.get(0).getJewel().getAmount()/1000)+
//                            "</font></html>"+getCurrency(recorList.get(0).getJewel().getCurrency())));
//                }else{
//                    txtStatus1.setText("参与");
//                    txtContent1.setText(Html.fromHtml("参与<html><font color=\"#fe4332\">"+
//                            recorList.get(0).getTimes()+ "</font></html>次夺宝"));
//                }
//                txtPhone1.setText(recorList.get(0).getMobile().substring(0,3)+"****"+recorList.get(0).getMobile().substring(7,recorList.get(0).getMobile().length()));
//            }else if(i == 1){
//                txtStatus2.setVisibility(View.VISIBLE);
//                if(recorList.get(1).getStatus().equals("2")){
//                    txtStatus2.setText("中奖");
//                    txtContent2.setText(Html.fromHtml("中得<html><font color=\"#fe4332\">"+
//                            (recorList.get(0).getJewel().getAmount()/1000)+
//                            "</font></html>"+getCurrency(recorList.get(0).getJewel().getCurrency())));
//                }else{
//                    txtStatus2.setText("参与");
//                    txtContent2.setText(Html.fromHtml("参与<html><font color=\"#fe4332\">"+
//                            recorList.get(1).getTimes()+ "</font></html>次夺宝"));
//                }
//                txtPhone2.setText(recorList.get(1).getMobile().substring(0,3)+"****"+recorList.get(1).getMobile().substring(7,recorList.get(0).getMobile().length()));
//            }else{
//                txtStatus3.setVisibility(View.VISIBLE);
//                if(recorList.get(2).getStatus().equals("2")){
//                    txtStatus3.setText("中奖");
//                    txtContent3.setText(Html.fromHtml("中得<html><font color=\"#fe4332\">"+
//                            (recorList.get(0).getJewel().getAmount()/1000)+
//                            "</font></html>"+getCurrency(recorList.get(0).getJewel().getCurrency())));
//                }else{
//                    txtStatus3.setText("参与");
//                    txtContent3.setText(Html.fromHtml("参与<html><font color=\"#fe4332\">"+
//                            recorList.get(2).getTimes()+ "</font></html>次夺宝"));
//                }
//                txtPhone3.setText(recorList.get(2).getMobile().substring(0,3)+"****"+recorList.get(2).getMobile().substring(7,recorList.get(0).getMobile().length()));
//            }
//
//        }
//
//    }

    private String getCurrency(String currency){
        String type = "";
        switch (currency){
            case "FRB":
                type = "分润";
                break;
            case "GXJL":
                type = "贡献奖励";
                break;
            case "GWB":
                type = "购物币";
                break;
            case "QBB":
                type = "钱包币";
                break;
            case "HBB":
                type = "红包";
                break;
            case "HBYJ":
                type = "红包业绩";
                break;
        }
        return type;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
                getJewelRecord();
            }
        }, 1500);
    }


//    @Override
//    public void onLoad() {
//        swipeContainer.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                swipeContainer.setLoading(false);
//                page = page + 1;
//                getData();
//            }
//        }, 1500);
//    }


}
