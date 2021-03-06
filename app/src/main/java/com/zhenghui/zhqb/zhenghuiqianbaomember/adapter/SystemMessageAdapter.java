package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MessageModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SystemMessageAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<MessageModel> list;

    public SystemMessageAdapter(Context context, List<MessageModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_system_message, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        holder.txtTitle.setText(list.get(position).getSmsTitle());
        holder.txtContent.setText(list.get(position).getSmsContent());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date d5 = new Date(list.get(position).getPushedDatetime());
        holder.txtTime.setText(s.format(d5));

    }

    static class ViewHolder {
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_content)
        TextView txtContent;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
