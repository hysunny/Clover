package com.clover.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.clover.R;
import com.clover.entities.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class HealthActivity extends BaseActivity {

    private RelativeLayout rv_menses;//布局
    private RelativeLayout rv_disease;//按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        initToolbar(getResources().getString(R.string.title_activity_health),new Intent(this, MainActivity.class), this);
        initView();
    }

    private void initView(){
        rv_menses = (RelativeLayout)findViewById(R.id.layout_menses);
        rv_disease = (RelativeLayout)findViewById(R.id.layout_disease);

        rv_menses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthActivity.this, MensesActivity.class);
                startActivity(intent);
            }
        });
        rv_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthActivity.this, DiseaseActivity.class);
                startActivity(intent);
            }
        });
    }

}
