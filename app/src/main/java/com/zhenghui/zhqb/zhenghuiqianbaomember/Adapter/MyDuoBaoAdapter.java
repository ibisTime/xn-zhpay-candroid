package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.DuoBaoActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MyDuoBaoDetailActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class MyDuoBaoAdapter extends BaseAdapter {

    private boolean isWin;
    private Context context;
    private ViewHolder holder;
    private List<MyDuoBaoModel> list;

    private SharedPreferences userInfo;

    public MyDuoBaoAdapter(Context context, List<MyDuoBaoModel> list, boolean isWin) {
        this.list = list;
        this.isWin = isWin;
        this.context = context;
        userInfo = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_duobao, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.txtStatus.getText().equals("追加")) {
                    context.startActivity(new Intent(context, DuoBaoActivity.class).putExtra("code", list.get(i).getJewel().getCode()));
                }
            }
        });

        holder.txtMyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MyDuoBaoDetailActivity.class).putExtra("model", list.get(i)));
            }
        });

        initView(i);

        return view;
    }

    private void initView(int position) {
        ImageUtil.glide(list.get(position).getJewel().getAdvPic(), holder.imgGoodPic, context);

        holder.txtGoodName.setText(list.get(position).getJewel().getName());
        holder.txtInfo.setText(list.get(position).getJewel().getSlogan());

        if (list.get(position).getJewel().getPrice1() == 0) {
            holder.layoutRmb.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getJewel().getPrice2() == 0) {
            holder.layoutGwb.setVisibility(View.INVISIBLE);
        }
        if (list.get(position).getJewel().getPrice3() == 0) {
            holder.layoutQbb.setVisibility(View.INVISIBLE);
        }

        holder.txtRmb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getJewel().getPrice1()));
        holder.txtGwb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getJewel().getPrice2()));
        holder.txtQbb.setText(MoneyUtil.moneyFormatDouble(list.get(position).getJewel().getPrice3()));

        holder.txtNumber.setText(list.get(position).getJewel().getInvestNum() + "次");
        int percent = (int) (list.get(position).getJewel().getInvestNum() * 100 / list.get(position).getJewel().getTotalNum());
        holder.txtSchedule.setText(percent + "%");

        if (isWin) {

            if (list.get(position).getStatus().equals("2")) {
                holder.txtStatus.setText("待确认地址");
            } else if (list.get(position).getStatus().equals("4")) {
                holder.txtStatus.setText("待发货");
            } else if (list.get(position).getStatus().equals("5")) {
                holder.txtStatus.setText("已发货");
            } else if (list.get(position).getStatus().equals("6")) { // 已签收
                holder.txtStatus.setText("给好评");
            }

        } else {
            String status = list.get(position).getJewel().getStatus();
            if (status.equals("5")) {

                holder.txtStatus.setText("到期流标");

            } else {

                if (list.get(position).getJewel().getWinNumber() == null) { //进行中
                    holder.txtStatus.setText("追加");
                } else { // 已揭晓
                    if (list.get(position).getJewel().getWinUserId().equals(userInfo.getString("userId", null))) { // 已中奖
                        holder.txtStatus.setText("已中奖");
                    } else {
                        holder.txtStatus.setText("未中奖");
                    }
                }

            }
        }

        holder.barSchedule.setMax((int) (list.get(position).getJewel().getTotalNum()));
        holder.barSchedule.setProgress(list.get(position).getJewel().getInvestNum());

    }


    static class ViewHolder {
        @InjectView(R.id.img_goodPic)
        ImageView imgGoodPic;
        @InjectView(R.id.txt_goodName)
        TextView txtGoodName;
        @InjectView(R.id.txt_status)
        TextView txtStatus;
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
        @InjectView(R.id.txt_myNumber)
        TextView txtMyNumber;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
