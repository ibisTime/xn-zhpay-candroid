package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class DuoBaoAdapter extends BaseAdapter {

    private List<DuoBaoModel> list;
    private Context context;
    private ViewHolder holder;

    public DuoBaoAdapter(Context context, List<DuoBaoModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_duobao, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        initView(i);

        return view;
    }

    private void initView(int position) {
        ImageUtil.glide(list.get(position).getAdvPic().split("\\|\\|")[0], holder.imgGoodPic, context);

        holder.txtGoodName.setText(list.get(position).getName());
        holder.txtInfo.setText(list.get(position).getSlogan());

        if(list.get(position).getPrice1() == 0){
            holder.layoutRmb.setVisibility(View.INVISIBLE);
        }
        if(list.get(position).getPrice2() == 0){
            holder.layoutGwb.setVisibility(View.INVISIBLE);
        }
        if(list.get(position).getPrice3() == 0){
            holder.layoutQbb.setVisibility(View.INVISIBLE);
        }

        holder.txtRmb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));
        holder.txtGwb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice2()));
        holder.txtQbb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice3()));


        holder.txtDay.setText(list.get(position).getRaiseDays() + "天");
        holder.txtNumber.setText(list.get(position).getInvestNum() + "人");

        int percent = (list.get(position).getInvestNum() * 100 / list.get(position).getTotalNum());
        holder.txtSchedule.setText(percent + "%");

        holder.barSchedule.setMax((int) (list.get(position).getTotalNum()));
        holder.barSchedule.setProgress(list.get(position).getInvestNum());

    }

    static class ViewHolder {
        @InjectView(R.id.img_goodPic)
        ImageView imgGoodPic;
        @InjectView(R.id.txt_goodName)
        TextView txtGoodName;
        @InjectView(R.id.txt_info)
        TextView txtInfo;
        @InjectView(R.id.bar_schedule)
        ProgressBar barSchedule;
        @InjectView(R.id.txt_schedule)
        TextView txtSchedule;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
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
        @InjectView(R.id.txt_day)
        TextView txtDay;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
