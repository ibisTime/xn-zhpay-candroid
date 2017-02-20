package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ProductTypeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/20.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    @InjectView(R.id.txt_type)
    TextView txtType;
    private Context context;
    private List<ProductTypeModel> list;

    private MyItemClickListener mItemClickListener;


    public RecyclerViewAdapter(Context context, List<ProductTypeModel> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, null);
        ViewHolder vh = new ViewHolder(view, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(list.get(position).isChoose()){
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.fontColor_orange));
        }else{
            holder.mTextView.setTextColor(context.getResources().getColor(R.color.fontColor_gray));
        }
        holder.mTextView.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        private MyItemClickListener mListener;

        public ViewHolder(View view,MyItemClickListener mListener) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.txt_type);

            this.mListener = mListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                mListener.OnItemClick(view.findViewById(R.id.txt_type),getPosition());
            }
        }
    }

    public interface MyItemClickListener{
        void OnItemClick(View view,int position);
    }


    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }


}
