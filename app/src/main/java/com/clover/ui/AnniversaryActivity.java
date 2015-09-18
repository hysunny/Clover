package com.clover.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.R;
import com.clover.entities.Relationship;
import com.clover.entities.User;
import com.clover.utils.CloverApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.listener.UpdateListener;


public class AnniversaryActivity extends BaseActivity{

    private TextView tv_timeUntilNow, tv_anniversaryDate;//到现在的天数，纪念日的日期
    private Button btn_save;
    CloverApplication application;
    User user;
    Relationship relationship;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);

        initToolbar(getResources().getString(R.string.title_activity_anniversary),new Intent(this, MainActivity.class), this);
        application = (CloverApplication) getApplication();
        preferences = getSharedPreferences("lover", MODE_PRIVATE);
        editor = preferences.edit();
        relationship = application.getRelationship();
        user = userManager.getCurrentUser(User.class);
        tv_timeUntilNow = (TextView)findViewById(R.id.timeUntilNow);
        tv_anniversaryDate = (TextView)findViewById(R.id.anniversaryDate);
        btn_save = (Button)findViewById(R.id.save);

        tv_anniversaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(AnniversaryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1;
                                String date = year+"-"+ monthOfYear+"-"+dayOfMonth;
                                tv_anniversaryDate.setText(date);
                                String nowdate = getNowDate();
                                long timetilnow = getcal(date,nowdate);
                                String timeutilnow = Long.toString(timetilnow);
                                tv_timeUntilNow.setText(timeutilnow);
                            }
                        },now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAnniversary();
            }
        });
        refreshDay();
    }

    public static long getcal(String time1,String time2){
        long js=0;
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date1=ft.parse(time1);
            Date date2=ft.parse(time2);
            js=date2.getTime()-date1.getTime();
            js=js/1000/60/60/24;
        }catch(java.text.ParseException e){
            e.printStackTrace();
        }
        return js;
    }

    private void refreshDay() {
        if (preferences.getString("date",getNowDate()) != null) {
            tv_anniversaryDate.setText(preferences.getString("date",getNowDate()));
            long timetilnow = getcal(preferences.getString("date",getNowDate()), getNowDate());
            String timeutilnow = Long.toString(timetilnow);
            tv_timeUntilNow.setText(timeutilnow);
        } else {
            tv_anniversaryDate.setText(getNowDate());
            tv_timeUntilNow.setText("0");
        }
    }

    private void updateAnniversary(){
        Relationship r = new Relationship();
        r.setDate(tv_anniversaryDate.getText().toString());
        r.setObjectId(preferences.getString("reObjectId", ""));
        r.update(AnniversaryActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AnniversaryActivity.this, R.string.updatesuccess, Toast.LENGTH_SHORT).show();
                editor.putString("date",tv_anniversaryDate.getText().toString());
                editor.commit();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AnniversaryActivity.this,R.string.updatefailure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String getNowDate(){
        Calendar now = Calendar.getInstance();
        int yearnow = now.get(Calendar.YEAR);
        int monthnow = now.get(Calendar.MONTH)+1;
        int daynow = now.get(Calendar.DAY_OF_MONTH);
        String nowdate = yearnow+"-"+monthnow+"-"+daynow;
        return nowdate;
    }

}
