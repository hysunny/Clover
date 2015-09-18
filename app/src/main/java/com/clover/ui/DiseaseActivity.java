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

public class DiseaseActivity extends BaseActivity {

    private Button btn_diseaseReminder;
    private Button btn_sendDrugReminder;
    private LinearLayout lv_drugReminder;
    private ImageView iv_disease;
    boolean tag = true;
    String msg;
    String DISEASE_COME_ACTION = "DISEASE_COME";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    User user, lover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        initToolbar(getResources().getString(R.string.title_activity_disease),new Intent(this, HealthActivity.class), this);
        preferences = getSharedPreferences("lover", MODE_PRIVATE);
        editor = preferences.edit();
        application = (CloverApplication) getApplication();
        chatManager = BmobChatManager.getInstance(this);
        user = BmobUserManager.getInstance(this).getCurrentUser(User.class);
        if(application.getOne_user()!=null) {
            lover = application.getOne_user();
        }
        btn_diseaseReminder = (Button)findViewById(R.id.diseaseReminder);
        btn_sendDrugReminder = (Button)findViewById(R.id.sendDrugReminder);
        lv_drugReminder = (LinearLayout)findViewById(R.id.DrugReminderLayout);
        iv_disease = (ImageView)findViewById(R.id.pic_disease);

        if(user.getDisease()==1&&preferences.getInt("disease", 2)==1){
            iv_disease.setImageResource(R.mipmap.disease);
            lv_drugReminder.setVisibility(View.VISIBLE);
            btn_diseaseReminder.setText(R.string.disease_gone_msg);
        }else if(user.getDisease()==1&&preferences.getInt("disease", 2)==2){
            iv_disease.setImageResource(R.mipmap.disease);
            lv_drugReminder.setVisibility(View.GONE);
            btn_diseaseReminder.setText(R.string.disease_gone_msg);
        }else if(user.getDisease()==2&&preferences.getInt("disease", 2)==1){
            iv_disease.setImageResource(R.mipmap.disease);
            lv_drugReminder.setVisibility(View.VISIBLE);
            btn_diseaseReminder.setText(R.string.disease_come_msg);
        }else if(user.getDisease()==2&&preferences.getInt("disease", 2)==2){
            iv_disease.setImageResource(R.mipmap.fine_disease);
            lv_drugReminder.setVisibility(View.GONE);
            btn_diseaseReminder.setText(R.string.disease_come_msg);
        }

        btn_diseaseReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tag) {
                    msg = getResources().getString(R.string.disease_come_msg);
                    BmobRequest.pushMessageToLover(msg, "DISEASE_COME", DiseaseActivity.this, application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    tag=false;
                    btn_diseaseReminder.setText(R.string.disease_gone_msg);
                    iv_disease.setImageResource(R.mipmap.disease);
                    updateDisease(user, 1);
                }else {
                    msg = getResources().getString(R.string.disease_gone_msg);
                    BmobRequest.pushMessageToLover(msg, "DISEASE_GONE", DiseaseActivity.this, application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    tag=true;
                    btn_diseaseReminder.setText(R.string.disease_come_msg);
                    if(preferences.getInt("disease", 2)==2) {
                        iv_disease.setImageResource(R.mipmap.fine_disease);
                    }
                    updateDisease(user, 2);
                }
            }
        });
        btn_sendDrugReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = getResources().getString(R.string.eatdrug);
                BmobRequest.pushMessageToLover(msg, "DRUG", DiseaseActivity.this, application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
            }
        });
        registerBoradcastReceiver();
    }

    /**
     * 广播接收，对方健康提醒
     */
    private BroadcastReceiver diseaseReminderReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int extra = intent.getExtras().getInt("key");
            switch(extra){
                case 1:
                    lv_drugReminder.setVisibility(View.VISIBLE);
                    iv_disease.setImageResource(R.mipmap.disease);
                    break;
                case 2:
                    lv_drugReminder.setVisibility(View.GONE);
                    if(BmobUserManager.getInstance(context).getCurrentUser(User.class).getDisease()==2) {
                        iv_disease.setImageResource(R.mipmap.fine_disease);
                    }
                    break;
            }
            //终结广播
            abortBroadcast();
        }
    };
    /**
     * 注册该广播接收
     */
    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(DISEASE_COME_ACTION);
        //注册广播
        registerReceiver(diseaseReminderReceiver, myIntentFilter);
    }

    private void updateDisease(User user, int state){
        User u = new User();
        u.setDisease(state);
        u.setObjectId(user.getObjectId());
        u.update(DiseaseActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
