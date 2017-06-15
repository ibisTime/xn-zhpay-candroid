package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

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

        ImageUtil.glide(list.get(position).getAdvPic(),holder.imgGoodPic,context);


        if(list.get(position).getProductSpecsList().get(0).getPrice1() == 0){
            holder.txtRmb1.setVisibility(View.GONE);
            holder.txtRmb2.setVisibility(View.GONE);
        }else{
            holder.txtRmb1.setVisibility(View.VISIBLE);
            holder.txtRmb2.setVisibility(View.VISIBLE);
            holder.txtRmb1.setText("¥"+MoneyUtil.moneyFormatDouble(list.get(position).getProductSpecsList().get(0).getPrice1()));
            if(list.get(position).getProductSpecsList().get(0).getPrice2() == 0 && list.get(position).getProductSpecsList().get(0).getPrice3() == 0){
                holder.txtRmb2.setText("");
            }else {
                holder.txtRmb2.setText("+");
            }
        }

        if(list.get(position).getProductSpecsList().get(0).getPrice2() == 0){
            holder.txtGwb1.setVisibility(View.GONE);
            holder.txtGwb2.setVisibility(View.GONE);
        }else{
            holder.txtGwb1.setVisibility(View.VISIBLE);
            holder.txtGwb2.setVisibility(View.VISIBLE);
            holder.txtGwb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getProductSpecsList().get(0).getPrice2()));
            if(list.get(position).getProductSpecsList().get(0).getPrice3() == 0){
                holder.txtGwb2.setText("购物币");
            }else{
                holder.txtGwb2.setText("购物币 + ");
            }
        }

        if(list.get(position).getProductSpecsList().get(0).getPrice3() == 0){
            holder.txtQbb1.setVisibility(View.GONE);
            holder.txtQbb2.setVisibility(View.GONE);
        }else{
            holder.txtQbb1.setVisibility(View.VISIBLE);
            holder.txtQbb2.setVisibility(View.VISIBLE);
            holder.txtQbb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getProductSpecsList().get(0).getPrice3()));
        }

        if(list.get(position).getProductSpecsList().get(0).getPrice3() == 0
                && list.get(position).getProductSpecsList().get(0).getPrice2() == 0
                && list.get(position).getProductSpecsList().get(0).getPrice1() == 0){
            holder.txtRmb1.setText("0");
            holder.txtRmb1.setVisibility(View.VISIBLE);
        }

    }

    static class ViewHolder {
        @InjectView(R.id.img_goodPic)
        ImageView imgGoodPic;
        @InjectView(R.id.txt_goodame)
        TextView txtGoodame;
        @InjectView(R.id.txt_info)
        TextView txtInfo;
        @InjectView(R.id.txt_gwb1)
        TextView txtGwb1;
        @InjectView(R.id.txt_gwb2)
        TextView txtGwb2;
        @InjectView(R.id.txt_rmb1)
        TextView txtRmb1;
        @InjectView(R.id.txt_rmb2)
        TextView txtRmb2;
        @InjectView(R.id.txt_qbb1)
        TextView txtQbb1;
        @InjectView(R.id.txt_qbb2)
        TextView txtQbb2;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
