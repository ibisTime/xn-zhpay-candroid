package com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.FriendModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/14.
 */

public class ChatToFriendAdapter extends BaseAdapter {

    private List<FriendModel> list;
    private Context context;
    private ViewHolder holder;

    public ChatToFriendAdapter(Context context, List<FriendModel> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_friend, null);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        setView(i);

        return view;
    }

    private void setView(int position) {

        if (list.get(position).getRefeereLevel() == -1) {
            holder.txtType.setText("P1");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_red));
        } else if (list.get(position).getRefeereLevel() == -2) {
            holder.txtType.setText("P2");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_orange));
        } else if (list.get(position).getRefeereLevel() == 1) {
            holder.txtType.setText("C1");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_green));
        } else if (list.get(position).getRefeereLevel() == 2) {
            holder.txtType.setText("C2");
            holder.layoutBg.setBackgroundColor(context.getResources().getColor(R.color.chat_blue));
        }


        if(null != list.get(position).getNickname()){
            holder.txtName.setText(list.get(position).getNickname());
        }else{
            holder.txtName.setText("未知");
        }
        ImageUtil.photo(list.get(position).getUserExt().getPhoto(), holder.imgPhoto, context);

        if (list.get(position).getUserId() != null) {

            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(list.get(position).getUserId());

            if (conversation != null) {
                //获取此会话的所有消息
                List<EMMessage> messages = conversation.getAllMessages();
                //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多

                if (messages.size() != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dt = new Date(messages.get(messages.size() - 1).getMsgTime());
                    String time = sdf.format(dt);
                    holder.txtTime.setText("来访时间:" + time);
                }

                if (conversation.getUnreadMsgCount() > 0) {
                    holder.txtPoint.setVisibility(View.VISIBLE);
                } else {
                    holder.txtPoint.setVisibility(View.GONE);
                }
            }
        }


    }

    static class ViewHolder {
        @InjectView(R.id.img_photo)
        CircleImageView imgPhoto;
        @InjectView(R.id.txt_point)
        TextView txtPoint;
        @InjectView(R.id.txt_name)
        TextView txtName;
        @InjectView(R.id.txt_type)
        TextView txtType;
        @InjectView(R.id.txt_time)
        TextView txtTime;
        @InjectView(R.id.layout_bg)
        LinearLayout layoutBg;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
