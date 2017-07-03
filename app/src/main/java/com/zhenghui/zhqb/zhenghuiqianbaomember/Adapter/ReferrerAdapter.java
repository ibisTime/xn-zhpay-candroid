package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.FriendModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReferrerAdapter extends BaseAdapter {

    private List<FriendModel> list;
    private String level;
    private Context context;
    private ViewHolder holder;

    public ReferrerAdapter(Context context, List<FriendModel> list, String level) {
        this.list = list;
        this.level = level;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_friend, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {

        if (level.equals("-1")) {
            holder.txtType.setText("P1");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_red));
        } else if (level.equals("1")) {
            holder.txtType.setText("C1");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_green));
        } else if (level.equals("2")) {
            holder.txtType.setText("C2");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_blue));
        } else if (level.equals("3")) {
            holder.txtType.setText("C3");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_orange));
        }


        holder.txtName.setText(list.get(position).getNickname());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date(list.get(position).getCreateDatetime());
        holder.txtTime.setText("加入时间:"+format.format(date));

        ImageUtil.photo(list.get(position).getUserExt().getPhoto(), holder.imgPhoto, context);


    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_point)
        TextView txtPoint;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_type)
        TextView txtType;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.layout_bg)
        LinearLayout layoutBg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
