package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShoppingCartActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShoppingCartModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/15.
 */

public class ShoppingCartAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private ShoppingCartActivity activity;

    private List<ShoppingCartModel.ListBean> list;

    private SharedPreferences userInfoSp;

    public ShoppingCartAdapter(Context context, List<ShoppingCartModel.ListBean> list, ShoppingCartActivity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;

        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_shopcart, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tip(i);
            }
        });

        holder.txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(i).setQuantity(((int) list.get(i).getQuantity()) + 1);
                modifyProductNum(i);
            }
        });

        holder.txtSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(i).getQuantity() > 1) {
                    list.get(i).setQuantity(((int) list.get(i).getQuantity()) - 1);
                    modifyProductNum(i);
                } else {
                    Toast.makeText(context, "商品已达到最小数量", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.get(i).isChoose()) {
                    list.get(i).setChoose(false);
                } else {
                    for(ShoppingCartModel.ListBean bean : list){
                        bean.setChoose(false);
                    }

                    list.get(i).setChoose(true);
                }
                notifyDataSetChanged();
                activity.getTotalPrice();
            }
        });

        setView(i);

        return view;
    }

    private void setView(int position) {
        holder.txtName.setText(list.get(position).getProductName());
        holder.txtNumber.setText(((int) list.get(position).getQuantity()) + "");

        ImageUtil.glide(list.get(position).getAdvPic(), holder.imgGood, context);

        if (list.get(position).isChoose()) {
            holder.imgChoose.setBackground(context.getResources().getDrawable(R.mipmap.shopcart_choose));
        } else {
            holder.imgChoose.setBackground(context.getResources().getDrawable(R.mipmap.shopcart_unchoose));
        }

        if(list.get(position).getPrice1() == 0){
            holder.txtRmb1.setVisibility(View.GONE);
            holder.txtRmb2.setVisibility(View.GONE);
        }else{
            holder.txtRmb1.setVisibility(View.VISIBLE);
            holder.txtRmb2.setVisibility(View.VISIBLE);
            holder.txtRmb1.setText("¥"+MoneyUtil.moneyFormatDouble(list.get(position).getPrice1()));
            if(list.get(position).getPrice2() == 0 && list.get(position).getPrice3() == 0){
                holder.txtRmb2.setText("");
            }

        }

        if(list.get(position).getPrice2() == 0){
            holder.txtGwb1.setVisibility(View.GONE);
            holder.txtGwb2.setVisibility(View.GONE);
        }else{
            holder.txtGwb1.setVisibility(View.VISIBLE);
            holder.txtGwb2.setVisibility(View.VISIBLE);
            holder.txtGwb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice2()));
            if(list.get(position).getPrice3() == 0){
                holder.txtGwb2.setText("购物币");
            } else{
                holder.txtRmb2.setText("购物币 + ");
            }
        }

        if(list.get(position).getPrice3() == 0){
            holder.txtQbb1.setVisibility(View.GONE);
            holder.txtQbb2.setVisibility(View.GONE);
        }else{
            holder.txtQbb1.setVisibility(View.VISIBLE);
            holder.txtQbb2.setVisibility(View.VISIBLE);
            holder.txtQbb1.setText(MoneyUtil.moneyFormatDouble(list.get(position).getPrice3()));
        }

    }


    /**
     * 删除单个商品
     */
    private void deleteSingleProduct(final int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", list.get(position).getCode());
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808031", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                list.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 编辑购物车商品数量
     */
    private void modifyProductNum(final int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", list.get(position).getCode());
            object.put("quantity", list.get(position).getQuantity());
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808033", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                notifyDataSetChanged();
                activity.getTotalPrice();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(context, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tip(final int position) {
        new AlertDialog.Builder(context).setTitle("提示")
                .setMessage("是否确认删除该商品?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSingleProduct(position);
                    }
                }).setNegativeButton("取消", null).show();
    }

    static class ViewHolder {
        @InjectView(R.id.img_choose)
        ImageView imgChoose;
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
        @InjectView(R.id.txt_subtract)
        TextView txtSubtract;
        @InjectView(R.id.txt_number)
        TextView txtNumber;
        @InjectView(R.id.txt_add)
        TextView txtAdd;
        @InjectView(R.id.img_delete)
        ImageView imgDelete;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
