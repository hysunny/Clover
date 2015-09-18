package com.clover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.clover.R;
import com.clover.adapter.MdListViewAdapter;
import com.clover.entities.Mood;
import com.clover.entities.User;
import com.clover.view.XListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class SpaceActivity extends BaseActivity implements XListView.IXListViewListener{
    private MdListViewAdapter mdListViewAdapter;
    private XListView mdListView;
    private List<Mood> mdListData;
    private ImageView iv;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        initView();
        initData(false);

    }

    private void initView(){
        initToolbar("空间",new Intent(this, MainActivity.class), this);

        mdListView = (XListView) findViewById(R.id.lv_mood);
        mdListView.setXListViewListener(this);
        iv = (ImageView) findViewById(R.id.iv_add);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpaceActivity.this, AddMoodActivity.class);
                startActivity(intent);
            }
        });
        mHandler = new Handler();

    }

    private void initData(boolean isRefresh){
        BmobQuery<Mood> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("belongId", BmobChatUser.getCurrentUser(this, User.class).getObjectId());
        BmobQuery<Mood> eq2 = new BmobQuery<Mood>();
        eq2.addWhereEqualTo("belongId", application.getOne_user().getObjectId());
        List<BmobQuery<Mood>> queries = new ArrayList<BmobQuery<Mood>>();
        queries.add(eq1);
        queries.add(eq2);
        BmobQuery<Mood> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        if(isRefresh){
            mainQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        }else {
            mainQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        }

        mainQuery.findObjects(this, new FindListener<Mood>() {
            @Override
            public void onSuccess(List<Mood> object) {
                if(object.size()>0){
                    mdListData = object;
                    ShowLog("查询成功");
                    mdListViewAdapter = new MdListViewAdapter(SpaceActivity.this, mdListData);
                    mdListView.setAdapter(mdListViewAdapter);
                }

            }
            @Override
            public void onError(int code, String msg) {
                ShowLog("查询失败");
            }
        });

    }
    private void onLoad() {
        mdListView.stopRefresh();
        mdListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //start = ++refreshCnt;
                mdListData.clear();
                initData(true);
                // mAdapter.notifyDataSetChanged();
                mdListViewAdapter = new MdListViewAdapter(SpaceActivity.this, mdListData);
                mdListView.setAdapter(mdListViewAdapter);
                onLoad();
            }
        }, 2000);
    }


}
