package com.clover.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.clover.ui.frgms.GamePage;
import com.clover.ui.frgms.MainPage;
import com.clover.ui.frgms.UserPage;
import com.clover.utils.CloverApplication;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    private ViewPager mPager;//页卡内容
    private ArrayList<Fragment> fragments; // Tab页面列表
    //private ImageView cursor;// 动画图片
    private TextView t1, t2, t3;// 页卡头标
    private Button bt_chat;
    private Button bt_space;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private CloverApplication application;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(userManager.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            application = (CloverApplication)getApplication();
           // requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main);
            preferences = getSharedPreferences("lover", MODE_PRIVATE);
            editor = preferences.edit();
            int tag = getIntent().getIntExtra("tag", 0);
            if((!getLover().getObjectId().equals(""))&&getLover()!=null){
                application.setOne_user(getLover());
                application.setRelationship(getRelationship());
              //  ShowToast("lover"+getLover().getUsername());
            }else {
                initApplication();
            }
            if(tag == 1){
                if( application.getOne_user() != null){
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                }

            }
            chatManager = BmobChatManager.getInstance(this);
            InitTextView();
            InitImageView();
            InitViewPager();
            t1.setBackgroundColor(getResources().getColor(R.color.material_green_A200));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
      //  if(application.getOne_user() == null){
            initApplication();

    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<>();
        Fragment fragment1 = new MainPage();
        Fragment fragment2 = new GamePage();
        Fragment fragment3 = new UserPage();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
        t3 = (TextView) findViewById(R.id.text3);
        bt_chat = (Button) findViewById(R.id.chart_bar);
        bt_space = (Button) findViewById(R.id.space_bar);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
        t3.setOnClickListener(new MyOnClickListener(2));
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(application.getOne_user() != null){
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }else{
                    initApplication();

                }

            }
        });
        bt_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (application.getOne_user() != null) {
                    Intent intent1 = new Intent(MainActivity.this, SpaceActivity.class);
                    startActivity(intent1);
                } else {
                    initApplication();
                }
            }
        });
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public MyPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
            super(fm);
            this.list = list;

        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }


    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
       // cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
       // cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

       // int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
     //   int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
          //  Animation animation = null;
            switch (arg0) {
                case 0:
                    t1.setBackgroundColor(getResources().getColor(R.color.material_green_A200));
                    if (currIndex == 1) {
                        t2.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                     //   animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        t3.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                     //   animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    t2.setBackgroundColor(getResources().getColor(R.color.material_green_A200));
                    if (currIndex == 0) {
                        t1.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                   //     animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        t3.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                     //   animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    t3.setBackgroundColor(getResources().getColor(R.color.material_green_A200));
                    if (currIndex == 0) {
                        t1.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                   //     animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        t2.setBackgroundColor(getResources().getColor(R.color.toolbar_bg_color));
                     //   animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
         //   animation.setFillAfter(true);// True:图片停在动画结束位置
          //  animation.setDuration(300);
         //   cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }


    private void initApplication(){
        BmobQuery<Relationship> query = new BmobQuery<>();
        query.addWhereEqualTo("m_user", BmobChatUser.getCurrentUser(this));
        BmobQuery<Relationship> queryW = new BmobQuery<>();
        queryW.addWhereEqualTo("w_user", BmobChatUser.getCurrentUser(this));
        List<BmobQuery<Relationship>> queries = new ArrayList<BmobQuery<Relationship>>();
        queries.add(query);
        queries.add(queryW);
        BmobQuery<Relationship> mainQuery = new BmobQuery<>();
        BmobQuery<Relationship> or = mainQuery.or(queries);
        or.findObjects(this, new FindListener<Relationship>() {
            @Override
            public void onSuccess(List<Relationship> list) {

                if (list.size() <= 0) {
                    application.setOne_user(null);
                    application.setRelationship(null);
                    clearData();
                    ShowToast("请设置情侣");
                    if(preferences.getInt("count",1) == 1){
                        Intent intent = new Intent(MainActivity.this, LoverManagerActivity.class);
                        startActivity(intent);
                        editor.putInt("count", 2);
                        editor.commit();
                    }
                } else {
                    saveRelationship(list.get(0));
                    findLoverUser();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void findLoverUser(){
        BmobQuery<User> queryLover = new BmobQuery<>();
        queryLover.addWhereEqualTo("objectId", getLover().getObjectId());
        queryLover.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if(list.size()>0){
                    saveLover(list.get(0));
                    application.setOne_user(list.get(0));
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    public void saveLover(User user){
        if(user.getUsername()!=null){
            editor.putString("username", user.getUsername());
        }
        if(user.getObjectId()!=null){
            editor.putString("objectId", user.getObjectId());
        }
        if(user.getAvatar()!=null){
            editor.putString("avatar", user.getAvatar());
        }
        if(user.getNick()!=null){
            editor.putString("nick", user.getNick());
        }
        if(user.getSex()!=null){
            editor.putBoolean("sex", user.getSex());
        }
        if(user.getAge()!=null){
            editor.putInt("age", user.getAge());
        }
        if(user.getSleep()!=null){
            editor.putInt("sleep", user.getSleep());
        }
        if(user.getDisease()!=null){
            editor.putInt("disease", user.getDisease());
        }
        if(user.getMenses()!= null){
            editor.putInt("menses", user.getMenses());
        }
        editor.commit();
    }

    public User getLover(){
        User user = new User();
        user.setUsername(preferences.getString("username", ""));
        user.setObjectId(preferences.getString("objectId", ""));
        user.setAvatar(preferences.getString("avatar", ""));
        user.setNick(preferences.getString("nick", ""));
        user.setSex(preferences.getBoolean("sex", true));
        user.setAge(preferences.getInt("age", 2));
        user.setSleep(preferences.getInt("sleep", 2));
        user.setDisease(preferences.getInt("disease", 2));
        user.setMenses(preferences.getInt("menses", 2));
        return user;
    }

public void saveRelationship(Relationship relationship){
    User lover=null;
    if(relationship.getM_user().getObjectId().equals(BmobChatUser.getCurrentUser(this, User.class).getObjectId()) ){
        if(relationship.getW_user() != null){
            lover = relationship.getW_user();
            //lover用户是M_user还是W_user
            editor.putString("position","W");
        }
    } else {
        if(relationship.getM_user() != null){
            lover = relationship.getM_user();
            editor.putString("position","M");
        }
    }
    saveLover(lover);

    if(relationship.getDate()!=null){
        editor.putString("date", relationship.getDate());
    }
    if(relationship.getObjectId()!=null){
        editor.putString("reObjectId", relationship.getObjectId());
    }
    editor.commit();

}

    public Relationship getRelationship(){
        SharedPreferences preferences = getSharedPreferences("lover", MODE_PRIVATE);
        Relationship relationship = new Relationship();
        User lover = getLover();
        if(preferences.getString("position","M").equals("M")){
            relationship.setM_user(lover);
            relationship.setW_user(BmobChatUser.getCurrentUser(this, User.class));
        }else{
            relationship.setW_user(lover);
            relationship.setM_user(BmobChatUser.getCurrentUser(this, User.class));
        }
        relationship.setObjectId(preferences.getString("reObjectId", ""));
        relationship.setDate(preferences.getString("date", ""));
        return relationship;
    }

    /*
    清除本地数据
     */
    public void clearData(){
        SharedPreferences.Editor editor = getSharedPreferences("lover", MODE_PRIVATE).edit();
        editor.remove("username");
        editor.remove("objectId");
        editor.remove("avatar");
        editor.remove("nick");
        editor.remove("sex");
        editor.remove("age");
        editor.remove("position");
        editor.remove("date");
        editor.remove("reObjectId");
        editor.remove("sleep");
        editor.remove("disease");
        editor.remove("menses");
        editor.commit();
    }

}
