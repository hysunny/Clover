package com.clover.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.clover.R;
import com.clover.net.BmobRequest;


public class TTTATPActivity extends BaseActivity implements OnClickListener {

    private Button btn_One, btn_Two, btn_Three, btn_Four, btn_Five, btn_Six,
            btn_Seven, btn_Eight,btn_Nine;
    String  msg;
    String ooxx ="O";
    String ooxx1 = "O";
    String GAME_BUTTON_ACTION = "GAME_BUTTON";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tttatp);
        initToolbar(getResources().getString(R.string.Game_Title_Tictactoe),new Intent(this, TTTAActivity.class), this);

        btn_One = (Button) findViewById(R.id.one);
        btn_Two = (Button) findViewById(R.id.two);
        btn_Three = (Button) findViewById(R.id.three);
        btn_Four = (Button) findViewById(R.id.four);
        btn_Five = (Button) findViewById(R.id.five);
        btn_Six = (Button) findViewById(R.id.six);
        btn_Seven = (Button) findViewById(R.id.seven);
        btn_Eight = (Button) findViewById(R.id.eight);
        btn_Nine = (Button) findViewById(R.id.nine);

        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);

        registerGameBoradcastReceiver();
    }

    @Override
    public void onClick(View v) {
        if(ooxx == "X"){
            msg = "I_AM_X";
            BmobRequest.pushMessageToLover(msg, "I_AM_X",TTTATPActivity.this,
                    application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
            ooxx1 = "X";
        }
        if(ooxx == "O"){
            msg = "I_AM_O";
            BmobRequest.pushMessageToLover(msg, "I_AM_O",TTTATPActivity.this,
                    application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
            ooxx1 = "O";
        }
        switch (v.getId()) {
            case R.id.one:
                if("".equals(btn_One.getText().toString()) ) {
                    msg = "BTN1_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN1_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_One.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.two:
                if("".equals(btn_Two.getText().toString()) ) {
                    msg = "BTN2_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN2_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Two.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.three:
                if("".equals(btn_Three.getText().toString()) ) {
                    msg = "BTN3_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN3_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Three.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.four:
                if("".equals(btn_Four.getText().toString()) ) {
                    msg = "BTN4_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN4_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);

                    btn_Four.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.five:
                if("".equals(btn_Five.getText().toString()) ) {
                    msg = "BTN5_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN5_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Five.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.six:
                if("".equals(btn_Six.getText().toString()) ) {
                    msg = "BTN6_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN6_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Six.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.seven:
                if("".equals(btn_Seven.getText().toString()) ) {
                    msg = "BTN7_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN7_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Seven.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.eight:
                if("".equals(btn_Eight.getText().toString()) ) {
                    msg = "BTN8_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN8_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Eight.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nine:
                if("".equals(btn_Nine.getText().toString()) ) {
                    msg = "BTN9_CLICKED";
                    BmobRequest.pushMessageToLover(msg, "BTN9_CLICKED",TTTATPActivity.this,
                            application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
                    btn_Nine.setText(ooxx);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if((btn_One.getText()==ooxx&&btn_Two.getText()==ooxx&&btn_Three.getText()==ooxx)
                ||(btn_One.getText()==ooxx&&btn_Five.getText()==ooxx&&btn_Nine.getText()==ooxx)
                ||(btn_One.getText()==ooxx&&btn_Four.getText()==ooxx&&btn_Seven.getText()==ooxx)
                ||(btn_Two.getText()==ooxx&&btn_Five.getText()==ooxx&&btn_Eight.getText()==ooxx)
                ||(btn_Three.getText()==ooxx&&btn_Six.getText()==ooxx&&btn_Nine.getText()==ooxx)
                ||(btn_Four.getText()==ooxx&&btn_Five.getText()==ooxx&&btn_Six.getText()==ooxx)
                ||(btn_Three.getText()==ooxx&&btn_Five.getText()==ooxx&&btn_Seven.getText()==ooxx)
                ||(btn_Seven.getText()==ooxx&&btn_Eight.getText()==ooxx&&btn_Nine.getText()==ooxx)
                ){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.TZFEActivity_Game_ResultTitle3)
                    .setMessage(R.string.TTTA_Game_AlertMsg3)
                    .setNegativeButton(R.string.TTTA_Game_AlertCancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.TTTA_Game_AlertSure,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialoginterface, int i){
                                    reset();
                                }
                            })
                    .show();
            msg = "YOU_LOSE";
            BmobRequest.pushMessageToLover(msg, "YOU_LOSE",TTTATPActivity.this,
                    application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
        }
        else if(!(btn_One.getText()=="X"&&btn_Two.getText()=="X"&&btn_Three.getText()=="X")
                &&!(btn_One.getText()=="X"&&btn_Five.getText()=="X"&&btn_Nine.getText()=="X")
                &&!(btn_One.getText()=="X"&&btn_Four.getText()=="X"&&btn_Seven.getText()=="X")
                &&!(btn_Two.getText()=="X"&&btn_Five.getText()=="X"&&btn_Eight.getText()=="X")
                &&!(btn_Three.getText()=="X"&&btn_Six.getText()=="X"&&btn_Nine.getText()=="X")
                &&!(btn_Four.getText()=="X"&&btn_Five.getText()=="X"&&btn_Six.getText()=="X")
                &&!(btn_Three.getText()=="X"&&btn_Five.getText()=="X"&&btn_Seven.getText()=="X")
                &&!(btn_Seven.getText()=="X"&&btn_Eight.getText()=="X"&&btn_Nine.getText()=="X")
                &&!
                (btn_One.getText()=="O"&&btn_Two.getText()=="O"&&btn_Three.getText()=="O")
                &&!(btn_One.getText()=="O"&&btn_Five.getText()=="O"&&btn_Nine.getText()=="O")
                &&!(btn_One.getText()=="O"&&btn_Four.getText()=="O"&&btn_Seven.getText()=="O")
                &&!(btn_Two.getText()=="O"&&btn_Five.getText()=="O"&&btn_Eight.getText()=="O")
                &&!(btn_Three.getText()=="O"&&btn_Six.getText()=="O"&&btn_Nine.getText()=="O")
                &&!(btn_Four.getText()=="O"&&btn_Five.getText()=="O"&&btn_Six.getText()=="O")
                &&!(btn_Three.getText()=="O"&&btn_Five.getText()=="O"&&btn_Seven.getText()=="O")
                &&!(btn_Seven.getText()=="O"&&btn_Eight.getText()=="O"&&btn_Nine.getText()=="O")
                &&!("".equals(btn_One.getText().toString()))
                &&!("".equals(btn_Two.getText().toString()))
                &&!("".equals(btn_Three.getText().toString()))
                &&!("".equals(btn_Four.getText().toString()))
                &&!("".equals(btn_Five.getText().toString()))
                &&!("".equals(btn_Six.getText().toString()))
                &&!("".equals(btn_Seven.getText().toString()))
                &&!("".equals(btn_Eight.getText().toString()))
                &&!("".equals(btn_Nine.getText().toString()))
                ){
            new AlertDialog.Builder(this)
                    .setTitle("GAME OVER")
                    .setMessage(R.string.TTTA_Game_AlertMsg1)
                    .setNegativeButton(R.string.TTTA_Game_AlertCancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.TTTA_Game_AlertSure,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    reset();
                                }
                            })
                    .show();
            msg = "TIE_SCORE";
            BmobRequest.pushMessageToLover(msg, "TIE_SCORE",TTTATPActivity.this,
                    application.getOne_user().getObjectId(), application.getOne_user(), chatManager);

        }

    }


    private BroadcastReceiver gameMessageReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int extra = intent.getExtras().getInt("key");
            switch(extra){
                case 1:
                    btn_One.setText(ooxx1);
                    break;
                case 2:
                    btn_Two.setText(ooxx1);
                    break;
                case 3:
                    btn_Three.setText(ooxx1);
                    break;
                case 4:
                    btn_Four.setText(ooxx1);
                    break;
                case 5:
                    btn_Five.setText(ooxx1);
                    break;
                case 6:
                    btn_Six.setText(ooxx1);
                    break;
                case 7:
                    btn_Seven.setText(ooxx1);
                    break;
                case 8:
                    btn_Eight.setText(ooxx1);
                    break;
                case 9:
                    btn_Nine.setText(ooxx1);
                    break;
                case 10:
                    new AlertDialog.Builder(TTTATPActivity.this)
                            .setTitle(R.string.TZFEActivity_Game_ResultTitle2)
                            .setMessage(R.string.TTTA_Game_AlertMsg2)
                            .setNegativeButton(R.string.TTTA_Game_AlertCancel,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(R.string.TTTA_Game_AlertSure,
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialoginterface, int i){
                                            reset();
                                        }
                                    })
                            .show();
                    break;
                case 11:
                    new AlertDialog.Builder(TTTATPActivity.this)
                            .setTitle("GAME OVER")
                            .setMessage(R.string.TTTA_Game_AlertMsg1)
                            .setNegativeButton(R.string.TTTA_Game_AlertCancel,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(R.string.TTTA_Game_AlertSure,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialoginterface, int i) {
                                            reset();
                                        }
                                    })
                            .show();
                    break;
                case 12:
                    new AlertDialog.Builder(TTTATPActivity.this)
                            .setTitle(R.string.TTTA_Game_AlertTitle)
                            .setMessage(R.string.TTTA_Game_ReStartMsg)
                            .setNegativeButton(R.string.TTTA_Game_AlertRefuse,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton(R.string.TTTActivity_Game_AlertSure,
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialoginterface, int i){
                                            reset();
                                        }
                                    })
                            .show();

                case 13:
                    ooxx = "X";
                    ooxx1 = "O";
                    break;
                case 14:
                    ooxx = "O";
                    ooxx1 = "X";
                    break;

            }
            //终结广播
            abortBroadcast();
        }
    };
    /**
     * 注册该广播接收
     */
    public void registerGameBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(GAME_BUTTON_ACTION);

        //注册广播
        registerReceiver(gameMessageReceiver, myIntentFilter);
    }

    public void clickBtnRestart(View v){
        msg = "RESTART_REQUEST";
        BmobRequest.pushMessageToLover(msg, "RESTART_REQUEST",TTTATPActivity.this,
                application.getOne_user().getObjectId(), application.getOne_user(), chatManager);
        reset();
    };

    private void reset(){
        btn_One.setText("");
        btn_Two.setText("");
        btn_Three.setText("");
        btn_Four.setText("");
        btn_Five.setText("");
        btn_Six.setText("");
        btn_Seven.setText("");
        btn_Eight.setText("");
        btn_Nine.setText("");
    }
    }
