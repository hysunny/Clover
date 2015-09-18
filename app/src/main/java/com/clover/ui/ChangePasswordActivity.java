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
import com.clover.utils.ActivityCollector;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.UpdateListener;


public class ChangePasswordActivity extends BaseActivity {

    private EditText et_oldpassword;//旧密码
    private EditText et_newpassword;//新密码
    private EditText et_repeatnewpsd;//重复新密码
    private Button btn_finish;//修改完成
    //String oldPassword;
    String newPassword;
    String repeatNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initToolbar(getResources().getString(R.string.title_activity_change_password),new Intent(this, SettingActivity.class), this);

        //et_oldpassword = (EditText)findViewById(R.id.oldpassword);
        et_newpassword = (EditText)findViewById(R.id.newpassword);
        et_repeatnewpsd = (EditText)findViewById(R.id.repeatnewpsd);
        btn_finish = (Button)findViewById(R.id.finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword(){
        //oldPassword = et_oldpassword.getText().toString();
        newPassword = et_newpassword.getText().toString();
        repeatNewPassword = et_repeatnewpsd.getText().toString();
        /*
        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(this, getResources().getString(R.string.inputoldpassword), Toast.LENGTH_SHORT).show();
            return;
        }
        String old = BmobChatUser.getCurrentUser(this).getPassword();
        if(!oldPassword.equals(userManager.getCurrentUser(User.class).getPassword())){
            Toast.makeText(this, getResources().getString(R.string.wrongoldpassword), Toast.LENGTH_SHORT).show();
            return;
        }
        */
        if(TextUtils.isEmpty(newPassword)){
            Toast.makeText(this, getResources().getString(R.string.inputnewpassword), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newPassword.equals(repeatNewPassword)){
            Toast.makeText(this, getResources().getString(R.string.differentpsd), Toast.LENGTH_SHORT).show();
            return;
        }

        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setPassword(newPassword);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ChangePasswordActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
                BmobUserManager userManager = BmobUserManager.getInstance(ChangePasswordActivity.this);
                userManager.logout();
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                ActivityCollector.finishAll();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(ChangePasswordActivity.this,R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
