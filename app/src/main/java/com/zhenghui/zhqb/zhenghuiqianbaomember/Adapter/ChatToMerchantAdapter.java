package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/14.
 */

public class ChatToMerchantAdapter extends BaseAdapter {

    private List<ShopModel> list;
    private Context context;
    private ViewHolder holder;

    public ChatToMerchantAdapter(Context context, List<ShopModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_merchant, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_money)
        TextView txtMoney;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
