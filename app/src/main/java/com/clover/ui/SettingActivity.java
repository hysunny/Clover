package com.clover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.clover.R;


public class SettingActivity extends BaseActivity {

    private RelativeLayout rv_changepassword;//修改密码
    private RelativeLayout rv_messagesetting;//消息通知的设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initToolbar(getResources().getString(R.string.title_activity_setting),new Intent(this, MainActivity.class), this);

        rv_changepassword = (RelativeLayout)findViewById(R.id.layout_changepassword);
        rv_messagesetting = (RelativeLayout)findViewById(R.id.layout_messagesetting);

        rv_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        rv_messagesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MessageSettingActivity.class);
                startActivity(intent);
            }
        });

    }

}
