package com.clover.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.clover.R;

import java.util.Random;

public class TTTAPWCActivity  extends BaseActivity {


    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine;

    public void clickBtn(View v){
        Button[] arrayBtn = {btnOne,btnTwo,btnThree,btnFour,btnFive,btnSix,
                btnSeven,btnEight,btnNine};
        switch (v.getId()) {
            case R.id.one:
                if("".equals(btnOne.getText().toString()) ) {
                    btnOne.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.two:
                if("".equals(btnTwo.getText().toString()) ) {
                    btnTwo.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.three:
                if("".equals(btnThree.getText().toString()) ) {
                    btnThree.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.four:
                if("".equals(btnFour.getText().toString()) ) {
                    btnFour.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.five:
                if("".equals(btnFive.getText().toString()) ) {
                    btnFive.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.six:
                if("".equals(btnSix.getText().toString()) ) {
                    btnSix.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.seven:
                if("".equals(btnSeven.getText().toString()) ) {
                    btnSeven.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.eight:
                if("".equals(btnEight.getText().toString()) ) {
                    btnEight.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nine:
                if("".equals(btnNine.getText().toString()) ) {
                    btnNine.setText("O");
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.TTTA_Game_PlaceMsg,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }

        Computer(arrayBtn);

        //the result
        if((btnOne.getText()=="O"&&btnTwo.getText()=="O"&&btnThree.getText()=="O")
                ||(btnOne.getText()=="O"&&btnFive.getText()=="O"&&btnNine.getText()=="O")
                ||(btnOne.getText()=="O"&&btnFour.getText()=="O"&&btnSeven.getText()=="O")
                ||(btnTwo.getText()=="O"&&btnFive.getText()=="O"&&btnEight.getText()=="O")
                ||(btnThree.getText()=="O"&&btnSix.getText()=="O"&&btnNine.getText()=="O")
                ||(btnFour.getText()=="O"&&btnFive.getText()=="O"&&btnSix.getText()=="O")
                ||(btnThree.getText()=="O"&&btnFive.getText()=="O"&&btnSeven.getText()=="O")
                ||(btnSeven.getText()=="O"&&btnEight.getText()=="O"&&btnNine.getText()=="O")
                ){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.TZFEActivity_Game_ResultTitle3)
                    .setMessage(R.string.TTTA_Game_AlertMsg4)
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
        }
        else if(!(btnOne.getText()=="X"&&btnTwo.getText()=="X"&&btnThree.getText()=="X")
                &&!(btnOne.getText()=="X"&&btnFive.getText()=="X"&&btnNine.getText()=="X")
                &&!(btnOne.getText()=="X"&&btnFour.getText()=="X"&&btnSeven.getText()=="X")
                &&!(btnTwo.getText()=="X"&&btnFive.getText()=="X"&&btnEight.getText()=="X")
                &&!(btnThree.getText()=="X"&&btnSix.getText()=="X"&&btnNine.getText()=="X")
                &&!(btnFour.getText()=="X"&&btnFive.getText()=="X"&&btnSix.getText()=="X")
                &&!(btnThree.getText()=="X"&&btnFive.getText()=="X"&&btnSeven.getText()=="X")
                &&!(btnSeven.getText()=="X"&&btnEight.getText()=="X"&&btnNine.getText()=="X")
                &&!
                (btnOne.getText()=="O"&&btnTwo.getText()=="O"&&btnThree.getText()=="O")
                &&!(btnOne.getText()=="O"&&btnFive.getText()=="O"&&btnNine.getText()=="O")
                &&!(btnOne.getText()=="O"&&btnFour.getText()=="O"&&btnSeven.getText()=="O")
                &&!(btnTwo.getText()=="O"&&btnFive.getText()=="O"&&btnEight.getText()=="O")
                &&!(btnThree.getText()=="O"&&btnSix.getText()=="O"&&btnNine.getText()=="O")
                &&!(btnFour.getText()=="O"&&btnFive.getText()=="O"&&btnSix.getText()=="O")
                &&!(btnThree.getText()=="O"&&btnFive.getText()=="O"&&btnSeven.getText()=="O")
                &&!(btnSeven.getText()=="O"&&btnEight.getText()=="O"&&btnNine.getText()=="O")
                &&!("".equals(btnOne.getText().toString()))
                &&!("".equals(btnTwo.getText().toString()))
                &&!("".equals(btnThree.getText().toString()))
                &&!("".equals(btnFour.getText().toString()))
                &&!("".equals(btnFive.getText().toString()))
                &&!("".equals(btnSix.getText().toString()))
                &&!("".equals(btnSeven.getText().toString()))
                &&!("".equals(btnEight.getText().toString()))
                &&!("".equals(btnNine.getText().toString()))
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

        }

        else if((btnOne.getText()=="X"&&btnTwo.getText()=="X"&&btnThree.getText()=="X")
                ||(btnOne.getText()=="X"&&btnFive.getText()=="X"&&btnNine.getText()=="X")
                ||(btnOne.getText()=="X"&&btnFour.getText()=="X"&&btnSeven.getText()=="X")
                ||(btnTwo.getText()=="X"&&btnFive.getText()=="X"&&btnEight.getText()=="X")
                ||(btnThree.getText()=="X"&&btnSix.getText()=="X"&&btnNine.getText()=="X")
                ||(btnFour.getText()=="X"&&btnFive.getText()=="X"&&btnSix.getText()=="X")
                ||(btnThree.getText()=="X"&&btnFive.getText()=="X"&&btnSeven.getText()=="X")
                ||(btnSeven.getText()=="X"&&btnEight.getText()=="X"&&btnNine.getText()=="X")
                ){
            new AlertDialog.Builder(this)
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

        }

    }

    public void Computer(Button[] array){
        //if player has any two-in-a-rows,computer try to "block" the player
        if ((btnOne.getText()=="O"&&btnTwo.getText()=="O"&&btnThree.getText()=="")
                ||(btnThree.getText()==""&&btnSix.getText()=="O"&&btnNine.getText()=="O")
                ||(btnThree.getText()==""&&btnFive.getText()=="O"&&btnSeven.getText()=="O")
                ) {
            btnThree.setText("X");

        }
        else if((btnOne.getText()==""&&btnTwo.getText()=="O"&&btnThree.getText()=="O")
                ||(btnOne.getText()==""&&btnFour.getText()=="O"&&btnSeven.getText()=="O")
                ||(btnOne.getText()==""&&btnFive.getText()=="O"&&btnNine.getText()=="O")
                ) {
            btnOne.setText("X");

        }
        else if((btnOne.getText()=="O"&&btnTwo.getText()==""&&btnThree.getText()=="O")
                ||(btnTwo.getText()==""&&btnFive.getText()=="O"&&btnEight.getText()=="O")
                ) {
            btnTwo.setText("X");
        }
        else if((btnOne.getText()=="O"&&btnFour.getText()==""&&btnSeven.getText()=="O")
                ||(btnFour.getText()==""&&btnFive.getText()=="O"&&btnSix.getText()=="O")
                ) {
            btnFour.setText("X");
        }
        else if((btnOne.getText()=="O"&&btnFive.getText()==""&&btnNine.getText()=="O")
                ||(btnTwo.getText()=="O"&&btnFive.getText()==""&&btnEight.getText()=="O")
                ||(btnThree.getText()=="O"&&btnFive.getText()==""&&btnSeven.getText()=="O")
                ||(btnFour.getText()=="O"&&btnFive.getText()==""&&btnSix.getText()=="O")
                ) {
            btnFive.setText("X");
        }
        else if((btnThree.getText()=="O"&&btnSix.getText()==""&&btnNine.getText()=="O")
                ||(btnFour.getText()=="O"&&btnFive.getText()=="O"&&btnSix.getText()=="")
                ) {
            btnSix.setText("X");
        }
        else if((btnOne.getText()=="O"&&btnFour.getText()=="O"&&btnSeven.getText()=="")
                ||(btnThree.getText()=="O"&&btnFive.getText()=="O"&&btnSeven.getText()=="")
                ||(btnSeven.getText()==""&&btnEight.getText()=="O"&&btnNine.getText()=="O")
                ) {
            btnSeven.setText("X");

        }
        else if((btnTwo.getText()=="O"&&btnFive.getText()=="O"&&btnEight.getText()=="")
                ||(btnSeven.getText()=="O"&&btnEight.getText()==""&&btnNine.getText()=="O")
                ) {
            btnEight.setText("X");
        }
        else if((btnOne.getText()=="O"&&btnFive.getText()=="O"&&btnNine.getText()=="")
                ||(btnThree.getText()=="O"&&btnSix.getText()=="O"&&btnNine.getText()=="")
                ||(btnSeven.getText()=="O"&&btnEight.getText()=="O"&&btnNine.getText()=="")
                ) {
            btnNine.setText("X");

        }
        else {
            //no two-in-a-rows, computer random
            Random rd = new Random();
            Button idx = array[rd.nextInt(array.length)];
            if(btnOne.getText()==""||btnTwo.getText()==""||btnThree.getText()==""
                    ||btnFour.getText()==""||btnFive.getText()==""||btnSix.getText()==""
                    ||btnSeven.getText()==""||btnEight.getText()==""||btnNine.getText()==""
                    ) {
                while (!("".equals(idx.getText()))) {
                    idx = array[rd.nextInt(array.length)];
                }
                idx.setText("X");
            }
        }
    }
    public void clickBtnRestart(View v){
        reset();
    };

    private void reset(){
        btnOne.setText("");
        btnTwo.setText("");
        btnThree.setText("");
        btnFour.setText("");
        btnFive.setText("");
        btnSix.setText("");
        btnSeven.setText("");
        btnEight.setText("");
        btnNine.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tttapwc);
        initToolbar(getResources().getString(R.string.Game_Title_Tictactoe),new Intent(this, TTTAActivity.class), this);

        btnOne = (Button) findViewById(R.id.one);
        btnTwo = (Button) findViewById(R.id.two);
        btnThree = (Button) findViewById(R.id.three);
        btnFour = (Button) findViewById(R.id.four);
        btnFive = (Button) findViewById(R.id.five);
        btnSix = (Button) findViewById(R.id.six);
        btnSeven = (Button) findViewById(R.id.seven);
        btnEight = (Button) findViewById(R.id.eight);
        btnNine = (Button) findViewById(R.id.nine);

        reset();
    }


}
