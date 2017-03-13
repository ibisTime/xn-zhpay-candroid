package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.JewelRecordModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/2/28.
 */

public class JewelRecordAdapter extends BaseAdapter {

    private ViewHolder holder;

    private Context context;
    private List<JewelRecordModel> list;

    public JewelRecordAdapter(Context context, List<JewelRecordModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_jewel_record, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int i) {
        holder.txtStatus.setVisibility(View.VISIBLE);
        if(list.get(i).getStatus().equals("2")){
            holder.txtStatus.setText("中奖");
            holder.txtContent.setText(Html.fromHtml("中得<html><font color=\"#fe4332\">"+
                    (list.get(i).getJewel().getAmount()/1000)+
                    "</font></html>"+getCurrency(list.get(i).getJewel().getCurrency())));
        }else{
            holder.txtStatus.setText("参与");
            holder.txtContent.setText(Html.fromHtml("参与<html><font color=\"#fe4332\">"+
                    list.get(i).getTimes()+ "</font></html>次夺宝"));
        }
        holder.txtPhone.setText(list.get(i).getMobile().substring(0,3)+"****"+list.get(i).getMobile().substring(7,list.get(i).getMobile().length()));
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
        }
        return type;
    }

    static class ViewHolder {
        @InjectView(R.id.txt_status)
        TextView txtStatus;
        @InjectView(R.id.txt_phone)
        TextView txtPhone;
        @InjectView(R.id.txt_content)
        TextView txtContent;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
