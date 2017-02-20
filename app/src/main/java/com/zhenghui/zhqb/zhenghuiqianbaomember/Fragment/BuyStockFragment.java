package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.StockActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.StockPayActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.WebActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.StockAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.StockModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/19.
 */

public class BuyStockFragment extends Fragment implements StockAdapter.MyItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_introduce)
    TextView txtIntroduce;
    @InjectView(R.id.txt_day)
    TextView txtDay;
    @InjectView(R.id.txt_month)
    TextView txtMonth;
    @InjectView(R.id.txt_money1)
    TextView txtMoney1;
    @InjectView(R.id.txt_money2)
    TextView txtMoney2;
    @InjectView(R.id.txt_money3)
    TextView txtMoney3;
    @InjectView(R.id.txt_money4)
    TextView txtMoney4;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    private View view;

    private StockActivity activity;
    private SharedPreferences userInfoSp;

    private String code;
    private Double price;
    private ArrayList<StockModel> stockList;

    private StockAdapter stockAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock_buy, null);
        ButterKnife.inject(this, view);

        inits();
        initRecyclerView();
        getStockList();

        return view;

    }

    private void inits() {
        stockList = new ArrayList<>();
        activity = (StockActivity) getActivity();

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    private void initRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        stockAdapter = new StockAdapter(getActivity(), stockList);
        recyclerView.setAdapter(stockAdapter);
        stockAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_introduce, R.id.txt_day, R.id.txt_month, R.id.txt_money1, R.id.txt_money2, R.id.txt_money3, R.id.txt_money4, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                getActivity().finish();
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("webURL", "www.baidu.com"));
                break;

            case R.id.txt_day:
                initStockType();
                txtDay.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtDay.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                break;

            case R.id.txt_month:
                initStockType();
                txtMonth.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtMonth.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                break;

            case R.id.txt_money1:
                initStockNumber();
                txtMoney1.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtMoney1.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                code = stockList.get(0).getCode();
                price = Double.parseDouble(stockList.get(0).getPrice() + "");
                break;

            case R.id.txt_money2:
                initStockNumber();
                txtMoney2.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtMoney2.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                code = stockList.get(1).getCode();
                price = Double.parseDouble(stockList.get(1).getPrice() + "");
                break;

            case R.id.txt_money3:
                initStockNumber();
                txtMoney3.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtMoney3.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                code = stockList.get(2).getCode();
                price = Double.parseDouble(stockList.get(2).getPrice() + "");
                break;

            case R.id.txt_money4:
                initStockNumber();
                txtMoney4.setTextColor(getResources().getColor(R.color.fontColor_orange));
                txtMoney4.setBackground(getResources().getDrawable(R.drawable.border_stock_orange));

                code = stockList.get(3).getCode();
                price = Double.parseDouble(stockList.get(3).getPrice() + "");
                break;

            case R.id.txt_confirm:
//                buyStock();

                startActivityForResult(new Intent(getActivity(), StockPayActivity.class).putExtra("code", code).putExtra("price", price), 0);

                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  //这个super可不能落下，否则可能回调不了
        if (data.getBooleanExtra("isPay", false)) {
            activity.getMyStock();
        }
    }

    private void initStockType() {
        txtDay.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtDay.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

        txtMonth.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtMonth.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

    }

    private void initStockNumber() {

        txtMoney1.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtMoney1.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

        txtMoney2.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtMoney2.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

        txtMoney3.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtMoney3.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

        txtMoney4.setTextColor(getResources().getColor(R.color.fontColor_support));
        txtMoney4.setBackground(getResources().getDrawable(R.drawable.border_stock_gray));

    }

    /**
     * 获取股票列表
     */
    private void getStockList() {
        JSONObject object = new JSONObject();
        try {
            object.put("start", "1");
            object.put("limit", "10");
            object.put("status", "1");
            object.put("orderColumn", "price");
            object.put("orderDir", "asc");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808401", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.i("jsonObject", jsonObject.toString());
                    Gson gson = new Gson();
                    ArrayList<StockModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<StockModel>>() {
                    }.getType());

                    System.out.println("lists.size()=" + lists.size());

                    stockList.addAll(lists);
                    stockAdapter.notifyDataSetChanged();

//                    setView();


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

    private void setView() {
        if (stockList.size() == 1) {
            txtMoney1.setText(MoneyUtil.moneyFormatDouble(stockList.get(0).getPrice()));
            txtMoney2.setVisibility(View.GONE);
            txtMoney3.setVisibility(View.GONE);
            txtMoney4.setVisibility(View.GONE);
        }
        if (stockList.size() == 2) {
            txtMoney1.setText(MoneyUtil.moneyFormatDouble(stockList.get(0).getPrice()));
            txtMoney2.setText(MoneyUtil.moneyFormatDouble(stockList.get(1).getPrice()));
            txtMoney3.setVisibility(View.GONE);
            txtMoney4.setVisibility(View.GONE);
        }
        if (stockList.size() == 3) {
            txtMoney1.setText(MoneyUtil.moneyFormatDouble(stockList.get(0).getPrice()));
            txtMoney2.setText(MoneyUtil.moneyFormatDouble(stockList.get(1).getPrice()));
            txtMoney3.setText(MoneyUtil.moneyFormatDouble(stockList.get(2).getPrice()));
            txtMoney4.setVisibility(View.GONE);
        }
        if (stockList.size() == 4) {
            txtMoney1.setText(MoneyUtil.moneyFormatDouble(stockList.get(0).getPrice()));
            txtMoney2.setText(MoneyUtil.moneyFormatDouble(stockList.get(1).getPrice()));
            txtMoney3.setText(MoneyUtil.moneyFormatDouble(stockList.get(2).getPrice()));
            txtMoney4.setText(MoneyUtil.moneyFormatDouble(stockList.get(3).getPrice()));
        }


    }

    /**
     * 购买股票
     */
    private void buyStock() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808403", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                activity.getMyStock();
                Toast.makeText(getActivity(), "购买成功", Toast.LENGTH_SHORT).show();

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
    public void OnItemClick(View view, int position) {
        for (StockModel model : stockList) {
            model.setChoose(false);
        }
        stockList.get(position).setChoose(true);
        stockAdapter.notifyDataSetChanged();

        code = stockList.get(position).getCode();
        price = Double.parseDouble(stockList.get(position).getPrice() + "");
    }
}
