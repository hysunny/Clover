package com.clover.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clover.R;
import com.clover.entities.User;
import com.clover.net.BmobRequest;
import com.clover.utils.CloverApplication;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.listener.UpdateListener;


public class MensesActivity extends BaseActivity {

    private Button btn_sendMensesMessage;
    private ImageView iv_menses;
    private LinearLayout lv_menseslayout;
    boolean tag = true;
    String msg;
    String MENSES_COME_ACTION = "MENSES_COME";
    BmobChatManager chatManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    User user, lover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menses);

        initToolbar(getResources().getString(R.string.title_activity_menses),new Intent(this, HealthActivity.class), this);
        preferences = getSharedPreferences("lover", MODE_PRIVATE);
        editor = preferences.edit();
        application = (CloverApplication) getApplication();
        chatManager = BmobChatManager.getInstance(this);
        user = BmobUserManager.getInstance(this).getCurrentUser(User.class);
        if(application.getOne_user()!=null) {
            lover = application.getOne_user();
        }
        btn_sendMensesMessage = (Button)findViewById(R.id.sendmensesmessage);
        iv_menses = (ImageView)findViewById(R.id.pic_menses);
        lv_menseslayout = (LinearLayout)findViewById(R.id.MensesReminderLayout);

        if(user.getMenses()==1&&preferences.getInt("menses", 2)==1){
            iv_menses.setImageResource(R.mipmap.menses);
            lv_menseslayout.setVisibility(View.VISIBLE);
            btn_sendMensesMessage.setText(R.string.menses_gone_msg);
        }else if(user.getMenses()==1&&preferences.getInt("menses", 2)==2){
            iv_menses.setImageResource(R.mipmap.menses);
            lv_menseslayout.setVisibility(View.GONE);
            btn_sendMensesMessage.setText(R.string.menses_gone_msg);
        }else if(user.getMenses()==2&&preferences.getInt("menses", 2)==1){
            iv_menses.setImageResource(R.mipmap.menses);
            lv_menseslayout.setVisibility(View.VISIBLE);
            btn_sendMensesMessage.setText(R.string.menses_tell_msg);
        }else if(user.getMenses()==2&&preferences.getInt("menses", 2)==2){
            iv_menses.setImageResource(R.mipmap.fine_menses);
            lv_menseslayout.setVisibility(View.GONE);
            btn_sendMensesMessage.setText(R.string.menses_tell_msg);
        }
        btn_sendMensesMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag) {
                    msg = getResources().getString(R.string.menses_tell_msg);
                    BmobRequest.pushMessageToLover(msg, "MENSES_COME", MensesActivity.this, application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    tag=false;
                    iv_menses.setImageResource(R.mipmap.menses);
                    btn_sendMensesMessage.setText(R.string.menses_gone_msg);
                    updateMenses(user, 1);
                }else {
                    msg = getResources().getString(R.string.menses_gone_msg);
                    BmobRequest.pushMessageToLover(msg, "MENSES_GONE", MensesActivity.this, application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    tag=true;
                    if(preferences.getInt("menses", 2)==2) {
                        iv_menses.setImageResource(R.mipmap.fine_menses);
                    }
                    btn_sendMensesMessage.setText(R.string.menses_tell_msg);
                    updateMenses(user, 2);
                }
            }
        });
        registerBoradcastReceiver();
    }
    /**
     * 广播接收，对方姨妈提醒
     */
    private BroadcastReceiver mensesReminderReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int extra = intent.getExtras().getInt("key");
            switch (extra){
                case 1:
                    iv_menses.setImageResource(R.mipmap.menses);
                    lv_menseslayout.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    if(BmobUserManager.getInstance(context).getCurrentUser(User.class).getMenses()==2) {
                        iv_menses.setImageResource(R.mipmap.fine_menses);
                    }
                    lv_menseslayout.setVisibility(View.GONE);
                    break;
            }
            abortBroadcast();
        }
    };
    /**
     * 注册该广播接收
     */
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(MENSES_COME_ACTION);
        //注册广播
        registerReceiver(mensesReminderReceiver, myIntentFilter);
    }

    private void updateMenses(User user, int state){
        User u = new User();
        u.setMenses(state);
        u.setObjectId(user.getObjectId());
        u.update(MensesActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

}
