package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/1/18.
 */

public class ParameterAdapter extends BaseAdapter {

    private ViewHolder holder;

    private List<GoodsModel.ProductSpecsBean> list;
    private Context context;

    public ParameterAdapter(Context context, List<GoodsModel.ProductSpecsBean> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_parameter, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtKey.setText(list.get(i).getDkey());
        holder.txtValue.setText(list.get(i).getDvalue());


        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.txt_key)
        TextView txtKey;
        @InjectView(R.id.txt_value)
        TextView txtValue;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
