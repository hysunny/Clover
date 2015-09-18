package com.clover.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.clover.R;

import java.util.Random;

public class TSwapActivity extends BaseActivity implements
        GestureDetector.OnGestureListener {
    //String[] process = {"搭讪","暧昧","约会","表白","恋爱","牵手","拥抱","接吻","求婚","见家长","结婚","蜜月","铜婚","银婚","金婚",""};
    String[] process;
    Button[] bns = new Button[16];
    Button freshData = null;
    boolean result = false;
    GestureDetector detector = null;
    final int flipLength = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tswap);
        final Random rand = new Random();
        initToolbar(getResources().getString(R.string.Game_Title_Tswap),new Intent(this, MainActivity.class), this);
        process = getResources().getStringArray(R.array.process);
        detector = new GestureDetector(TSwapActivity.this, this);
        for (int i = 0; i < 15; i++) {
            int j = rand.nextInt(15);
            String temp = process[i];
            process[i] = process[j];
            process[j] = temp;
        }

        bns[0] = (Button) findViewById(R.id.bn0);
        bns[1] = (Button) findViewById(R.id.bn1);
        bns[2] = (Button) findViewById(R.id.bn2);
        bns[3] = (Button) findViewById(R.id.bn3);
        bns[4] = (Button) findViewById(R.id.bn4);
        bns[5] = (Button) findViewById(R.id.bn5);
        bns[6] = (Button) findViewById(R.id.bn6);
        bns[7] = (Button) findViewById(R.id.bn7);
        bns[8] = (Button) findViewById(R.id.bn8);
        bns[9] = (Button) findViewById(R.id.bn9);
        bns[10] = (Button) findViewById(R.id.bn10);
        bns[11] = (Button) findViewById(R.id.bn11);
        bns[12] = (Button) findViewById(R.id.bn12);
        bns[13] = (Button) findViewById(R.id.bn13);
        bns[14] = (Button) findViewById(R.id.bn14);
        bns[15] = (Button) findViewById(R.id.bn15);
        freshData = (Button) findViewById(R.id.refresh);

        for (int i = 0; i < 15; i++)
            bns[i].setText(String.valueOf(process[i]));

        bns[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                action(0, 1, 4);
                check();
            }
        });

        bns[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(1, 0, 2, 5);
                check();
            }
        });

        bns[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(2, 1, 3, 6);
                check();
            }
        });

        bns[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(3, 2, 7);
                check();
            }
        });

        bns[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(4, 0, 5, 8);
                check();
            }
        });

        bns[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(5, 1, 4, 6, 9);
                check();
            }
        });

        bns[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(6, 2, 5, 7, 10);
                check();
            }
        });

        bns[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(7, 3, 6, 11);
                check();
            }
        });

        bns[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(8, 4, 9, 12);
                check();
            }
        });

        bns[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(9, 5, 8, 10, 13);
                check();
            }
        });

        bns[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(10, 6, 9, 11, 14);
                check();
            }
        });

        bns[11].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(11, 7, 10, 15);
                check();
            }
        });

        bns[12].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(12, 8, 13);
                check();
            }
        });

        bns[13].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(13, 9, 12, 14);
                check();
            }
        });

        bns[14].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(14, 10, 13, 15);
                check();
            }
        });

        bns[15].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(15, 11, 14);
                check();
            }
        });

        freshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 16; i++) {
                    if ("".equals(process[i]))
                    {  bns[i].setBackgroundResource(R.drawable.photo204);
                        swap(i,15);}
                    process[i] = process[i];
                }
                for (int i = 0; i < 15; i++) {
                    int j = rand.nextInt(15);
                    String temp = process[i];
                    process[i] = process[j];
                    process[j] = temp;
                }
                for (int i = 0; i < 15; i++)
                    bns[i].setText(String.valueOf(process[i]));

                bns[15].setBackgroundResource(R.drawable.photo204);

            }
        });



    }

    // 向上滑动
    boolean upCase(int where) {

        if (where == 0 || where == 1 || where == 2 || where == 3)
            return false;
        else if ("".equals(process[where - 4])) {
            swap(where, where - 4);
            return true;
        } else
            return false;
    }

    // 向下滑动
    boolean downCase(int where) {
        if (where == 12 || where == 13 || where == 14 || where == 15)
            return false;
        else if ("".equals(process[where + 4])) {
            swap(where, where + 4);
            return true;
        } else
            return false;
    }

    // 向左滑动
    boolean leftCase(int where) {
        if (where % 4 == 0)
            return false;
        else if ("".equals(process[where - 1])) {
            swap(where, where - 1);
            return true;
        } else
            return false;
    }

    // 向右滑动
    boolean rightCase(int where) {
        if (where % 4 == 3)
            return false;
        else if ("".equals(process[where + 1])) {
            swap(where, where + 1);
            return true;
        } else
            return false;
    }

    private void swap(int a, int b) {
        bns[a].setText("");
        bns[a].setBackgroundResource(R.drawable.photo204);
        bns[b].setText(String.valueOf(process[a]));
        bns[b].setBackgroundResource(R.drawable.photo204);
        String temp = process[a];
        process[a] = process[b];
        process[b] = temp;
    }

    private void action(int a0, int a1, int a2) {
        if (!("".equals(process[a1])) && !("".equals(process[a2])))
            return;
        else if ("".equals(process[a1]))
            swap(a0, a1);
        else if ("".equals(process[a2]))
            swap(a0, a2);
    }

    private void action(int a0, int a1, int a2, int a3) {
        if (!("".equals(process[a1])) && !("".equals(process[a2])) && !("".equals(process[a3])))
            return;
        else if ("".equals(process[a1]))
            swap(a0, a1);
        else if ("".equals(process[a2]))
            swap(a0, a2);
        else if ("".equals(process[a3]))
            swap(a0, a3);
    }

    private void action(int a0, int a1, int a2, int a3, int a4) {
        if (!("".equals(process[a1])) && !("".equals(process[a2])) && !("".equals(process[a3])) && !("".equals(process[a4])))
            return;
        else if ("".equals(process[a1]))
            swap(a0, a1);
        else if ("".equals(process[a2]))
            swap(a0, a2);
        else if ("".equals(process[a3]))
            swap(a0, a3);
        else if ("".equals(process[a4]))
            swap(a0, a4);
    }

    private void check() {
        result = ((getResources().getText(R.string.Game_item1).equals(process[0])) && (getResources().getText(R.string.Game_item2).equals(process[1])) &&(getResources().getText(R.string.Game_item3).equals(process[2]))&& (getResources().getText(R.string.Game_item4).equals(process[3])));
        if (result)
            result = (result && (getResources().getText(R.string.Game_item5).equals(process[4])) && (getResources().getText(R.string.Game_item6).equals(process[5]))
                    && (getResources().getText(R.string.Game_item7).equals(process[6])) && (getResources().getText(R.string.Game_item8).equals(process[7])));
        else
            return;
        if (result)
            result = (result && (getResources().getText(R.string.Game_item9).equals(process[8])) && (getResources().getText(R.string.Game_item10).equals(process[9]))
                    && (getResources().getText(R.string.Game_item11).equals(process[10])) && (getResources().getText(R.string.Game_item12).equals(process[11])));
        else
            return;
        if (result)
            result = (result && (getResources().getText(R.string.Game_item13).equals(process[12])) && (getResources().getText(R.string.Game_item14).equals(process[13])) && (getResources().getText(R.string.Game_item15).equals(process[14])));
        if (result) {
            new AlertDialog.Builder(TSwapActivity.this).setTitle(R.string.TZFEActivity_Game_ResultTitle3)
                    .setMessage("You Win！！！\n").setPositiveButton(R.string.TSwapActivity_Game_AlertSure, null)
                    .show();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent arg1) {

        boolean state = detector.onTouchEvent(arg1);
        if (state)
            return true;
        else
            return super.dispatchTouchEvent(arg1);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO 自动生成的方法存根
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // 滑动响应
        if (e1.getX() - e2.getX() > flipLength) {
            for (int i = 0; i < 16; i++) {
                if (leftCase(i))
                    return true;
            }
        } else if (e2.getX() - e1.getX() > flipLength) {
            for (int i = 0; i < 16; i++) {
                if (rightCase(i))
                    return true;
            }
        } else if (e1.getY() - e2.getY() > flipLength - 25) {
            for (int i = 0; i < 16; i++) {
                if (upCase(i))
                    return true;
            }
        } else if (e2.getY() - e1.getY() > flipLength - 25) {
            for (int i = 0; i < 16; i++) {
                if (downCase(i))
                    return true;
            }
        } else
            return false;
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
        // TODO 自动生成的方法存根

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO 自动生成的方法存根
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO 自动生成的方法存根

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
