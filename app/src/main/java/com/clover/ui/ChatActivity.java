package com.clover.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.clover.R;
import com.clover.adapter.MSListViewAdapter;
import com.clover.entities.User;
import com.clover.utils.CloverApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ChatActivity extends BaseActivity {
    private ImageView iv_send;
    private EditText et_input;
    private ListView mListView;
    private String msg_content;
    private ImageView iv_back;
    private TextView tv_name;
    private MSListViewAdapter adapter;
    private CloverApplication application;
    private List<BmobMsg> messageList;
    private User targetUser; //对方的用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        application = (CloverApplication) getApplication();
        checkSetting();
        //获取情侣用户对象
        targetUser = application.getOne_user();

        messageList = BmobDB.create(this).queryMessages(targetUser.getObjectId(), 1);

        initToolbar("聊天",new Intent(this, MainActivity.class), this);
        initView();         //初始化视图
        adapter = new MSListViewAdapter(ChatActivity.this, messageList, targetUser.getAvatar());
        mListView.setAdapter(adapter);
        mListView.setSelection(adapter.getCount() - 1);


       // initMsgData();      //加载数据
        ShowLog("设置adapter");

        //注册接受广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("MESSAGE_COMMING");
        filter.setPriority(100);
        MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);

    }

    private void initView(){
        iv_send = (ImageView) findViewById(R.id.iv_send);
        et_input = (EditText) findViewById(R.id.et_sendmessage);
        mListView = (ListView) findViewById(R.id.listview);
      //  iv_back = (ImageView) findViewById(R.id.iv_back);
      //  tv_name = (TextView) findViewById(R.id.tv_name);

       // tv_name.setText("chatting");

       /* iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg_content = et_input.getText().toString();
                if (TextUtils.isEmpty(msg_content) || msg_content == null) {
                    ShowToast("请输入信息内容");
                    return;
                }
                BmobMsg msg = BmobMsg.createTextSendMsg(ChatActivity.this, targetUser.getObjectId(), msg_content);
                msg.setExtra("chat");

                ShowLog("添加消息内容：" + msg.getContent());
                adapter.add(msg);
                chatManager.sendTextMessage(targetUser, msg);
                BmobDB.create(ChatActivity.this).saveMessage(msg);
                et_input.setText("");// 清空编辑框数据

                mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
            }
        });
    }


    private void initMsgData() {
        BmobQuery<BmobMsg> query = new BmobQuery<>();
        String[] msgName = {targetUser.getObjectId(), BmobChatUser.getCurrentUser(this).getObjectId()};
        query.addWhereContainedIn("belongId",Arrays.asList(msgName));
        query.findObjects(this, new FindListener<BmobMsg>() {
            @Override
            public void onSuccess(List<BmobMsg> list) {
                ShowLog("信息内容更新成功");
                for (BmobMsg msg : list) {
                    adapter.add(msg);
                    ShowLog("msg内容"+msg.getContent());
                    BmobDB.create(ChatActivity.this).saveMessage(msg);
                }
                mListView.setSelection(adapter.getCount() - 1);

            }

            @Override
            public void onError(int i, String s) {
                ShowLog("加载数据失败");
            }
        });

    }

    private void checkSetting(){

        if(application.getOne_user() == null){
            ShowToast("请设置您的Lover");
            Intent intent = new Intent(this, LoverManagerActivity.class);
            startActivity(intent);
           // finish();
        }

    }

    private class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
           /* Bundle bundle = intent.getBundleExtra("msg");
            BmobMsg msg = (BmobMsg) bundle.getSerializable("msg");
            adapter.add(msg);
            BmobDB.create(context).saveMessage(msg);
            mListView.setSelection(adapter.getCount() - 1);*/
            String json = intent.getStringExtra("msg");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                String ex = jsonObject.getString("ex");
                if (ex.equals("chat")) {
                    BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {
                        @Override
                        public void onSuccess(BmobMsg bmobMsg) {
                            adapter.add(bmobMsg);
                            BmobDB.create(ChatActivity.this).saveMessage(bmobMsg);
                            mListView.setSelection(adapter.getCount() - 1);
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

}
