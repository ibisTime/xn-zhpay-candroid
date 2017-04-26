package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.OrderModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/17.
 */

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
//        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order, null);
            holder = new ViewHolder(view);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }

//        holder.txtBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

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

        if(list.get(position).getProductOrderList().size() != 0){
            ImageUtil.glide(list.get(position).getProductOrderList().get(0).getProduct().getAdvPic(), holder.imgGood, context);
            holder.txtName.setText(list.get(position).getProductOrderList().get(0).getProduct().getName());
            holder.txtNumber.setText("X" + list.get(position).getProductOrderList().get(0).getQuantity() + "件");

            setPrice(position);
        }

        setTotalPrice(position);

    }

    private void setPrice(int position) {
        if(list.get(position).getProductOrderList().get(0).getPrice1() == 0){
            holder.txtRmb1.setVisibility(View.GONE);
            holder.txtRmb2.setVisibility(View.GONE);
        }else{
            holder.txtRmb1.setVisibility(View.VISIBLE);
            holder.txtRmb2.setVisibility(View.VISIBLE);
            holder.txtRmb1.setText("¥"+ MoneyUtil.moneyFormatDouble(list.get(position).getProductOrderList().get(0).getPrice1()));
            if(list.get(position).getProductOrderList().get(0).getPrice2() == 0 && list.get(position).getProductOrderList().get(0).getPrice3() == 0){
                holder.txtRmb2.setText("");
            }
        }

        if(list.get(position).getProductOrderList().get(0).getPrice2() == 0){
            holder.txtGwb1.setVisibility(View.GONE);
            holder.txtGwb2.setVisibility(View.GONE);
        }else{
            holder.txtGwb1.setVisibility(View.VISIBLE);
            holder.txtGwb2.setVisibility(View.VISIBLE);
            holder.txtGwb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getProductOrderList().get(0).getPrice2()));
            if(list.get(position).getProductOrderList().get(0).getPrice3() == 0){
                holder.txtGwb2.setText("购物币");
            }
        }

        if(list.get(position).getProductOrderList().get(0).getPrice3() == 0){
            holder.txtQbb1.setVisibility(View.GONE);
            holder.txtQbb2.setVisibility(View.GONE);
        }else{
            holder.txtQbb1.setVisibility(View.VISIBLE);
            holder.txtQbb2.setVisibility(View.VISIBLE);
            holder.txtQbb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getProductOrderList().get(0).getPrice3()));
        }
    }



    private void setTotalPrice(int position) {
        // 人名币总价
        double rmb = list.get(position).getAmount1();
        // 购物币总价
        double gwb = list.get(position).getAmount2();
        // 钱包币总价
        double qbb = list.get(position).getAmount3();

//        for (int i=0; i<list.get(position).getProductOrderList().size(); i++){
//            rmb = sums(rmb,list.get(position).getProductOrderList().get(i).getPrice1());
//            gwb = gwb + list.get(position).getProductOrderList().get(i).getPrice2();
//            qbb = qbb + list.get(position).getProductOrderList().get(i).getPrice3();
//        }

        if(rmb == 0){
            holder.txtRmb_1.setVisibility(View.GONE);
            holder.txtRmb_2.setVisibility(View.GONE);
            holder.txtRmb_3.setVisibility(View.GONE);
        }else{
            holder.txtRmb_1.setVisibility(View.VISIBLE);
            holder.txtRmb_2.setVisibility(View.VISIBLE);
            holder.txtRmb_3.setVisibility(View.VISIBLE);

            holder.txtRmb_2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if(gwb == 0 && qbb == 0){
                holder.txtRmb_3.setText("");
            }
        }

        if(gwb == 0){
            holder.txtGwb_1.setVisibility(View.GONE);
            holder.txtGwb_2.setVisibility(View.GONE);
            holder.txtGwb_3.setVisibility(View.GONE);
        }else{
            holder.txtGwb_1.setVisibility(View.VISIBLE);
            holder.txtGwb_2.setVisibility(View.VISIBLE);
            holder.txtGwb_3.setVisibility(View.VISIBLE);

            holder.txtGwb_2.setText(MoneyUtil.moneyFormatDouble(gwb));
            if(qbb == 0){
                holder.txtGwb_3.setText("");
            }
        }

        if(qbb == 0){
            holder.txtQbb_1.setVisibility(View.GONE);
            holder.txtQbb_2.setVisibility(View.GONE);
        }else{
            holder.txtQbb_1.setVisibility(View.VISIBLE);
            holder.txtQbb_2.setVisibility(View.VISIBLE);

            holder.txtQbb_2.setText(MoneyUtil.moneyFormatDouble(qbb));
        }
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
        @InjectView(R.id.txt_rmb1)
        TextView txtRmb1;
        @InjectView(R.id.txt_rmb2)
        TextView txtRmb2;
        @InjectView(R.id.txt_gwb1)
        TextView txtGwb1;
        @InjectView(R.id.txt_gwb2)
        TextView txtGwb2;
        @InjectView(R.id.txt_qbb1)
        TextView txtQbb1;
        @InjectView(R.id.txt_qbb2)
        TextView txtQbb2;
        @InjectView(R.id.layout_price)
        LinearLayout layoutPrice;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_totalPrice)
        TextView txtTotalPrice;
        @InjectView(R.id.txt_rmb_1)
        TextView txtRmb_1;
        @InjectView(R.id.txt_rmb_2)
        TextView txtRmb_2;
        @InjectView(R.id.txt_rmb_3)
        TextView txtRmb_3;
        @InjectView(R.id.txt_gwb_1)
        TextView txtGwb_1;
        @InjectView(R.id.txt_gwb_2)
        TextView txtGwb_2;
        @InjectView(R.id.txt_gwb_3)
        TextView txtGwb_3;
        @InjectView(R.id.txt_qbb_1)
        TextView txtQbb_1;
        @InjectView(R.id.txt_qbb_2)
        TextView txtQbb_2;
        @InjectView(R.id.txt_btn)
        TextView txtBtn;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public double sums(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }
}
