package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.JewelRecordModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class JewelAdapter extends BaseAdapter {

    private List<JewelRecordModel> list;
    private Context context;

    private ViewHolder holder;

    public JewelAdapter(Context context, List<JewelRecordModel> list) {
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_jewel, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();

        setView(i);

        return view;
    }

    private void setView(int i) {
        holder.txtName.setText(list.get(i).getUser().getNickname());
        ImageUtil.photo(list.get(i).getUser().getPhoto(),holder.imgPhoto,context);
        holder.txtIp.setText(list.get(i).getIp());

        if(list.get(i).getStatus().equals("2")){
            holder.txtAward.setText(Html.fromHtml("中得<html><font color=\"#fe4332\">"+
                    MoneyUtil.moneyFormatDouble(list.get(i).getJewel().getToAmount())+
                    "</font></html>"+getCurrency(list.get(i).getJewel().getToCurrency())));
        }else{
            holder.txtAward.setText(Html.fromHtml("参与了<html><font color=\"#fe4332\">"+
                    list.get(i).getTimes()+ "</font></html>人次"));
        }

        SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = new Date(list.get(i).getInvestDatetime());


        holder.txtTime.setText(list.get(i).getPayDatetime());
        holder.txtDate.setText(s2.format(d1));

        if(i == 0){
            holder.layoutDate.setVisibility(View.VISIBLE);
        }else{
            Date d2 = new Date(list.get(i-1).getInvestDatetime());
            if(s2.format(d1).equals(s2.format(d2))){
                holder.layoutDate.setVisibility(View.GONE);
            }else{
                holder.layoutDate.setVisibility(View.VISIBLE);
            }
        }

    }

    private String getLocation(String ip){

        final String[] location = {""};

        new Xutil().getLocation(ip, new Xutil.XUtils3CallBackGet() {
            @Override
            public void onSuccess(String result) {

                location[0] = result;

            }

            @Override
            public void onTip(String tip) {

            }

            @Override
            public void onError(String error, boolean isOnCallback) {

            }
        });

        return location[0];
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
        @InjectView(R.id.txt_date)
        TextView txtDate;
        @InjectView(R.id.layout_date)
        LinearLayout layoutDate;
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_ip)
        TextView txtIp;
        @InjectView(R.id.txt_award)
        TextView txtAward;
        @InjectView(R.id.txt_time)
        TextView txtTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
