package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/23.
 */

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
        ImageUtil.glide(list.get(position).getStore().getAdPic(), holder.imgShopPic, context);

        holder.txtNumber.setText(list.get(position).getCode());

        holder.txtShopName.setText(list.get(position).getStore().getName());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date d5 = new Date(list.get(position).getCreateDatetime());
        holder.txtTime.setText(s.format(d5));

        if (list.get(position).getStatus().equals("0")) {
            holder.txtStatus.setText("待支付");
        } else if (list.get(position).getStatus().equals("1")) {
            holder.txtStatus.setText("已支付");
        } else {
            holder.txtStatus.setText("已取消");
        }

        if (list.get(position).getStoreTicket() == null) {
            holder.layoutDiscount.setVisibility(View.INVISIBLE);
        } else {
            double key1 = list.get(position).getStoreTicket().getKey1();
            double key2 = list.get(position).getStoreTicket().getKey2();
            holder.txtDiscount.setText("满"+MoneyUtil.moneyFormatDouble(key1)+"减"+MoneyUtil.moneyFormatDouble(key2));
        }

        holder.txtPrice.setText("消费: ¥" + MoneyUtil.moneyFormatDouble(list.get(position).getAmount1()));

        if(list.get(position).getPurchaseAmount() != 0){
            holder.txtReal.setText("实付:"+MoneyUtil.moneyFormatDouble(list.get(position).getPurchaseAmount()));
//            if (list.get(position).getPayType().equals("1")) {
//                holder.txtReal.setText("实付:"+MoneyUtil.moneyFormatDouble(list.get(position).getAmount2() + list.get(position).getAmount3()));
//            } else {
//                holder.txtReal.setText("实付:"+MoneyUtil.moneyFormatDouble(list.get(position).getAmount1()));
//            }
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
