package com.clover.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.clover.R;


public class MessageSettingActivity extends BaseActivity {

    private RelativeLayout rv_voice;//声音
    private RelativeLayout rv_shake;//振动
    private ImageView iv_openvoice;//开启声音
    private ImageView iv_closevoice;//关闭声音
    private ImageView iv_openshake;//开启振动
    private ImageView iv_closeshake;//关闭振动
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //SharePreferenceUtil mSharedUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);

         sharedPreferences = getSharedPreferences("set", MODE_PRIVATE);
         editor = sharedPreferences.edit();

        initToolbar(getResources().getString(R.string.title_activity_message_setting),new Intent(this, SettingActivity.class), this);
        //mSharedUtil = application.getSpUtil();

        rv_voice = (RelativeLayout)findViewById(R.id.switch_voice);
        rv_shake = (RelativeLayout)findViewById(R.id.switch_shake);
        iv_openvoice = (ImageView)findViewById(R.id.openvoice);
        iv_closevoice = (ImageView)findViewById(R.id.closevoice);
        iv_openshake = (ImageView)findViewById(R.id.openshake);
        iv_closeshake = (ImageView)findViewById(R.id.closeshake);
        if (sharedPreferences.getInt("voice", 0) == 1) {
            iv_openvoice.setVisibility(View.VISIBLE);
            iv_closevoice.setVisibility(View.INVISIBLE);
        }else{
            iv_openvoice.setVisibility(View.INVISIBLE);
            iv_closevoice.setVisibility(View.VISIBLE);
        }

        if (sharedPreferences.getInt("shake", 0) == 1) {
            iv_openshake.setVisibility(View.VISIBLE);
            iv_closeshake.setVisibility(View.INVISIBLE);
        }else{
            iv_openshake.setVisibility(View.INVISIBLE);
            iv_closeshake.setVisibility(View.VISIBLE);
        }


        rv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_openvoice.getVisibility() == View.VISIBLE) {
                    iv_openvoice.setVisibility(View.INVISIBLE);
                    iv_closevoice.setVisibility(View.VISIBLE);
                    //mSharedUtil.setAllowVoiceEnable(false);
                    editor.putInt("voice",0);
                }else if(iv_closevoice.getVisibility() == View.VISIBLE){
                    iv_closevoice.setVisibility(View.INVISIBLE);
                    iv_openvoice.setVisibility(View.VISIBLE);
                    //mSharedUtil.setAllowVoiceEnable(true);
                    editor.putInt("voice",1);
                }
                editor.commit();
            }
        });

        rv_shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_openshake.getVisibility() == View.VISIBLE) {
                    iv_openshake.setVisibility(View.INVISIBLE);
                    iv_closeshake.setVisibility(View.VISIBLE);
                    //mSharedUtil.setAllowVibrateEnable(false);
                    editor.putInt("shake",0);
                }else if(iv_closeshake.getVisibility() == View.VISIBLE){
                    iv_closeshake.setVisibility(View.INVISIBLE);
                    iv_openshake.setVisibility(View.VISIBLE);
                    //mSharedUtil.setAllowVibrateEnable(true);
                    editor.putInt("shake",1);
                }
                editor.commit();
            }
        });

    }


}
