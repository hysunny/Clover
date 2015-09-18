package com.clover.ui;

import android.content.Intent;
import android.os.Bundle;

import com.clover.R;

public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initToolbar("关于", new Intent(this, MainActivity.class), this);
    }
}
