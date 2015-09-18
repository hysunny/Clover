package com.clover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.User;

import cn.bmob.v3.listener.UpdateListener;

public class UpdateUserNickActivity extends BaseActivity {

    private EditText et_nick;
    private Button btn_finishNick;
    String nick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_nick);
        initView();
    }

    private void initView(){
        initToolbar(getResources().getString(R.string.title_activity_update_user_nick),new Intent(this, UserInfoUpdateActivity.class), this);
        et_nick = (EditText)findViewById(R.id.updateNick);
        btn_finishNick = (Button)findViewById(R.id.finishNick);

        btn_finishNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = et_nick.getText().toString();
                if(TextUtils.isEmpty(nick)){
                    Toast.makeText(UpdateUserNickActivity.this, R.string.pleaseinputnick, Toast.LENGTH_SHORT).show();
                }else{
                    updateNick(nick);
                    finish();
                }
            }
        });
    }

    /**
     * 修改昵称
     */
    private void updateNick(String nick){
        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setNick(nick);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UpdateUserNickActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(UpdateUserNickActivity.this,R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
