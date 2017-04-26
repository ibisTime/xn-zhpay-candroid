package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.Model;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GridAdapter extends BaseAdapter {

    private List<Model> modelList;
    private Context context;
    private ViewHolder holder;

    public GridAdapter(Context context, List<Model> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setView(position);

        return convertView;
    }

    private void setView(int position) {
        holder.txtTitle.setText(modelList.get(position).getTitle());

        ImageUtil.glide(modelList.get(position).getImage(),holder.imgImage,context);
    }


    static class ViewHolder {
        @InjectView(R.id.img_image)
        ImageView imgImage;
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.layout_layout)
        LinearLayout layoutLayout;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
