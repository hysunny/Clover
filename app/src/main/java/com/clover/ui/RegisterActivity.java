package com.clover.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class RegisterActivity extends BaseActivity{
    private EditText et_account;
    private EditText et_password;
    private EditText et_pwdAgain;
    private EditText et_nickname;
    private Button bt_register;
    private String username;
    private String password;
    private String pwdAgain;
    private String nickname;
    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioGroup radioGroup;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initView();

        bt_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registe();
            }
        });

        et_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUsername();
                }

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == radioButton0.getId()){
                    user.setSex(false);//男
                }
                if(checkedId == radioButton1.getId()){
                    user.setSex(true);//女
                }
            }
        });
        radioButton0.setChecked(true);

    }

    private void initView(){
        initToolbar("注册",new Intent(this, LoginActivity.class), this);
        et_account = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_pwdAgain = (EditText) findViewById(R.id.et_pwd_again);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        bt_register = (Button) findViewById(R.id.btn_register);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton0 = (RadioButton) findViewById(R.id.radioGroupButton0);
        radioButton1 = (RadioButton) findViewById(R.id.radioGroupButton1);
    }

    private void registe(){
        username = et_account.getText().toString();
        password = et_password.getText().toString();
        pwdAgain = et_pwdAgain.getText().toString();
        nickname = et_nickname.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(nickname)){
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(pwdAgain)){
            Toast.makeText(this, "两次密码不符", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在注册，请稍等...");
        dialog.setCancelable(false);
        dialog.show();
        ShowLog("dialog......");

        user.setUsername(username);
        user.setPassword(password);
        user.setNick(nickname);
        ShowLog("signup......");
        user.signUp(RegisterActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                ShowToast("注册成功");
                dialog.dismiss();
                BmobQuery<Relationship> query = new BmobQuery<Relationship>();
                //query.addWhereEndsWith("")
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                ShowToast("注册失败");
                dialog.dismiss();
            }
        });
    }

    private void checkUsername(){
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", et_account.getText().toString());
        query.findObjects(this, new FindListener<User>() {

            @Override
            public void onSuccess(List<User> arg0) {
                if(arg0!=null&&(arg0.size()>0)){
                    ShowToast("用户名已经被使用");
                    et_account.requestFocus();
                }
            }
            @Override
            public void onError(int arg0, String arg1) {
            }
        });
    }
}
