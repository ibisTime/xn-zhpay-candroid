package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GiveModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GiveAdapter extends BaseAdapter {

    private List<GiveModel> list;
    private Context context;

    private ViewHolder holder;

    public GiveAdapter(Context context, List<GiveModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_give, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int i) {

        if(list.get(i).getStatus().equals("0")){ // 待发送
            holder.layoutBg.setBackgroundResource(R.mipmap.give_bg_green);
            holder.txtGeter.setText("领取人：待发送");
        } else if(list.get(i).getStatus().equals("1")){ // 已发送,待领取
            holder.layoutBg.setBackgroundResource(R.mipmap.give_bg_orange);
            holder.txtGeter.setText("领取人：已发送，待领取");
        }else if(list.get(i).getStatus().equals("2")){ // 已领取
            holder.layoutBg.setBackgroundResource(R.mipmap.give_bg_red);
            holder.txtGeter.setText("领取人："+list.get(i).getReceiverUser().getMobile());
        }else if(list.get(i).getStatus().equals("3")){ // 已失效
            holder.layoutBg.setBackgroundResource(R.mipmap.give_bg_red);
            holder.txtGeter.setText("已失效");
        }

        holder.txtName.setText(list.get(i).getSlogan());
        holder.txtCode.setText("红包编号："+list.get(i).getCode());


    }

    static class ViewHolder {
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_code)
        TextView txtCode;
        @InjectView(R.id.txt_geter)
        TextView txtGeter;
        @InjectView(R.id.layout_bg)
        LinearLayout layoutBg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
