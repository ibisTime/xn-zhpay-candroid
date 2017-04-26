package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.AddAddressActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.AddressSelectActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.AddressModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/16.
 */

public class AddressAdapter extends BaseAdapter {

    private List<AddressModel> list;
    private Context context;
    private ViewHolder holder;

    private SharedPreferences userInfoSp;
    private SharedPreferences addConfigSp;

    public AddressAdapter(Context context, List<AddressModel> list) {
        this.list = list;
        this.context = context;

        userInfoSp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        addConfigSp = context.getSharedPreferences("addConfig", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        System.out.println("lists.size()=" + list.size());
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
            view = LayoutInflater.from(context).inflate(R.layout.item_address, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(list.get(i).getIsDefault());

                if (list.get(i).getIsDefault().equals("0")) {
                    setDefaultAddress(i);
                }
            }
        });

        holder.layoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddAddressActivity.class)
                        .putExtra("isModifi",true)
                        .putExtra("code",list.get(i).getCode()));
            }
        });

        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tip(i);
            }
        });

        setView(i);

        return view;
    }

    private void setView(int position) {
        if (list.get(position).getIsDefault().equals("1")) {
            holder.imgChoose.setBackground(context.getResources().getDrawable(R.mipmap.address_choose));
        } else {
            holder.imgChoose.setBackground(context.getResources().getDrawable(R.mipmap.address_unchoose));
        }

        holder.txtName.setText(list.get(position).getAddressee());
        holder.txtPhone.setText(list.get(position).getMobile());
        holder.txtAddress.setText(list.get(position).getProvince() + " " + list.get(position).getCity() + " " + list.get(position).getDistrict() + "" + list.get(position).getDetailAddress());
    }


    /**
     * 设置默认地址
     */
    private void setDefaultAddress(final int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", list.get(position).getCode());
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", addConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805163", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                AddressSelectActivity.instances.getData();

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
     * 删除默认地址
     */
    private void deleteAddress(final int position) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", list.get(position).getCode());
            object.put("token", userInfoSp.getString("token", null));
//            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", addConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805161", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                AddressSelectActivity.instances.getData();

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
                .setMessage("是否确认删除该收货地址?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAddress(position);
                    }
                }).setNegativeButton("取消", null).show();
    }

    static class ViewHolder {
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_phone)
        TextView txtPhone;
        @InjectView(R.id.txt_address)
        TextView txtAddress;
        @InjectView(R.id.img_choose)
        ImageView imgChoose;
        @InjectView(R.id.layout_edit)
        LinearLayout layoutEdit;
        @InjectView(R.id.layout_delete)
        LinearLayout layoutDelete;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
