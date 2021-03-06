package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.CityPickerActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.ShopDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.ShopListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.SystemMessageActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.WebActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.PagerAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.BannerModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MessageModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.Model;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.StoreTypeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LocateState;
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
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_804040;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_806052;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808007;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808217;

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

    // 店铺类别
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private PagerAdapter pageAdapter;
    // 传递的数据
    private ArrayList<Model> modelList;
    // 商家分类
    private List<StoreTypeModel> storeTypelist;
    // 分类分页
    private int pageCount;
    private int totalCount;

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

    TextView txtFlag1;
    TextView txtFlag2;
    TextView txtFlag3;
    TextView txtFlag4;
    TextView txtFlag5;
    TextView txtFlag6;
    List<TextView> txtFlagList;

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
        initViewPager();

        // 初始城市按钮
        updateLocateState(locateState, locatedCity);
        initLocation();
        getData(locatedCity);
        getBanner();
        getNotice(true);
        getStoreType();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotice(false);
        getStoreType();
    }


    private void inits() {
        images = new ArrayList<>();
        shopList = new ArrayList<>();
        bannerList = new ArrayList<>();
        txtFlagList = new ArrayList<>();

        fragments = new ArrayList<>();
        modelList = new ArrayList<>();
        storeTypelist = new ArrayList<>();

        adapter = new ShopAdapter(getActivity(), shopList);

        //初始化pageAdapter
        pageAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(),
                fragments);


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

        viewPager = (ViewPager) headView.findViewById(R.id.viewpager_layout);

        txtFlag1 = (TextView) headView.findViewById(R.id.txt_flag1);
        txtFlag2 = (TextView) headView.findViewById(R.id.txt_flag2);
        txtFlag3 = (TextView) headView.findViewById(R.id.txt_flag3);
        txtFlag4 = (TextView) headView.findViewById(R.id.txt_flag4);
        txtFlag5 = (TextView) headView.findViewById(R.id.txt_flag5);
        txtFlag6 = (TextView) headView.findViewById(R.id.txt_flag6);

        noticeTxt = (TextView) headView.findViewById(R.id.txt_notice);
        ktvLayout = (LinearLayout) headView.findViewById(R.id.layout_ktv);
        foodLayout = (LinearLayout) headView.findViewById(R.id.layout_food);
        hairLayout = (LinearLayout) headView.findViewById(R.id.layout_hair);
        hotelLayout = (LinearLayout) headView.findViewById(R.id.layout_hotel);
        fruitsLayout = (LinearLayout) headView.findViewById(R.id.layout_fruits);
        lavipeditumLayout = (LinearLayout) headView.findViewById(R.id.layout_lavipeditum);
        sevenElevenLayout = (LinearLayout) headView.findViewById(R.id.layout_seven_eleven);
        parentChildLayout = (LinearLayout) headView.findViewById(R.id.layout_parent_child);

        viewPager.setOnPageChangeListener(new MyPageChangeListener());

        txtFlagList.add(txtFlag1);
        txtFlagList.add(txtFlag2);
        txtFlagList.add(txtFlag3);
        txtFlagList.add(txtFlag4);
        txtFlagList.add(txtFlag5);
        txtFlagList.add(txtFlag6);
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
        }

        private void enableDisableSwipeRefresh(boolean b) {
            // swipeContainer:自定义下拉刷新控件
            if (swipeContainer != null) {
                swipeContainer.setEnabled(b);
            }
        }
    }

    private void initEvent() {
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

    /**
     * 初始化数据
     */
    private void initPagerDatas() {
        totalCount = storeTypelist.size();

        modelList.clear();
        for (int i = 0; i < storeTypelist.size(); i++) {
            Model model = new Model(i + "", i + "name");
            model.setTitle(storeTypelist.get(i).getName());
            model.setImage(storeTypelist.get(i).getPic());
            model.setType(storeTypelist.get(i).getCode());
            model.setLocationCity(locatedCity);
            modelList.add(model);
        }

        setViewPager();
    }

    /**
     *
     */
    private void setViewPager() {
        int size = modelList.size();
        fragments.clear();

        if (size % 8 == 0) {
            pageCount = size / 8;
        } else {
            pageCount = size / 8 + 1;
        }

        for (int i = 0; i < pageCount; i++) {
            if(i<6){
                // 初始化点
                txtFlagList.get(i).setVisibility(View.VISIBLE);
            }
            //初始化每一个fragment
            GridFragment gf = GridFragment.newInstance(i, modelList);
            fragments.add(gf);
        }

        pageAdapter.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < pageCount; i++) {
                    if(i == arg0){
                        txtFlagList.get(i).setBackground(getResources().getDrawable(R.drawable.corners_store_type_orange));
                    }else{
                        txtFlagList.get(i).setBackground(getResources().getDrawable(R.drawable.corners_store_type_gray));
                    }
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
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
                        .putExtra("title", "店铺搜索")
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
        banner.setOnPageChangeListener(new MyPageChangeListener());
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

    private void initViewPager() {
        viewPager.setAdapter(pageAdapter);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            banner.stopAutoPlay();
        }else{
            getNotice(false);
            banner.startAutoPlay();
        }
    }

    /**
     * 获取商家分类
     */
    private void getStoreType(){
        JSONObject object = new JSONObject();
        try {
            object.put("type","2");
            object.put("name","");
            object.put("status","1");
            object.put("parentCode","");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808007, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<StoreTypeModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<StoreTypeModel>>() {
                    }.getType());

                    storeTypelist.clear();
                    storeTypelist.addAll(lists);

                    initPagerDatas();

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

    /**
     * 获取商家列表
     */
    private void getData(String locatedCity) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("name", "");
            object.put("level", "");
            object.put("type", "SN");
            object.put("province", "");
            object.put("city", locatedCity);
            object.put("area", "");
            object.put("uiLocation", "1");
            object.put("status", "2");
            object.put("longitude", longitude+ "");
            object.put("latitude", latitude + "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "ui_order");
            object.put("orderDir", "asc");
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

        new Xutil().post(CODE_806052, object.toString(), new Xutil.XUtils3CallBackPost() {
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

    private void getNotice(final boolean isShow) {

        JSONObject object = new JSONObject();
        try {
            object.put("fromSystemCode", appConfigSp.getString("systemCode",null));
            object.put("channelType", "4");
            object.put("pushType", "");
            object.put("toSystemCode", appConfigSp.getString("systemCode",null));
            object.put("toKind", "1");
            object.put("status","1");
            object.put("toMobile", "");
            object.put("smsType", "");
            object.put("start", "1");
            object.put("limit", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_804040, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MessageModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MessageModel>>() {
                    }.getType());

                    if(lists.size() != 0){
                        noticeTxt.setText(lists.get(0).getSmsTitle());
                        if(isShow){
                            showNotice(lists.get(0).getSmsTitle(),lists.get(0).getSmsContent());
                        }
                    }


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

    private void showNotice(String title,String content) {
        new AlertDialog.Builder(getActivity()).setTitle(title)
                .setMessage(content)
                .setPositiveButton("确定", null).show();
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
                getStoreType();
                getNotice(false);
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
