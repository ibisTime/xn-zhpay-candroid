package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class ShopAdapter extends BaseAdapter {

    private List<ShopModel> list;
    private Context context;
    private ViewHolder holder;

    public ShopAdapter(Context context, List<ShopModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_shop, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        ImageUtil.glide(list.get(position).getAdvPic(), holder.imgShopPic, context);
        holder.txtShopName.setText(list.get(position).getName());

        if (list.get(position).getLevel().equals("2")) {
            holder.txtType.setText("公益商家");
        } else {
            holder.txtType.setText("普通商家");
        }

        holder.txtInfo.setText(list.get(position).getSlogan());

        if (list.get(position).getDistance() != null) {
            holder.txtDistance.setText((Integer.parseInt(list.get(position).getDistance()) / 1000) + "KM");
        }
    }

    static class ViewHolder {
        @InjectView(R.id.img_shopPic)
        ImageView imgShopPic;
        @InjectView(R.id.txt_shopName)
        TextView txtShopName;
        @InjectView(R.id.txt_info)
        TextView txtInfo;
        @InjectView(R.id.txt_type)
        TextView txtType;
        @InjectView(R.id.txt_distance)
        TextView txtDistance;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
