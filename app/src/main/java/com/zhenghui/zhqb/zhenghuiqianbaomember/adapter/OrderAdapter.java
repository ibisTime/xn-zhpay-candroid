package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrderAdapter extends BaseAdapter {

    private List<OrderModel> list;
    private Context context;
    private ViewHolder holder;

    public OrderAdapter(Context context, List<OrderModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_order, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        setView(i);

        return view;
    }

    public void setView(int position) {
        holder.txtOrderId.setText(list.get(position).getCode());

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date d5 = new Date(list.get(position).getApplyDatetime());
        holder.txtTime.setText(s.format(d5));

        //1待支付 2 已支付待发货 3 已发货待收货 4 已收货 91用户取消 92 商户取消 93 快递异常
        if (list.get(position).getStatus().equals("1")) { // 待支付
            holder.txtBtn.setText("待支付");
        } else if (list.get(position).getStatus().equals("2")) { // 已支付
            holder.txtBtn.setText("待发货");
        } else if (list.get(position).getStatus().equals("3")) { // 已发货
            holder.txtBtn.setText("去收货");
        } else if (list.get(position).getStatus().equals("4")) { // 已收货
            holder.txtBtn.setText("已收货");
        } else if (list.get(position).getStatus().equals("91")) { // 用户取消
            holder.txtBtn.setText("已取消");
        } else if (list.get(position).getStatus().equals("92")) { // 商户取消
            holder.txtBtn.setText("已取消");
        } else if (list.get(position).getStatus().equals("93")) {
            holder.txtBtn.setText("快递异常");
        }

        ImageUtil.glide(list.get(position).getProduct().getAdvPic(), holder.imgGood, context);
        holder.txtName.setText(list.get(position).getProduct().getName());
        holder.txtNumber.setText("X" + list.get(position).getQuantity() + "件");

        holder.txtParameter.setText(list.get(position).getProductSpecsName());

        holder.txtYunfei.setText(MoneyUtil.moneyFormatDouble(list.get(position).getYunfei()));

        setPrice(position);

    }


    private void setPrice(int position) {

        if (list.get(position).getStore().getType().equals("G01")){
            holder.txtCurrency.setText("礼品券");
            holder.txtTotalCurrency.setText("礼品券");
        }else {
            if (list.get(position).getProduct().getPayCurrency().equals("4")){
                holder.txtCurrency.setText("钱包币");
                holder.txtTotalCurrency.setText("钱包币");
            }else{
                holder.txtCurrency.setText("¥");
                holder.txtTotalCurrency.setText("¥");
            }
        }

        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));
        holder.txtTotalPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getAmount1()));


    }


    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public double sums(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    static class ViewHolder {
        @InjectView(R.id.txt_orderId)
        TextView txtOrderId;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.img_good)
        ImageView imgGood;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_currency)
        TextView txtCurrency;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.layout_title)
        LinearLayout layoutTitle;
        @InjectView(R.id.txt_parameter)
        TextView txtParameter;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_total_currency)
        TextView txtTotalCurrency;
        @InjectView(R.id.txt_total_price)
        TextView txtTotalPrice;
        @InjectView(R.id.txt_yunfei)
        TextView txtYunfei;
        @InjectView(R.id.txt_btn)
        TextView txtBtn;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
