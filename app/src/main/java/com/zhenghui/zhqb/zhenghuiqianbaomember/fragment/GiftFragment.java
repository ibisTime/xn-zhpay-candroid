package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.ShopDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LocateState;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.StringUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808217;

public class GiftFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.list_gift)
    ListView listGift;
    @InjectView(R.id.layout_refresh)
    RefreshLayout layoutRefresh;

    // Fragment主视图
    private View view;

    private int page = 1;
    private int pageSize = 10;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private ShopAdapter adapter;
    private List<ShopModel> shopList;

    private AMapLocationClient mLocationClient;

    private String latitude = "";
    private String longitude = "";

    // 定位位置
    public String locatedCity = "杭州";
    // 定位状态
    public int locateState = LocateState.LOCATING;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gift, null);
        ButterKnife.inject(this, view);

        init();
        initListView();
        initRefreshLayout();

        initLocation();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void init() {
        shopList = new ArrayList<>();
        adapter = new ShopAdapter(getActivity(), shopList);

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listGift.setAdapter(adapter);

        listGift.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        layoutRefresh.setOnLoadListener(this);
        layoutRefresh.setOnRefreshListener(this);

    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                        latitude = aMapLocation.getLatitude() + "";
                        longitude = aMapLocation.getLongitude() + "";

                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("latitude",latitude);
                        editor.putString("longitude",longitude);
                        editor.commit();

                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String province = aMapLocation.getProvince();

                        String location = StringUtils.extractLocation(city, district);
                        updateLocateState(LocateState.SUCCESS, location);
                    } else {
                        //定位失败
                        updateLocateState(LocateState.FAILED, "");
                    }
                }
            }
        });
        mLocationClient.startLocation();
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        locatedCity = city;
        locateState = state;
        switch (locateState) {
            case LocateState.FAILED:
                getData("");
                break;

            case LocateState.SUCCESS:
                getData(locatedCity);
                break;
        }
    }

    /**
     * 获取商家列表
     */
    private void getData(String location) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("name", "");
            object.put("level", "");
            object.put("type", "G01");
            object.put("kind", "");
            object.put("province", "");
            object.put("city", "");
            object.put("area", "");
            object.put("uiLocation", "");
            object.put("status", "");
            object.put("longitude", longitude);
            object.put("latitude", latitude);
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_808217, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<ShopModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ShopModel>>() {
                    }.getType());

                    if (page == 1) {
                        shopList.clear();
                    }

                    shopList.addAll(lists);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), ShopDetailsActivity.class)
                .putExtra("type", "gift")
                .putExtra("code", shopList.get(position).getCode()));
    }

    @Override
    public void onRefresh() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setRefreshing(false);
                page = 1;
                getData(locatedCity);
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        layoutRefresh.postDelayed(new Runnable() {

            @Override
            public void run() {
                layoutRefresh.setLoading(false);
                page = page + 1;
                getData(locatedCity);
            }
        }, 1500);
    }
}
