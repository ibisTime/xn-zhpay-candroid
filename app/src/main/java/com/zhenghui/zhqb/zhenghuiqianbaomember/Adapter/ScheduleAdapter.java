package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ScheduleModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class ScheduleAdapter extends BaseAdapter {

    private List<ScheduleModel> list;
    private Context context;
    private ViewHolder holder;

    public ScheduleAdapter(Context context, List<ScheduleModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_schedule, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        ImageUtil.photo(list.get(position).getPhoto(), holder.imgPhoto, context);

        if (list.get(position).getPayAmount1() == 0) {
            holder.layoutRmb.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getPayAmount2() == 0) {
            holder.layoutGwb.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getPayAmount3() == 0) {
            holder.layoutQbb.setVisibility(View.INVISIBLE);
        }

        holder.txtRmb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPayAmount1()));
        holder.txtGwb.setText(list.get(position).getPayAmount2() / 1000 + "");
        holder.txtQbb.setText(list.get(position).getPayAmount3() / 1000 + "");

        holder.txtName.setText(list.get(position).getNickname());

        if(list.get(position).getCreateDatetime() != null){
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date d5 = new Date(list.get(position).getCreateDatetime());
            holder.txtTime.setText("购买时间: " + s.format(d5));
        }

    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_rmb)
        TextView txtRmb;
        @InjectView(R.id.layout_rmb)
        LinearLayout layoutRmb;
        @InjectView(R.id.txt_gwb)
        TextView txtGwb;
        @InjectView(R.id.layout_gwb)
        LinearLayout layoutGwb;
        @InjectView(R.id.txt_qbb)
        TextView txtQbb;
        @InjectView(R.id.layout_qbb)
        LinearLayout layoutQbb;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
