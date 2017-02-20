package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MyDuoBaoNumActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoDetailModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by LeiQ on 2017/1/12.
 */

public class MyDuoBaoDetailAdapter extends BaseAdapter {

    private List<MyDuoBaoDetailModel> list;
    private Context context;
    private ViewHolder holder;

    public MyDuoBaoDetailAdapter(Context context, List<MyDuoBaoDetailModel> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_my_duobao_detail, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MyDuoBaoNumActivity.class)
                        .putExtra("code",list.get(i).getCode())
                        .putExtra("name",list.get(i).getJewel().getName()));
            }
        });

        setView(i);

        return view;
    }

    public void setView(int position) {

        if(list.get(position).getCreateDatetime() != null){
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d5 = new Date(list.get(position).getCreateDatetime());
            holder.txtTime.setText(s.format(d5));
        }


        holder.txtNumber.setText(Html.fromHtml("<html><font corlor=\"#FE4332\">"+list.get(position).getTimes()+"</font></html>"+"人次"));

    }

    static class ViewHolder {
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_check)
        TextView txtCheck;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
