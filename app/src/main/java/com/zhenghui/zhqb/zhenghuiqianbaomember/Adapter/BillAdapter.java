package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.BillModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.BillUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/18.
 */

public class BillAdapter extends BaseAdapter {

    private List<BillModel> list;
    private Context context;
    private ViewHolder holder;

    public BillAdapter(Context context, List<BillModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_bill, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {

        holder.txtTitle.setText(BillUtil.getBillType(list.get(position).getBizType()));
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getTransAmount()));
        holder.txtInfo.setText(list.get(position).getBizNote());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(list.get(position).getCreateDatetime());
        holder.txtTime.setText(s.format(d5));

    }

    static class ViewHolder {
        @InjectView(R.id.txt_title)
        TextView txtTitle;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.txt_info)
        TextView txtInfo;
        @InjectView(R.id.txt_time)
        TextView txtTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
