package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.model.EvaluateModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EvaluateAdapter extends BaseAdapter {

    ViewHolder holder;
    private Context context;
    private List<EvaluateModel> list;

    public EvaluateAdapter(Context context, List<EvaluateModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_evaluate, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {

        ImageUtil.glide(list.get(position).getUser().getPhoto(),holder.imgPhoto,context);
        holder.txtName.setText(list.get(position).getUser().getNickname());

        if(list.get(position).getType()!=null){
//            if(list.get(position).getType().equals("A")){
            if(list.get(position).getType().equals("3")){
                holder.imgEvalute.setImageResource(R.mipmap.evaluate_good);
                holder.txtEvalute.setText("好评");
                holder.txtEvalute.setTextColor(context.getResources().getColor(R.color.orange));
            }else if(list.get(position).getType().equals("B")){
                holder.imgEvalute.setImageResource(R.mipmap.evaluate_general);
                holder.txtEvalute.setText("中评");
                holder.txtEvalute.setTextColor(context.getResources().getColor(R.color.fontColor_support));
            }else{
                holder.imgEvalute.setImageResource(R.mipmap.evaluate_bad);
                holder.txtEvalute.setText("差评");
                holder.txtEvalute.setTextColor(context.getResources().getColor(R.color.gray));
            }
        }


    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.img_evalute)
        ImageView imgEvalute;
        @InjectView(R.id.txt_evalute)
        TextView txtEvalute;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
