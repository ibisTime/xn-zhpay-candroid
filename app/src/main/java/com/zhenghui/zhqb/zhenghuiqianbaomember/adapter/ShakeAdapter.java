package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShakeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShakeAdapter extends BaseAdapter {

    private Context context;
    private List<ShakeModel> list;
    private ViewHolder holder;

    public ShakeAdapter(Context context, List<ShakeModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_tree, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    public void setView(int position) {

        holder.txtName.setText(list.get(position).getUser().getNickname()+"的");
        if(list.get(position).getDistance().length() > 3){
            holder.txtDistance.setText((Integer.parseInt(list.get(position).getDistance())/1000)+"KM");
        }else{
            holder.txtDistance.setText(list.get(position).getDistance()+"M");
        }

    }

    static class ViewHolder {
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_distance)
        TextView txtDistance;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
