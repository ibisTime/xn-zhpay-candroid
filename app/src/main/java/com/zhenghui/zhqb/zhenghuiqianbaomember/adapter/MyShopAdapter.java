package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MyShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyShopAdapter extends BaseAdapter {

    private List<MyShopModel> list;
    private Context context;
    private ViewHolder holder;


    public MyShopAdapter(Context context, List<MyShopModel> list) {
        this.list = list;
        this.context = context;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        ImageUtil.glide(list.get(position).getStore().getAdvPic(), holder.imgShopPic, context);

        holder.txtNumber.setText(list.get(position).getCode());

        holder.txtShopName.setText(list.get(position).getStore().getName());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(list.get(position).getCreateDatetime());
        holder.txtTime.setText(s.format(d5));


        holder.txtDiscount.setText(list.get(position).getStore().getSlogan());

        if(list.get(position).getStore().getType().equals("G01")){

        }else {


        }
        switch (list.get(position).getPayType()){
            case "1":
                holder.txtPrice.setText("¥ " + MoneyUtil.moneyFormatDouble(list.get(position).getPayAmount3() + list.get(position).getPayAmount2()));
                break;

            case "20":
                holder.txtPrice.setText("礼品券 " + MoneyUtil.moneyFormatDouble(list.get(position).getPayAmount1()));
                break;

            case "21":
                holder.txtPrice.setText("联盟券 " + MoneyUtil.moneyFormatDouble(list.get(position).getPayAmount1()));
                break;

            default:
                holder.txtPrice.setText("¥ " + MoneyUtil.moneyFormatDouble(list.get(position).getPayAmount1()));
                break;

        }

    }

    static class ViewHolder {
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.img_shopPic)
        ImageView imgShopPic;
        @InjectView(R.id.txt_shopName)
        TextView txtShopName;
        @InjectView(R.id.txt_status)
        TextView txtStatus;
        @InjectView(R.id.txt_discount)
        TextView txtDiscount;
        @InjectView(R.id.layout_discount)
        LinearLayout layoutDiscount;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.txt_real)
        TextView txtReal;
        @InjectView(R.id.txt_distance)
        TextView txtDistance;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
