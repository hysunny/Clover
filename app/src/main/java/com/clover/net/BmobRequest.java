package com.clover.net;

import android.content.Context;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.User;
import com.clover.utils.CommonUtils;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dan on 2015/6/29.
 */
public class BmobRequest {

    private User user;

    public BmobRequest(){

    }

    public void setUser(User user){
        this.user =user;
    }


    public static void pushMessageToLover(String msg, String tag, Context context, String targetId, User user, BmobChatManager manager){
        String pushmsg = msg;
        boolean isNetConnected = CommonUtils.isNetworkAvailable(context);
        if (!isNetConnected) {
            Toast.makeText(context, R.string.network_tips, Toast.LENGTH_LONG).show();
            // return;
        }
        BmobMsg message = BmobMsg.createTextSendMsg(context, targetId, pushmsg);
        message.setExtra(tag);
        if(user !=null) {
            // 默认发送完成，将数据保存到本地消息表和最近会话表中
            manager.sendTextMessage(user, message);
        }else{
            Toast.makeText(context, R.string.sorryforquery, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 根据id查询用户
     */
    public  User queryUserById(String objectId, Context context) {

        BmobQuery<User> query = new BmobQuery<User>();query.addWhereEqualTo("objectId", objectId);
        query.findObjects(context, new FindListener<User>() {

            @Override
            public void onSuccess(List<User> users) {
                if(users!=null&&(users.size()>0)){
                    user = users.get(0);
                }
            }
            @Override
            public void onError(int arg0, String arg1) {
            }
        });
        return user;
    }
}
