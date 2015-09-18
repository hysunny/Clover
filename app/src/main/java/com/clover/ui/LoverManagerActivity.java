package com.clover.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.clover.utils.CloverApplication;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class LoverManagerActivity extends BaseActivity {
    private EditText et_lovername;
    private Button bt_search;
    //搜索后弹出布局
    private LinearLayout ll_result;
    private TextView tv_lovername;
    private Button bt_bind;

    private Boolean isSetLover;
    private CloverApplication application;
    private User user;
    private Relationship relationship;
    private User temp_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lover_manager);
        application = (CloverApplication) getApplication();
        application.setOne_user(null);
        relationship = new Relationship();
        initToolbar("绑定情侣", new Intent(this, MainActivity.class), this);

        initView();
    }

    private void initView(){
        et_lovername = (EditText) findViewById(R.id.et_lovername);
        bt_search = (Button) findViewById(R.id.bt_search);
        ll_result = (LinearLayout) findViewById(R.id.layout_search_result);
        tv_lovername = (TextView) findViewById(R.id.tv_result);
        bt_bind = (Button) findViewById(R.id.bt_bind);

        bt_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSetLover && temp_user != null){
                    BmobMsg msg = BmobMsg.createTextSendMsg(LoverManagerActivity.this,temp_user.getObjectId() , BmobChatUser.getCurrentUser(LoverManagerActivity.this).getUsername());
                    msg.setExtra("invisitBind");
                    chatManager.sendTextMessage(temp_user, msg);
                    ShowToast("邀请成功");
                    bt_bind.setClickable(false);
                    bt_bind.setText("已邀请");

                }
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lovername = et_lovername.getText().toString();
                if(TextUtils.isEmpty(lovername)){
                    ShowToast("请输入对方账号");
                    return;
                }
                final ProgressDialog dialog = new ProgressDialog(LoverManagerActivity.this);
                dialog.setMessage("正在搜索，请稍等...");
                dialog.setCancelable(true);
                dialog.show();


                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("username", lovername);
                query.findObjects(LoverManagerActivity.this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if(list.size()>0){
                            ll_result.setVisibility(View.VISIBLE);
                            user = list.get(0);
                            tv_lovername.setText(user.getUsername());
                            temp_user = list.get(0);
                            isSetLover = true;
                            dialog.dismiss();
                        }else{
                            ShowToast("不存在此用户");
                        }

                    }

                    @Override
                    public void onError(int i, String s) {
                        ShowToast("搜索失败");
                    }
                });

            }
        });

    }

}
