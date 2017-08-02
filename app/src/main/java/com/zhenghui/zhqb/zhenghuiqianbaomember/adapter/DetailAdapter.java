package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.DeviceUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailAdapter extends BaseAdapter {

    private ViewHolder holder;

    private String[] pic;
    private Context context;

    public DetailAdapter(Context context, String[] pic) {
        this.pic = pic;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pic.length;
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_detail, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        String[] picInfo = pic[i].split("\\.")[0].split("_");
        String w = picInfo[picInfo.length-2];
        String h = picInfo[picInfo.length-1];

        int picWidth = Integer.parseInt(w);
        int picHeight = Integer.parseInt(h);

        double screenWith = DeviceUtils.getScreenWith(context) - DeviceUtils.dip2px(context,30f);
        double ratio = screenWith/picWidth;
        double height = ratio*picHeight;

        System.out.println("screenWith="+screenWith);
        System.out.println("ratio="+ratio);
        System.out.println("height="+height);
        System.out.println("(int)height="+(int)height);

        Glide.with(context)
                .load(ImageUtil.IMAGE + pic[i])
                .override((int)screenWith, (int)height)
                .fitCenter()
                .crossFade()
                .into(holder.img);

//        ImageUtil.glide(pic[i], holder.img,context);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.img)
        ImageView img;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
