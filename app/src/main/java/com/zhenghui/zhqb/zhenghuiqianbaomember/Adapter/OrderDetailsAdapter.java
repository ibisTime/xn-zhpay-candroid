package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.OrderDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderDetailsAdapter extends BaseAdapter {

    private List<OrderModel> list;
    private Context context;
    private ViewHolder holder;

    private OrderDetailsActivity activity;
//    private String[] bank = {"好评!","中评!","差评!"};
    private String[] bank = {"好评!"};
    private SharedPreferences userInfoSp;

    public OrderDetailsAdapter(Context context, List<OrderModel> list) {
        this.list = list;
        this.context = context;

        activity = (OrderDetailsActivity) context;
        userInfoSp = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order_details, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evaluateTip(i);
            }
        });

        setView(i);

        return view;
    }

    public void setView(int position) {

        ImageUtil.glide(list.get(position).getProduct().getAdvPic(), holder.imgGood, context);

        holder.txtName.setText(list.get(position).getProduct().getName());
        holder.txtNumber.setText("X" + list.get(position).getQuantity() + "件");

        holder.txtParameter.setText(list.get(position).getProductSpecsName());

        setPrice(position);

    }

    private void setPrice(int position) {
        if (list.get(position).getPrice1() == 0) {
            holder.txtRmb1.setVisibility(View.GONE);
            holder.txtRmb2.setVisibility(View.GONE);
        } else {
            holder.txtRmb1.setVisibility(View.VISIBLE);
            holder.txtRmb2.setVisibility(View.VISIBLE);
            holder.txtRmb1.setText("¥" + MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));
            if (list.get(position).getPrice2() == 0 && list.get(position).getPrice3() == 0) {
                holder.txtRmb2.setText("");
            }
        }

        if (list.get(position).getPrice2() == 0) {
            holder.txtGwb1.setVisibility(View.GONE);
            holder.txtGwb2.setVisibility(View.GONE);
        } else {
            holder.txtGwb1.setVisibility(View.VISIBLE);
            holder.txtGwb2.setVisibility(View.VISIBLE);
            holder.txtGwb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice2()));
            if (list.get(position).getPrice3() == 0) {
                holder.txtGwb2.setText("购物币");
            }
        }

        if (list.get(position).getPrice3() == 0) {
            holder.txtQbb1.setVisibility(View.GONE);
            holder.txtQbb2.setVisibility(View.GONE);
        } else {
            holder.txtQbb1.setVisibility(View.VISIBLE);
            holder.txtQbb2.setVisibility(View.VISIBLE);
            holder.txtQbb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice3()));
        }

        if(list.get(position).getPrice1() == 0 && list.get(position).getPrice2() == 0 && list.get(position).getPrice3() == 0){
            holder.txtRmb1.setText("0");
            holder.txtRmb1.setVisibility(View.VISIBLE);
        }
    }

    private void evaluateTip(final int position) {
        new AlertDialog.Builder(context).setTitle("宝贝收到了给个评价吧").setItems(
                bank, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            evaluate("3",position);
                        }else if(which == 1){
                            evaluate("B",position);
                        }else{
                            evaluate("C",position);
                        }

                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 评价
     */
    private void evaluate(String evaluateType,int position) {
        JSONObject object = new JSONObject();
        try {
//            object.put("orderCode", list.get(0).getOrderCode());
//            object.put("jewelCode", list.get(0).getProductCode());
//            object.put("evaluateType", evaluateType);
//            object.put("interacter", userInfoSp.getString("userId",null));

            object.put("storeCode", list.get(0).getProductCode());
            object.put("type", evaluateType);
            object.put("userId", userInfoSp.getString("userId",null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        new Xutil().post("808320", object.toString(), new Xutil.XUtils3CallBackPost() {
        new Xutil().post("808240", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(context, "评价成功", Toast.LENGTH_SHORT).show();
                activity.getDatas();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ViewHolder {
        @InjectView(R.id.img_good)
        ImageView imgGood;
        @InjectView(R.id.txt_parameter)
        TextView txtParameter;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_rmb1)
        TextView txtRmb1;
        @InjectView(R.id.txt_rmb2)
        TextView txtRmb2;
        @InjectView(R.id.txt_gwb1)
        TextView txtGwb1;
        @InjectView(R.id.txt_gwb2)
        TextView txtGwb2;
        @InjectView(R.id.txt_qbb1)
        TextView txtQbb1;
        @InjectView(R.id.txt_qbb2)
        TextView txtQbb2;
        @InjectView(R.id.layout_price)
        LinearLayout layoutPrice;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_btn)
        TextView txtBtn;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
