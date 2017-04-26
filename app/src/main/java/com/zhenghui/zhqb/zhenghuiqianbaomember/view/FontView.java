package com.zhenghui.zhqb.zhenghuiqianbaomember.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dell1 on 2016/6/22.
 */
public class FontView extends TextView {
    public FontView(Context context) {
        super(context);
        init(context);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        AssetManager assetManager = context.getAssets();
        Typeface font = Typeface.createFromAsset(assetManager,"font/font.ttf");
        setTypeface(font);

    }

}
