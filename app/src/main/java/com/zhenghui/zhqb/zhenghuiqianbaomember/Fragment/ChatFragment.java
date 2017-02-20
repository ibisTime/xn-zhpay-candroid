package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ChatActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MainActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ChatToFriendAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ChatToMerchantAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.FriendModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/12.
 */

public class ChatFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    @InjectView(R.id.txt_point)
    TextView txtPoint;
    @InjectView(R.id.layout_service)
    FrameLayout layoutService;
    @InjectView(R.id.line_friend)
    View lineFriend;
    @InjectView(R.id.layout_friend)
    LinearLayout layoutFriend;
    @InjectView(R.id.line_merchant)
    View lineMerchant;
    @InjectView(R.id.layout_merchant)
    LinearLayout layoutMerchant;
    @InjectView(R.id.list)
    ListView list;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    // Fragment主视图
    private View view;

    private List<FriendModel> friendList;
    private List<ShopModel> merchantList;

    private ChatToFriendAdapter friendAdapter;
    private ChatToMerchantAdapter merchantAdapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private boolean isAtFriend = true;

    private MainActivity activity;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what){
                case 1:
                    txtPoint.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    txtPoint.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, null);
        ButterKnife.inject(this, view);

        inis();
        initEvent();
        initRefreshLayout();
        getFriend();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden){
            serviceUnReade();
        }
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
    }

    private void inis() {
        activity = (MainActivity) getActivity();

        friendList = new ArrayList<>();
        merchantList = new ArrayList<>();

        friendAdapter = new ChatToFriendAdapter(getActivity(), friendList);
        merchantAdapter = new ChatToMerchantAdapter(getActivity(), merchantList);

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_friend, R.id.layout_merchant, R.id.layout_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_friend:
                initBtn();
                isAtFriend = true;
                lineFriend.setVisibility(View.VISIBLE);
                layoutFriend.setBackgroundColor(getResources().getColor(R.color.white));
                getFriend();
                break;

            case R.id.layout_merchant:
                initBtn();
                isAtFriend = false;
                lineMerchant.setVisibility(View.VISIBLE);
                layoutMerchant.setBackgroundColor(getResources().getColor(R.color.white));
                break;

            case R.id.layout_service:
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("nickName", "客服");
                intent.putExtra("myPhoto", userInfoSp.getString("photo",""));
                intent.putExtra("otherPhoto", "");
                intent.putExtra(EaseConstant.EXTRA_USER_ID, activity.SERVICE_ID);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                startActivity(intent);
                break;
        }
    }

    public void initBtn() {
        lineFriend.setVisibility(View.INVISIBLE);
        lineMerchant.setVisibility(View.INVISIBLE);

        layoutFriend.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutMerchant.setBackgroundColor(getResources().getColor(R.color.grayfa));
    }

    private void initEvent() {
        list.setOnItemClickListener(this);
        list.setAdapter(friendAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        if (isAtFriend) {
            intent.putExtra(EaseConstant.EXTRA_USER_ID, friendList.get(i).getUserId());
        } else {
            intent.putExtra(EaseConstant.EXTRA_USER_ID, merchantList.get(i).getOwner());
        }
        intent.putExtra("myPhoto", userInfoSp.getString("photo",""));
        intent.putExtra("otherPhoto", friendList.get(i).getUserExt().getPhoto());
        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        intent.putExtra("nickName", friendList.get(i).getNickname());
        startActivityForResult(intent, 0);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 ){
            getFriend();
        }

    }

    public void serviceUnReade() {
        // 刷新列表红点
        getFriend();

        //
        Message msg = new Message();
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(activity.SERVICE_ID);
        if(conversation != null){
            if (conversation.getUnreadMsgCount() > 0) {
                msg.what = 1;
            }else{
                msg.what = 2;
            }
            handler.sendMessage(msg);
        }
    }

    public void getFriend() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805157", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<FriendModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FriendModel>>() {
                    }.getType());

                    friendList.clear();
                    friendList.addAll(lists);
                    friendAdapter.notifyDataSetChanged();

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


    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                serviceUnReade();
            }
        }, 1500);
    }
}
