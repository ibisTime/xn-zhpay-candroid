package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by LeiQ on 2017/1/18.
 */

public class DetailAdapter extends BaseAdapter {

    private ViewHolder holder;

    private String[] pic;
    private Context context;

    public DetailAdapter(Context context, String[] pic) {
        this.pic = pic;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pic.length;
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
        view = LayoutInflater.from(context).inflate(R.layout.item_detail, null);
        holder = new ViewHolder(view);

        ImageUtil.picasso(pic[i], holder.img,context);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
