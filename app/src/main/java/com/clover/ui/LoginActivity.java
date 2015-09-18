package com.clover.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clover.R;
import com.clover.entities.User;

import cn.bmob.v3.listener.SaveListener;


public class LoginActivity  extends BaseActivity {
    private EditText et_username;
    private String username;
    private String password;
    private EditText et_password;
    private Button bt_login;
    private TextView register;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        register = (TextView)findViewById(R.id.btn_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin(){
        username = et_username.getText().toString();
        password = et_password.getText().toString();
        if(TextUtils.isEmpty(username)){
            ShowToast("请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(password)){
            ShowToast("请输入密码");
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在登录");
        dialog.setCancelable(false);
        dialog.show();
        user= new User();
        user.setUsername(username);
        user.setPassword(password);
        userManager.login(user, new SaveListener() {
            @Override
            public void onSuccess() {
                dialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                ShowToast("登录失败");
                dialog.dismiss();
            }
        });
    }
}
