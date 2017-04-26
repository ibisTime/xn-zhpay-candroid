package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.GoodDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.GoodSearchActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.LoginActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShoppingCartActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TargetActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.GoodAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.RecyclerViewAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ProductTypeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/12.
 */

public class GoodFragment extends Fragment implements AdapterView.OnItemClickListener, RecyclerViewAdapter.MyItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;
    @InjectView(R.id.txt_bigType1)
    TextView txtBigType1;
    @InjectView(R.id.line_duoshou)
    View lineDuoshou;
    @InjectView(R.id.layout_duoshou)
    LinearLayout layoutDuoshou;
    @InjectView(R.id.txt_bigType2)
    TextView txtBigType2;
    @InjectView(R.id.line_oneyuan)
    View lineOneyuan;
    @InjectView(R.id.layout_oneyuan)
    LinearLayout layoutOneyuan;
    @InjectView(R.id.txt_bigType3)
    TextView txtBigType3;
    @InjectView(R.id.line_0yuan)
    View line0yuan;
    @InjectView(R.id.layout_0yuan)
    LinearLayout layout0yuan;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;
    @InjectView(R.id.txt_type1)
    TextView txtType1;
    @InjectView(R.id.txt_type2)
    TextView txtType2;
    @InjectView(R.id.txt_type3)
    TextView txtType3;
    @InjectView(R.id.txt_type4)
    TextView txtType4;
    @InjectView(R.id.list_good)
    ListView listGood;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.layout_shopCart)
    FrameLayout layoutShopCart;

    // Fragment主视图
    private View view;
    //
    private View headView;

    private boolean isDuoshou = false;
    private List<GoodsModel> list;
    private GoodAdapter goodAdapter;

    private RecyclerViewAdapter recyclerViewAdapter;

    // 所有
    private List<ProductTypeModel> typeList;
    private List<ProductTypeModel> bigTypeList;
    private List<ProductTypeModel> smallTypeList;

    private List<ProductTypeModel> smallList1;
    private List<ProductTypeModel> smallList2;
    private List<ProductTypeModel> smallList3;
    private List<ProductTypeModel> recyclerViewList;

    private String bigType = "";
    private String smallType = "";

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private int page = 1;
    private int pageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_good, null);
        ButterKnife.inject(this, view);

        inits();
        initEvent();
        initRefreshLayout();
        initListView();

        getProductType();


        return view;
    }

    private void inits() {
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        list = new ArrayList<GoodsModel>();
        typeList = new ArrayList<ProductTypeModel>();
        bigTypeList = new ArrayList<ProductTypeModel>();
        smallTypeList = new ArrayList<ProductTypeModel>();

        smallList1 = new ArrayList<ProductTypeModel>();
        smallList2 = new ArrayList<ProductTypeModel>();
        smallList3 = new ArrayList<ProductTypeModel>();
        recyclerViewList = new ArrayList<>();

        goodAdapter = new GoodAdapter(getActivity(), list);
    }


    private void initEvent() {
        listGood.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initListView() {
        listGood.setAdapter(goodAdapter);
        isDuoshou = true;
    }

    @OnClick({R.id.layout_duoshou, R.id.layout_oneyuan, R.id.layout_0yuan, R.id.txt_type1, R.id.txt_type2, R.id.txt_type3, R.id.txt_type4, R.id.layout_shopCart, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_duoshou:
                initBtn();
                lineDuoshou.setVisibility(View.VISIBLE);
                layoutDuoshou.setBackgroundColor(getResources().getColor(R.color.white));

                listGood.setAdapter(goodAdapter);
                if (!isDuoshou) {
                    isDuoshou = true;
                }
                if(smallList1.size() != 0){
                    for (int i = 0; i < smallList1.size(); i++) {
                        smallList1.get(i).setChoose(false);
                    }

                    recyclerViewList.clear();
                    recyclerViewList.addAll(smallList1);
                    recyclerViewList.get(0).setChoose(true);
                    recyclerViewAdapter.notifyDataSetChanged();

                    bigType = bigTypeList.get(0).getCode();
                    smallType = smallList1.get(0).getCode();
                    getList();
                }

                break;

            case R.id.layout_oneyuan:
                startActivity(new Intent(getActivity(), TargetActivity.class));
                break;

            case R.id.layout_0yuan:
                initBtn();
                line0yuan.setVisibility(View.VISIBLE);
                layout0yuan.setBackgroundColor(getResources().getColor(R.color.white));

                if (isDuoshou) {
                    listGood.setAdapter(goodAdapter);
                    isDuoshou = false;
                }

                if(smallList1.size() != 0){
                    for (int i = 0; i < smallList3.size(); i++) {
                        smallList3.get(i).setChoose(false);
                    }
                    recyclerViewList.clear();
                    recyclerViewList.addAll(smallList3);
                    recyclerViewList.get(0).setChoose(true);
                    recyclerViewAdapter.notifyDataSetChanged();

                    bigType = bigTypeList.get(1).getCode();
                    smallType = smallList3.get(0).getCode();

                    getList();
                }

                break;

            case R.id.layout_shopCart:

                if(userInfoSp.getString("userId",null) == null){
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }else{
                    startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                }
                break;

            case R.id.layout_search:
                startActivity(new Intent(getActivity(), GoodSearchActivity.class));
                break;

        }
    }

    private void initBtn() {
        layout0yuan.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutOneyuan.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutDuoshou.setBackgroundColor(getResources().getColor(R.color.grayfa));

        lineDuoshou.setVisibility(View.INVISIBLE);
        lineOneyuan.setVisibility(View.INVISIBLE);
        line0yuan.setVisibility(View.INVISIBLE);
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("category", bigType);
            object.put("type", smallType);
            object.put("name", "");
            object.put("status", "3");
            object.put("location", "");
            object.put("start", page+"");
            object.put("limit", pageSize+"");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
//            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808028", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {


                try {
                    JSONObject jsonObject = new JSONObject(result);


                    Gson gson = new Gson();
                    ArrayList<GoodsModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<GoodsModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }

                    list.addAll(lists);
                    goodAdapter.notifyDataSetChanged();

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
     * 获取产品类别
     */
    private void getProductType() {
        JSONObject object = new JSONObject();
        try {
            object.put("parentCode", "");
            object.put("name", "");
            object.put("type", "1");
            object.put("orderColumn", "order_no");
            object.put("orderDir", "asc");
            object.put("status", "1");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808007", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<ProductTypeModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<ProductTypeModel>>() {
                    }.getType());

                    typeList.clear();
                    bigTypeList.clear();
                    smallTypeList.clear();

                    smallList1.clear();
                    smallList2.clear();
                    smallList3.clear();

                    typeList.addAll(lists);
                    creatType();

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

    private void creatType() {

        for (ProductTypeModel model : typeList) {
            if (model.getParentCode().equals("0")) {
                bigTypeList.add(model);
            } else {
                smallTypeList.add(model);
            }
        }

        setType();
    }

    private void setType() {
        for (ProductTypeModel big : bigTypeList) {
            if (big.getCode().equals("FL201700000000000001")) {
                txtBigType1.setText(big.getName());
                for (ProductTypeModel small : smallTypeList) {
                    if (small.getParentCode().equals(big.getCode())) {
                        smallList1.add(small);
                    }
                }

            }
//            else if (big.getOrderNo() == 2) {
////                txtBigType2.setText(big.getName());
//                for (ProductTypeModel small : smallTypeList) {
//                    if (small.getParentCode().equals(big.getCode())) {
//                        smallList2.add(small);
//                    }
//                }
//            }
            else if (big.getCode().equals("FL201700000000000002")) {
                txtBigType3.setText(big.getName());
                for (ProductTypeModel small : smallTypeList) {
                    if (small.getParentCode().equals(big.getCode())) {
                        smallList3.add(small);
                    }
                }
            }
        }

        recyclerViewList.clear();
        if(bigType.equals("") || smallType.equals("")){
        // 默认加载第一大类的第一小类
            recyclerViewList.addAll(smallList1);
            recyclerViewList.get(0).setChoose(true);

            smallType = smallList1.get(0).getCode();
            bigType = bigTypeList.get(0).getCode();
        }else{
            // 恢复大小类关系，以及小类选择状态
            for (int i = 0; i < bigTypeList.size(); i++){
                if(bigTypeList.get(i).getCode().equals(bigType)){
                    if(i == 0){
                        recyclerViewList.addAll(smallList1);
                        for(ProductTypeModel model : smallList1){
                            if(model.getCode().equals(smallType)){
                                model.setChoose(true);
                            }
                        }
                    }else{
                        recyclerViewList.addAll(smallList3);
                        for(ProductTypeModel model : smallList3){
                            if(model.getCode().equals(smallType)){
                                model.setChoose(true);
                            }
                        }
                    }
                }
            }
        }
        initRecyclerView();
        getList();
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), recyclerViewList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取购物车内商品数量
     */
    private void getShoppingCartNum() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("start", "0");
            object.put("limit", "10");
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808045", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getInt("totalCount") == 0) {
                        txtNumber.setVisibility(View.GONE);
                    } else {
                        txtNumber.setVisibility(View.VISIBLE);
                        txtNumber.setText(jsonObject.getInt("totalCount") + "");
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        startActivity(new Intent(getActivity(), GoodDetailsActivity.class).putExtra("code", list.get(i).getCode()));

    }

    @OnClick(R.id.layout_shopCart)
    public void onClick() {
    }

    @Override
    public void OnItemClick(View view, int position) {
        for (ProductTypeModel model : recyclerViewList) {
            model.setChoose(false);
        }
        recyclerViewList.get(position).setChoose(true);
        recyclerViewAdapter.notifyDataSetChanged();

        smallType = recyclerViewList.get(position).getCode();
        getList();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(userInfoSp.getString("userId",null) != null){
            getShoppingCartNum();
        }
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
//                getList();
                getProductType();
                // 更新数据
                // 更新完后调用该方法结束刷新
                if(userInfoSp.getString("userId",null) != null){
                    getShoppingCartNum();
                }
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
                getList();
            }
        }, 1500);
    }
}
