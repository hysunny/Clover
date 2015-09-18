package com.clover.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clover.R;
import com.clover.net.BmobRequest;



public class TTTAActivity extends BaseActivity {

    String GAME_START_ACTION = "GAME_START";
    private Button btn_Start_TwoPeople;
    String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttta);
        initToolbar(getResources().getString(R.string.Game_Title_Tictactoe),new Intent(this, MainActivity.class), this);
        btn_Start_TwoPeople = (Button)findViewById(R.id.btn_start_two);


        btn_Start_TwoPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = "GAME_INVITE";
                Toast.makeText(getApplicationContext(),R.string.TTTActivity_Game_InviteMsg,
                        Toast.LENGTH_SHORT).show();
                BmobRequest.pushMessageToLover(msg, "GAME_INVITE", TTTAActivity.this,application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                btn_Start_TwoPeople.setEnabled(false);
            }
        });

        registerBoradcastReceiver();
    }

    private BroadcastReceiver gameStartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            int extra = intent.getExtras().getInt("key");
            switch (extra) {
                case 1:

                    break;
                case 2:
                    Toast.makeText(
                            getApplicationContext(),R.string.TTTA_Game_Invited,
                            Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(TTTAActivity.this, TTTATPActivity.class);

                    startActivity(intent2);

            }
            abortBroadcast();
        }
    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(GAME_START_ACTION);
        //注册广播
        registerReceiver(gameStartReceiver, myIntentFilter);
    }

    public void startTwoPeople(View v){
        Intent intent = new Intent(this,TTTATPActivity.class);
        startActivity(intent);
    }

    public  void  startPlayWithComputer(View v){
        Intent intent = new Intent(this,TTTAPWCActivity.class);
        startActivity(intent);
    }

}
