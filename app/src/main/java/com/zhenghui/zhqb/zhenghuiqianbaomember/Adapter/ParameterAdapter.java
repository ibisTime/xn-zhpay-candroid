package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/6/7.
 */

public class ParameterAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<GoodsModel.ProductSpecsListBean> list;

    public ParameterAdapter(Context context, List<GoodsModel.ProductSpecsListBean> list) {
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

        setView(i);

        return view;
    }

    public void setView(int i) {
        holder.txtParameter.setText(list.get(i).getName()+"    "+"重量:" + list.get(i).getWeight() + "Kg" + "    " + "发货地:" + list.get(i).getProvince());
        if (list.get(i).isSelect()) {
            holder.txtParameter.setTextColor(context.getResources().getColor(R.color.white));
            holder.layoutParameter.setBackground(context.getResources().getDrawable(R.drawable.corners_parameter));
        } else {
            holder.txtParameter.setTextColor(context.getResources().getColor(R.color.fontColor_gray));
            holder.layoutParameter.setBackground(context.getResources().getDrawable(R.drawable.corners_parameter_gray));
        }
    }

    static class ViewHolder {
        @InjectView(R.id.txt_parameter)
        TextView txtParameter;
        @InjectView(R.id.layout_parameter)
        LinearLayout layoutParameter;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
