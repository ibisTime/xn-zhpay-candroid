package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.OrderDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

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
        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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

        if (list.get(position).getStore().getType().equals("G01")){
            holder.txtCurrency.setText("礼品券");
        }else {
            if (list.get(position).getProduct().getPayCurrency().equals("4")){
                holder.txtCurrency.setText("钱包币");
            }else{
                holder.txtCurrency.setText("¥");
            }
        }
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));

    }



    static class ViewHolder {
        @InjectView(R.id.img_good)
        ImageView imgGood;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_currency)
        TextView txtCurrency;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.layout_title)
        LinearLayout layoutTitle;
        @InjectView(R.id.txt_parameter)
        TextView txtParameter;
        @InjectView(R.id.txt_number)
        TextView txtNumber;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
