package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GoodAdapter extends BaseAdapter {

    private List<GoodsModel> list;
    private Context context;
    private ViewHolder holder;

    public GoodAdapter(Context context, List<GoodsModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_zeroyuan, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        holder.txtGoodame.setText(list.get(position).getName());
        holder.txtInfo.setText(list.get(position).getSlogan());

        ImageUtil.glide(list.get(position).getAdvPic(), holder.imgGoodPic, context);

        switch (list.get(position).getStore().getType()) {

            case "G01":
                holder.txtCurrency.setText("礼品券");
                break;

            default:
                holder.txtCurrency.setText("¥");
                break;

        }
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getProductSpecsList().get(0).getPrice1()));

    }

    static class ViewHolder {
        @InjectView(R.id.img_goodPic)
        ImageView imgGoodPic;
        @InjectView(R.id.txt_goodame)
        TextView txtGoodame;
        @InjectView(R.id.txt_info)
        TextView txtInfo;
        @InjectView(R.id.txt_currency)
        TextView txtCurrency;
        @InjectView(R.id.txt_price)
        TextView txtPrice;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
