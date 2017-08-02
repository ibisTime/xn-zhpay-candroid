package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.RightsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RightsAdapter extends BaseAdapter {

    private List<RightsModel> list;
    private Context context;
    private ViewHolder holder;

    public RightsAdapter(Context context, List<RightsModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_rights, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    public void setView(int i) {
        if(list.get(i).getStatus().equals("3")) {
            holder.txtStatus.setText("生效中");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.fontColor_orange));
        }if(list.get(i).getStatus().equals("2")){
            holder.txtStatus.setText("待生效");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.fontColor_brown));
        }if(list.get(i).getStatus().equals("4")){
            holder.txtStatus.setText("已生效");
            holder.txtStatus.setTextColor(context.getResources().getColor(R.color.fontColor_support));
        }

        holder.txtCode.setText(list.get(i).getCode().substring(list.get(i).getCode().length()-6,list.get(i).getCode().length()));
        holder.txtToday.setText(MoneyUtil.moneyFormatDouble(list.get(i).getTodayAmount()));
        holder.txtTotal.setText(MoneyUtil.moneyFormatDouble(list.get(i).getProfitAmount()));
        holder.txtHas.setText(MoneyUtil.moneyFormatDouble(list.get(i).getBackAmount()));
        holder.txtCondition.setText(MoneyUtil.moneyFormatDouble(list.get(i).getCostAmount())+"交易额");
        if(list.get(i).getCreateDatetime() != null){
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d5 = new Date(list.get(i).getCreateDatetime());
            holder.txtDate.setText(s.format(d5));
        }
    }

    static class ViewHolder {
        @InjectView(R.id.txt_today)
        TextView txtToday;
        @InjectView(R.id.txt_total)
        TextView txtTotal;
        @InjectView(R.id.txt_condition)
        TextView txtCondition;
        @InjectView(R.id.txt_status)
        TextView txtStatus;
        @InjectView(R.id.txt_has)
        TextView txtHas;
        @InjectView(R.id.txt_date)
        TextView txtDate;
        @InjectView(R.id.txt_code)
        TextView txtCode;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
