package com.clover.ui.frgms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.clover.R;
import com.clover.ui.TSwapActivity;
import com.clover.ui.TTTAActivity;
import com.clover.ui.TZFEActivity;


public class GamePage extends Fragment implements OnClickListener{

    private Activity activity;
    private Button btn_ticTacToe;
    private Button btn_my2048;
    private Button btn_swapGame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_page,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        activity = getActivity();
        btn_ticTacToe = (Button) view.findViewById(R.id.ticTacToe);
        btn_my2048 = (Button) view.findViewById(R.id.my2048);
        btn_swapGame = (Button) view.findViewById(R.id.swapGame);


        btn_ticTacToe.setOnClickListener(this);
        btn_my2048.setOnClickListener(this);
        btn_swapGame.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.ticTacToe:
                intent = new Intent(activity,TTTAActivity.class);
                startActivity(intent);
                break;
            case R.id.my2048:
                intent = new Intent(activity,TZFEActivity.class);
                startActivity(intent);
                break;
            case R.id.swapGame:
                intent = new Intent(activity,TSwapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
