package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ProductModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<ProductModel> list;

    public ProductAdapter(Context context, List<ProductModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_product, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {
        holder.txtName.setText(list.get(position).getProductName());
        holder.txtNumber.setText("X" + list.get(position).getProductNumber());
        holder.txtParameter.setText(list.get(position).getProductSpecsName());
        ImageUtil.glide(list.get(position).getProductImage(), holder.imgProduct, context);

        if (list.get(position).getType().equals("G01")){
            holder.txtCurrency.setText("礼品券");
        }else {
            if (list.get(position).getPayCurrency().equals("4")){
                holder.txtCurrency.setText("钱包币");
            }else{
                holder.txtCurrency.setText("¥");
            }
        }
        holder.txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));

    }


    static class ViewHolder {
        @InjectView(R.id.img_product)
        ImageView imgProduct;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_currency)
        TextView txtCurrency;
        @InjectView(R.id.txt_price)
        TextView txtPrice;
        @InjectView(R.id.txt_parameter)
        TextView txtParameter;
        @InjectView(R.id.txt_number)
        TextView txtNumber;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
