package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiniu.android.http.ResponseInfo;
import com.squareup.picasso.Picasso;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.EarningsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.MyJewelHistoryActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.MyShopActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.OrderListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.RelationActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.SettingActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.WalletActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.QiNiuUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil.RESULT_CAMARA_IMAGE;

public class MyFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.img_photo)
    CircleImageView imgPhoto;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_account)
    TextView txtAccount;
    @InjectView(R.id.txt_share)
    TextView txtShare;
    @InjectView(R.id.layout_earnings)
    LinearLayout layoutEarnings;
    @InjectView(R.id.layout_moneyTree)
    LinearLayout layoutMoneyTree;
    @InjectView(R.id.layout_wallet)
    LinearLayout layoutWallet;
    @InjectView(R.id.layout_target)
    LinearLayout layoutTarget;
    @InjectView(R.id.layout_shoppinglist)
    LinearLayout layoutShoppinglist;
    @InjectView(R.id.layout_shop)
    LinearLayout layoutShop;
    @InjectView(R.id.layout_relation)
    LinearLayout layoutRelation;
    @InjectView(R.id.layout_seting)
    LinearLayout layoutSeting;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    // Fragment主视图
    private View view;

    private PersonalModel model;

    private SharedPreferences wxShareSp;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;


    private String shareURL = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my2, null);
        ButterKnife.inject(this, view);

        inis();
        getData();
        initRefreshLayout();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void inis() {

        model = new PersonalModel();

        wxShareSp = getActivity().getSharedPreferences("wxShare", Context.MODE_PRIVATE);
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
    }


    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805056", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("mobile", model.getMobile());
                    editor.putString("realName", model.getRealName());
                    editor.putString("nickName", model.getNickname());
                    editor.putString("identityFlag", model.getIdentityFlag());
                    editor.putString("tradepwdFlag", model.getTradepwdFlag());
                    editor.putString("userRefereeName", model.getUserRefereeName());
                    if (null != model.getUserExt().getPhoto()) {
                        editor.putString("photo", model.getUserExt().getPhoto());
                    }
                    String address = model.getUserExt().getProvince() + model.getUserExt().getCity() + model.getUserExt().getArea();
                    editor.putString("address", address);
                    editor.commit();

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/user/register.html?userReferee=" + userInfoSp.getString("mobile", null);

        txtName.setText(model.getNickname());
        txtAccount.setText(model.getMobile().substring(0, 3) + "****" + model.getMobile().substring(7, model.getMobile().length()));
        if (null == model.getUserExt().getPhoto()) {
            Picasso.with(getActivity()).load(R.mipmap.photo_default).into(imgPhoto);
        } else {
            ImageUtil.photo(model.getUserExt().getPhoto(), imgPhoto, getActivity());
        }
    }


    @OnClick({R.id.layout_moneyTree, R.id.layout_target, R.id.layout_seting, R.id.txt_share,
            R.id.img_photo,R.id.layout_earnings, R.id.layout_wallet, R.id.layout_shoppinglist,
            R.id.layout_shop, R.id.layout_relation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                choosePhoto(view);
                break;

            case R.id.txt_share:

                SharedPreferences.Editor editor = wxShareSp.edit();
                editor.putString("shareWay", "my");
                editor.commit();

                showShare(view);
                break;

            case R.id.layout_moneyTree:
                startActivity(new Intent(getActivity(), TreeActivity.class));
                break;

//            case R.id.layout_give:
//                startActivity(new Intent(getActivity(), GiveActivity.class));
//
//                break;

            case R.id.layout_earnings:
                startActivity(new Intent(getActivity(), EarningsActivity.class));
                break;

            case R.id.layout_target:
                startActivity(new Intent(getActivity(), MyJewelHistoryActivity.class));
                break;

            case R.id.layout_seting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            case R.id.layout_wallet:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;

            case R.id.layout_shoppinglist:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;

            case R.id.layout_shop:
                startActivity(new Intent(getActivity(), MyShopActivity.class));
                break;

            case R.id.layout_relation:
                startActivity(new Intent(getActivity(), RelationActivity.class)
                        .putExtra("nickName", model.getNickname())
                        .putExtra("photo",model.getUserExt().getPhoto())
                        .putExtra("userReferee",model.getUserRefereeName())
                        .putExtra("updateDatetime", model.getCreateDatetime()));

                break;
        }
    }

    private void showShare(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.popup_share, null);

        ImageView qrCode = (ImageView) mview.findViewById(R.id.img_QRCode);
        ImageView wx = (ImageView) mview.findViewById(R.id.img_wx);
        ImageView pyq = (ImageView) mview.findViewById(R.id.img_pyq);
        TextView qx = (TextView) mview.findViewById(R.id.txt_cancel);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        String textContent = model.getUserExt().getMobile();
        if (TextUtils.isEmpty(textContent)) {
            Toast.makeText(getActivity(), "您的手机号码为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap mBitmap = CodeUtils.createImage(textContent, 400, 400, null);
        qrCode.setImageBitmap(mBitmap);

        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToWX(getActivity(), shareURL,
                        "正汇钱包邀您玩转红包",
                        "小目标，发一发，摇一摇，聊一聊各种红包玩法");
                popupWindow.dismiss();

                System.out.println("shareURL=" + shareURL);
            }
        });
        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(getActivity(), shareURL,
                        "正汇钱包邀您玩转红包",
                        "小目标，发一发，摇一摇，聊一聊各种红包玩法");
                popupWindow.dismiss();

                System.out.println("shareURL=" + shareURL);

            }
        });
        qx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    /**
     * 选择头像
     *
     * @param view
     */
    private PopupWindow popupWindow;

    private void choosePhoto(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(getActivity()).inflate(
                R.layout.popup_release, null);

        TextView txtPhotograph = (TextView) mview
                .findViewById(R.id.txt_photograph);
        TextView txtAlbum = (TextView) mview
                .findViewById(R.id.txt_album);
        TextView txtCancel = (TextView) mview
                .findViewById(R.id.txt_releasePopup_cancel);

        LinearLayout dismiss = (LinearLayout) mview.findViewById(R.id.quxiao);

        popupWindow = new PopupWindow(mview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);

        popupWindow.setTouchable(true);

        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        txtAlbum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // 调用android的图库
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, ImageUtil.RESULT_LOAD_IMAGE);
                popupWindow.dismiss();
            }
        });

        txtPhotograph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, RESULT_CAMARA_IMAGE);

                popupWindow.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, Gravity.BOTTOM);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //data为B中回传的Intent
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == ImageUtil.RESULT_LOAD_IMAGE) {
                if(data.getData() != null){
                    Glide.with(getActivity()).load(album(data)).into(imgPhoto);

                    new QiNiuUtil(getActivity(), album(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            updatePhoto(key);
                        }
                    }, true);
                }

            } else if (requestCode == ImageUtil.RESULT_CAMARA_IMAGE) {
                if(data.getExtras() != null){
                    Glide.with(getActivity()).load(camara(data)).into(imgPhoto);

                    new QiNiuUtil(getActivity(), camara(data), null).qiNiu(new QiNiuUtil.QiNiuCallBack() {
                        @Override
                        public void onSuccess(String key, ResponseInfo info, JSONObject res) {
                            updatePhoto(key);
                        }
                    }, true);
                }
            }
        }

    }

    /**
     * 调用系统相册的操作,在onActivityResult中调用
     *
     * @param data onActivityResult中的Intent
     */
    public String album(Intent data) {
        Uri selectedImage = data.getData();

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Log.d("picturePath", picturePath);
        BitmapFactory.decodeFile(picturePath);
        return picturePath;
    }

    /**
     * 调用系统相机,在onActivityResult中调用，拍照后保存到sdcard卡中
     *
     * @param data onActivityResult中的Intent
     * @return
     */
    public String camara(Intent data) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            Toast.makeText(getActivity(),
                    "SD card is not avaiable/writeable right now.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        String name = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".jpg";
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        FileOutputStream b = null;
        File file = new File("sdcard/DCIM/Camera/");
        file.mkdirs();// 创建文件夹
        String fileName = "sdcard/DCIM/Camera/" + name;
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private void updatePhoto(final String url) {
        JSONObject object = new JSONObject();
        try {
            object.put("photo", url);
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805077", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getActivity(), "头像修改成功", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = userInfoSp.edit();
                editor.putString("photo", url);
                editor.commit();
                getData();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                getData();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }
        }, 1500);
    }

}
