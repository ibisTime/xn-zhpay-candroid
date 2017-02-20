package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DiscountModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/18.
 */

public class DiscountAdapter extends BaseAdapter {

    private List<DiscountModel> list;
    private Context context;
    private ViewHolder holder;

    public DiscountAdapter(Context context, List<DiscountModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_discount, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int i) {

        if (list.get(i).getStatus().equals("1")) { //已使用
            holder.imgZhang.setVisibility(View.VISIBLE);
            holder.imgZhang.setBackgroundResource(R.mipmap.discount_used);
            holder.layoutBg.setBackground(context.getResources().getDrawable(R.mipmap.discount_used_bg));
            holder.txtPhone.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtKey1.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtMark.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtKey2.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.graycc));

        } else if (list.get(i).getStatus().equals("0")) { // 未使用
            holder.imgZhang.setVisibility(View.GONE);
            holder.layoutBg.setBackground(context.getResources().getDrawable(R.mipmap.discount_unused_bg));
            holder.txtKey1.setTextColor(context.getResources().getColor(R.color.fontColor_support));
            holder.txtMark.setTextColor(context.getResources().getColor(R.color.fontColor_orange));
            holder.txtKey2.setTextColor(context.getResources().getColor(R.color.fontColor_orange));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.fontColor_gray));
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.fontColor_support));

        } else { // 已过期
            holder.imgZhang.setVisibility(View.VISIBLE);
            holder.imgZhang.setBackgroundResource(R.mipmap.discount_timeout);
            holder.layoutBg.setBackground(context.getResources().getDrawable(R.mipmap.discount_used_bg));
            holder.txtPhone.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtKey1.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtMark.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtKey2.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtName.setTextColor(context.getResources().getColor(R.color.graycc));
            holder.txtTime.setTextColor(context.getResources().getColor(R.color.graycc));
        }

        holder.txtKey1.setText("满" + (list.get(i).getTicketKey1() / 1000) + "元可用");
        holder.txtKey2.setText((list.get(i).getTicketKey2() / 1000) + "");

        holder.txtName.setText(list.get(i).getStore().getName());
        holder.txtPhone.setText(list.get(i).getStore().getBookMobile());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date d5 = new Date(list.get(i).getStoreTicket().getValidateEnd());
        holder.txtTime.setText("有效期至" + s.format(d5));

    }

    static class ViewHolder {
        @InjectView(R.id.txt_mark)
        TextView txtMark;
        @InjectView(R.id.txt_key2)
        TextView txtKey2;
        @InjectView(R.id.txt_key1)
        TextView txtKey1;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_phone)
        TextView txtPhone;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.img_zhang)
        ImageView imgZhang;
        @InjectView(R.id.layout_bg)
        LinearLayout layoutBg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
