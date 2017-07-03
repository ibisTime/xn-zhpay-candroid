package com.zhenghui.zhqb.zhenghuiqianbaomember.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(ImageUtil.IMAGE + path.toString() )
                    .into(imageView);
        }

    }
}
