package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.CityPickerActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShopDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShopListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.StockActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.SystemMessageActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.WebActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.BannerModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MessageModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LocateState;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.StringUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by dell1 on 2016/12/12.
 */

public class ShopFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, OnBannerClickListener {

    @InjectView(R.id.list_shop)
    ListView list;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_city)
    TextView txtCity;
    @InjectView(R.id.layout_location)
    LinearLayout layoutLocation;
    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;

    // Fragment主视图
    private View view;
    //
    private View headView;

    // 定位城市

    // 轮播图
    Banner banner;
    // 签到
    ImageView imgSign;
    // 美食
    LinearLayout foodLayout;
    // ktv
    LinearLayout ktvLayout;
    // 美发
    LinearLayout hairLayout;
    // 便利店
    LinearLayout sevenElevenLayout;
    // 足浴
    LinearLayout lavipeditumLayout;
    // 酒店
    LinearLayout hotelLayout;
    // 亲子
    LinearLayout parentChildLayout;
    // 蔬果
    LinearLayout fruitsLayout;
    // 公告
    TextView noticeTxt;

    private List<String> images;
    private List<BannerModel> bannerList;
    private List<ShopModel> shopList;


    private ShopAdapter adapter;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private int page = 1;
    private int pageSize = 10;

    private PopupWindow popupWindow;

    private AMapLocationClient mLocationClient;

    // 定位位置
    public String locatedCity = "杭州";
    // 定位状态
    public int locateState = LocateState.LOCATING;

    private String latitude = "";
    private String longitude = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, null);
        ButterKnife.inject(this, view);

        inits();
        initRefreshLayout();
        initHeadView(inflater);
        initEvent();
        initListView();
        getNotice();

        // 初始城市按钮
        updateLocateState(locateState, locatedCity);
        initLocation();
        getData(locatedCity);
        getBanner();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void inits() {
        images = new ArrayList<>();
        shopList = new ArrayList<>();
        bannerList = new ArrayList<>();
        adapter = new ShopAdapter(getActivity(), shopList);

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initHeadView(LayoutInflater inflater) {
        headView = inflater.inflate(R.layout.head_shop, null);

        banner = (Banner) headView.findViewById(R.id.banner);

        imgSign = (ImageView) headView.findViewById(R.id.img_sign);

        noticeTxt = (TextView) headView.findViewById(R.id.txt_notice);

        ktvLayout = (LinearLayout) headView.findViewById(R.id.layout_ktv);
        foodLayout = (LinearLayout) headView.findViewById(R.id.layout_food);
        hairLayout = (LinearLayout) headView.findViewById(R.id.layout_hair);
        hotelLayout = (LinearLayout) headView.findViewById(R.id.layout_hotel);
        fruitsLayout = (LinearLayout) headView.findViewById(R.id.layout_fruits);
        lavipeditumLayout = (LinearLayout) headView.findViewById(R.id.layout_lavipeditum);
        sevenElevenLayout = (LinearLayout) headView.findViewById(R.id.layout_seven_eleven);
        parentChildLayout = (LinearLayout) headView.findViewById(R.id.layout_parent_child);


    }

    private void initEvent() {
        imgSign.setOnClickListener(new HeadViewOnClickListener());

        noticeTxt.setOnClickListener(new HeadViewOnClickListener());

        foodLayout.setOnClickListener(new HeadViewOnClickListener());
        ktvLayout.setOnClickListener(new HeadViewOnClickListener());
        hairLayout.setOnClickListener(new HeadViewOnClickListener());
        hotelLayout.setOnClickListener(new HeadViewOnClickListener());
        fruitsLayout.setOnClickListener(new HeadViewOnClickListener());
        lavipeditumLayout.setOnClickListener(new HeadViewOnClickListener());
        sevenElevenLayout.setOnClickListener(new HeadViewOnClickListener());
        parentChildLayout.setOnClickListener(new HeadViewOnClickListener());


    }

    @OnClick({R.id.layout_location, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_location:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class)
                        .putExtra("location_city", locatedCity), 0);
                break;

            case R.id.layout_search:
                startActivity(new Intent(getActivity(), ShopListActivity.class)
                        .putExtra("title", "优店")
                        .putExtra("type", "")
                        .putExtra("locatedCity", locatedCity)
                        .putExtra("latitude", latitude)
                        .putExtra("longitude", longitude));
                break;
        }
    }

    public class HeadViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_sign:
                    if (userInfoSp.getString("userId", null) != null) {
                        startActivity(new Intent(getActivity(), StockActivity.class));
                    } else {
                        LoginUtil.toLogin(getActivity());
                    }

                    break;

                case R.id.layout_food:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "美食")
                            .putExtra("type", "1")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_ktv:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "KTV")
                            .putExtra("type", "2")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_hair:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "美发")
                            .putExtra("type", "3")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_seven_eleven:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "便利店")
                            .putExtra("type", "4")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_lavipeditum:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "足疗")
                            .putExtra("type", "5")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_hotel:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "酒店")
                            .putExtra("type", "6")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_parent_child:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "亲子")
                            .putExtra("type", "7")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.layout_fruits:
                    startActivity(new Intent(getActivity(), ShopListActivity.class)
                            .putExtra("title", "蔬果")
                            .putExtra("type", "8")
                            .putExtra("locatedCity", locatedCity)
                            .putExtra("latitude", latitude)
                            .putExtra("longitude", longitude));
                    break;

                case R.id.txt_notice:
                    startActivity(new Intent(getActivity(), SystemMessageActivity.class));
                    break;

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //data为B中回传的Intent
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                locatedCity = data.getExtras().getString("picked_city");
                locateState = data.getExtras().getInt("locate_state");

                txtCity.setText(locatedCity);
                updateLocateState(locateState, locatedCity);
                break;
            default:
                break;
        }
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
            case LocateState.LOCATING:
                txtCity.setText(this.getString(R.string.locating));
                break;
            case LocateState.FAILED:
                txtCity.setText(R.string.located_failed);
                break;
            case LocateState.SUCCESS:
                txtCity.setText(city);
                getData(locatedCity);
                break;
        }
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

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        banner.setOnBannerClickListener(this);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        // 设置在操作Banner时listView事件不触发
        banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    list.requestDisallowInterceptTouchEvent(false);
                } else {
                    list.requestDisallowInterceptTouchEvent(true);
                }
                return true;
            }
        });
    }

    @Override
    public void OnBannerClick(int position) {

        if(!bannerList.get(position - 1).getUrl().equals("")){
            startActivity(new Intent(getActivity(), WebActivity.class).putExtra("webURL", bannerList.get(position - 1).getUrl()));
        }
    }

    private void initListView() {
        list.addHeaderView(headView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 获取商家列表
     */
    private void getData(String locatedCity) {
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser", "");
            object.put("name", "");
            object.put("type", "");
            object.put("legalPersonName", "");
            object.put("userReferee", "");
            object.put("province", "");
            object.put("city", locatedCity);
            object.put("area", "");
            object.put("status", "2");
            object.put("owner", "");
            object.put("userLatitude", latitude + "");
            object.put("userLongitude", longitude + "");
            object.put("owner", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808207", object.toString(), new Xutil.XUtils3CallBackPost() {
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

    private void getBanner() {
        JSONObject object = new JSONObject();
        try {
            object.put("name", "");
            object.put("type", "2");
            object.put("status", "1");
            object.put("belong", "");
            object.put("location", "");
            object.put("parentCode", "");
            object.put("contentType", "");
            object.put("companyCode", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("806052", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<BannerModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<BannerModel>>() {
                    }.getType());

                    bannerList.clear();
                    bannerList.addAll(lists);
                    images.clear();
                    for (BannerModel model : bannerList) {
                        images.add(model.getPic());
                    }
                    initBanner();

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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { // 有headView，所以position从1开始

        startActivity(new Intent(getActivity(), ShopDetailsActivity.class).putExtra("code", shopList.get(i - 1).getCode()));
    }

    private void getNotice() {

        JSONObject object = new JSONObject();
        try {
            object.put("fromSystemCode", appConfigSp.getString("systemCode",null));
            object.put("channelType", "4");
            object.put("pushType", "");
            object.put("toSystemCode", appConfigSp.getString("systemCode",null));
            object.put("toKind", "1");
            object.put("toMobile", "");
            object.put("smsType", "");
            object.put("start", page);
            object.put("limit", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("804040", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MessageModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MessageModel>>() {
                    }.getType());

                    noticeTxt.setText(lists.get(0).getSmsTitle());

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
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData(locatedCity);
                getBanner();
                // 更新数据
                // 更新完后调用该方法结束刷新

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
                getData(locatedCity);
            }
        }, 1500);
    }
}
