package com.zhenghui.zhqb.zhenghuiqianbaomember.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.DeviceUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShopDetailsAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private ViewHolder holder;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    public ShopDetailsAdapter(Context context, List<String> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_shop_details, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        ImageUtil.glide(list.get(i),holder.imgShopPic,context);

//        ViewTreeObserver viewTreeObserver = holder.imgShopPic.getViewTreeObserver();
//        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                int height = holder.imgShopPic.getHeight();
//                int width = holder.imgShopPic.getWidth();
//                Log.v("获取TextView宽高", "宽度:" + width + ",高度:" + height);
//                return true;
//            }
//        });

//        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//        holder.imgShopPic.measure(w, h);
//        int height =holder.imgShopPic.getMeasuredHeight();
//        int width =holder.imgShopPic.getMeasuredWidth();
////        textView.append("\n"+height+","+width);
//
//        System.out.println("height="+height+",width="+width);

//        new Thread(networkTask).start();
//        ImageUtil.picasso(list.get(i),holder.imgShopPic,context);

        String[] picInfo = list.get(i).split("\\.")[0].split("_");
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
                .load(ImageUtil.IMAGE + list.get(i))
                .override((int)screenWith, (int)height)
                .fitCenter()
                .crossFade()
                .into(holder.imgShopPic);

        return view;
    }
    // 在这里进行 http request.网络请求相关操作
     Message msg = new Message();

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            Bundle data = new Bundle();
            data.putString("value", "请求结果");
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    static class ViewHolder {
        @InjectView(R.id.img_shopPic)
        ImageView imgShopPic;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
