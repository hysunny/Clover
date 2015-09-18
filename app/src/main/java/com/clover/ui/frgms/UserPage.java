package com.clover.ui.frgms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clover.R;
import com.clover.entities.User;
import com.clover.ui.AboutUsActivity;
import com.clover.ui.LoginActivity;
import com.clover.ui.LoverManagerActivity;
import com.clover.ui.SettingActivity;
import com.clover.ui.UserInfoUpdateActivity;
import com.clover.utils.CloverApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import cn.bmob.im.BmobUserManager;

public class UserPage extends Fragment implements OnClickListener{

    private RelativeLayout rl_userinfo_update;
    private RelativeLayout rl_lover_set;
    private RelativeLayout rl_setting;
    private TextView tv_logout;
    private Activity activity;
    private CloverApplication application;
    private TextView tv_loverinfo_vg;
    private ImageView iv_userAvatar;
    private TextView tv_userNick;
    private TextView tv_userSex;
    private TextView tv_userAge;
    private TextView tv_about;
    BmobUserManager userManager;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        application = (CloverApplication) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_user_page, container, false);
        initView(view);

		return view;
	}


	private void initView(View view){
        activity = getActivity();
        userManager = BmobUserManager.getInstance(activity);
        rl_userinfo_update = (RelativeLayout) view.findViewById(R.id.layout_set_userinfo);
        rl_lover_set = (RelativeLayout) view.findViewById(R.id.layout_lover_set);
	    rl_setting = (RelativeLayout) view.findViewById(R.id.layout_setting);
        tv_logout = (TextView) view.findViewById(R.id.tv_logout);

        tv_loverinfo_vg = (TextView) view.findViewById(R.id.tv_loverinfo_vg);

        iv_userAvatar = (ImageView) view.findViewById(R.id.useravatar);
        tv_userNick = (TextView) view.findViewById(R.id.userName);
        tv_userSex = (TextView)view.findViewById(R.id.userSex);
        tv_userAge = (TextView)view.findViewById(R.id.userAge);
        tv_about = (TextView) view.findViewById(R.id.tv_about);


        rl_userinfo_update.setOnClickListener(this);
        rl_lover_set.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_about.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLover();
        refreshUser(userManager.getCurrentUser(User.class));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.layout_set_userinfo:
                intent = new Intent(activity, UserInfoUpdateActivity.class);
                intent.putExtra("tag","me");
                startActivity(intent);
                break;
            case R.id.layout_lover_set:
                if(application.getOne_user() == null){
                    intent = new Intent(activity, LoverManagerActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(activity, UserInfoUpdateActivity.class);
                    intent.putExtra("tag","lover");
                    startActivity(intent);
                }
                break;
            case R.id.layout_setting:
                intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_logout:
                BmobUserManager userManager = BmobUserManager.getInstance(activity);
                userManager.logout();
                intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
                break;
            case R.id.tv_about:
                intent = new Intent(activity, AboutUsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void checkLover(){
        if(application.getOne_user() != null){
            tv_loverinfo_vg.setText(application.getOne_user().getNick());

        }else {
            tv_loverinfo_vg.setText("点击绑定情侣");
        }
    }

    private void refreshUser(User user){
        refreshAvatar(user);
        refreshNick(user);
        refreshAge(user);
        refreshSex(user);
    }

    /**
     * 刷新头像
     */
    private void refreshAvatar(User user){
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.equals("")) {
            ImageLoader.getInstance().displayImage(avatarUrl, iv_userAvatar,
                    new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisc(true)
                            .considerExifParams(true)
                            .imageScaleType(ImageScaleType.EXACTLY)//设置图片编码方式
                            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                            .considerExifParams(true)
                            .resetViewBeforeLoading(true)
                            .displayer(new FadeInBitmapDisplayer(100))// 淡入
                            .build());
        } else {
            iv_userAvatar.setImageResource(R.mipmap.photo);
        }
    }

    /**
     * 刷新昵称
     */
    public void refreshNick(User user){
        if(user.getNick()==null){
            tv_userNick.setText("");
        }else{
            tv_userNick.setText(user.getNick());
        }
    }

    /**
     * 刷新年龄
     */
    public void refreshAge(User user){
        if(user.getAge()==null){
            tv_userAge.setText("0");
        }else{
            tv_userAge.setText(String.valueOf(user.getAge()));
        }
    }

    /**
     * 刷新性别
     */
    private void refreshSex(User user){

        if(user.getSex()==null){
            tv_userSex.setText("");
        }else if(user.getSex()){
            tv_userSex.setText(this.getResources().getString(R.string.woman));
        }else if(!user.getSex()){
            tv_userSex.setText(this.getResources().getString(R.string.man));
        }
    }

}
