package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.TargetModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/2/21.
 */

public class TagetAdapter extends BaseAdapter {

    private List<TargetModel> list;
    private Context context;

    private ViewHolder holder;

    public TagetAdapter(Context context, List<TargetModel> list) {
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
        view = LayoutInflater.from(context).inflate(R.layout.item_target, null);
        holder = new ViewHolder(view);

        setView(i);

        return view;
    }

    private void setView(int i) {

        holder.txtName.setText(MoneyUtil.moneyFormatDouble(list.get(i).getToAmount())+getCurrency(list.get(i).getToCurrency()));
        holder.txtTime.setText("第"+list.get(i).getPeriods()+"期");
        holder.txtSum.setText(list.get(i).getTotalNum()+"");
        holder.txtResidue.setText((list.get(i).getTotalNum()-list.get(i).getInvestNum())+"");
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(i).getFromAmount())+getCurrency(list.get(i).getFromCurrency()));

        holder.barSchedule.setMax(list.get(i).getTotalNum());
        holder.barSchedule.setProgress(list.get(i).getInvestNum());

        if(i > 2){
            if(i%3 == 0){
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_orange);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_orange));
            }else if(i%3 == 1){
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_yellow);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_yellow));
            }else if(i%3 == 2) {
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_green);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_green));
            }
        }else {
            if(i == 0){
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_orange);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_orange));
            }else if(i == 1){
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_yellow);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_yellow));
            }else if(i == 2) {
                holder.layoutBg.setBackgroundResource(R.mipmap.target_bg_green);
                holder.barSchedule.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_green));
            }
        }

    }

    private String getCurrency(String currency){
        String type = "";
        switch (currency){
            case "FRB":
                type = "分润";
                break;
            case "GXJL":
                type = "贡献奖励";
                break;
            case "GWB":
                type = "购物币";
                break;
            case "QBB":
                type = "钱包币";
                break;
            case "HBB":
                type = "红包";
                break;
            case "HBYJ":
                type = "红包业绩";
                break;
            case "CNY":
                type = "人民币";
                break;
        }
        return type;
    }

    static class ViewHolder {
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.bar_schedule)
        ProgressBar barSchedule;
        @InjectView(R.id.txt_sum)
        TextView txtSum;
        @InjectView(R.id.txt_residue)
        TextView txtResidue;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.layout_bg)
        LinearLayout layoutBg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
