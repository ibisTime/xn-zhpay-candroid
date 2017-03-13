package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

/**
 * Created by dell1 on 2016/12/17.
 */

public class ImageUtil {

    public static String IMAGE = "http://7xnuu2.com1.z0.glb.clouddn.com/";

    public static final int RESULT_LOAD_IMAGE = 8888;
    public static final int RESULT_CAMARA_IMAGE = 9999;

    /**
     *
     * @param url
     *            图片路径
     * @param sellersmallimg
     *            imageView控件
     * @param context
     *            上下文
     */
    public static void glide(String url, ImageView sellersmallimg, Context context) {
        if (url == null || url.indexOf("www")!=-1) {
            return;
        }
        if (url.indexOf("http") != -1) {
            Glide.with(context).load(url)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
                    .into(sellersmallimg);
        } else {
            Glide.with(context)
                    .load(IMAGE + url )
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
                    .into(sellersmallimg);

            System.out.println("glide + uri="+IMAGE + url );

        }

    }

    /**
     *
     * @param url
     *            图片路径
     * @param sellersmallimg
     *            imageView控件
     * @param context
     *            上下文
     */
    public static void picasso(String url, ImageView sellersmallimg, Context context) {
        if (url == null) {
            return;
        }
        if (url.indexOf("http") != -1) {
            Glide.with(context).load(url)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
                    .into(sellersmallimg);
        } else {
            Glide.with(context)
                    .load(IMAGE + url )
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
                    .into(sellersmallimg);

            System.out.println("picasso + uri="+IMAGE + url );

        }

    }


    /**
     *
     * @param url
     *            图片路径
     * @param sellersmallimg
     *            imageView控件
     * @param context
     *            上下文
     */
    public static void photo(String url, ImageView sellersmallimg, Context context) {
        if (url == null) {
            return;
        }
        if (url.equals("")){
            Picasso.with(context)
                    .load(R.mipmap.photo_default)
                    .into(sellersmallimg);
        } else if (url.indexOf("http") != -1) {
            Picasso.with(context).load(url)
                    .error(R.mipmap.photo_default)
                    .into(sellersmallimg);
        } else {
            Picasso.with(context)
                    .load(IMAGE + url )
                    .error(R.mipmap.photo_default)
                    .into(sellersmallimg);

            System.out.println("photo + uri="+IMAGE + url );

        }

    }

}
