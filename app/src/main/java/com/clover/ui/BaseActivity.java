package com.clover.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.R;
import com.clover.utils.CloverApplication;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

public class BaseActivity extends AppCompatActivity {

	BmobUserManager userManager;
    BmobChatManager chatManager;
    CloverApplication application;
    //String APPID = "551b50c06f7edb512c12fcddbeec4925";
    String APPID = "85e40757e81851d007990f3e103ec5ae";
	private Toolbar toolbar;

	protected void onCreate(android.os.Bundle arg0) {
		super.onCreate(arg0);
		userManager = BmobUserManager.getInstance(this);
        BmobChat.getInstance(this).init(APPID);
        chatManager = BmobChatManager.getInstance(this);
        application = (CloverApplication) getApplication();
	}
	
	Toast mToast;
	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}
	
	public void ShowLog(String msg){
		Log.i("zpfang",msg);
	}

	public void initToolbar(String str, final Intent intent, final Activity activity){
		ToolbarHolder holder = new ToolbarHolder();
		holder.iv_back = (ImageView) findViewById(R.id.iv_back);
		holder.tv_title = (TextView) findViewById(R.id.tv_name);

		holder.tv_title.setText(str);
		holder.iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(intent);
				activity.finish();
			}
		});
	}

	private class ToolbarHolder{
		ImageView iv_back;
		TextView tv_title;
	}



}
