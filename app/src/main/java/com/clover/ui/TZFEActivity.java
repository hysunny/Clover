package com.clover.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.clover.R;
import com.clover.utils.MyDatabase;

import java.util.Random;

public class TZFEActivity extends BaseActivity implements
        GestureDetector.OnGestureListener {

    final int[] backgrounds = new int[] { R.drawable.photo0, R.drawable.photo2,
            R.drawable.photo4, R.drawable.photo8, R.drawable.photo16,
            R.drawable.photo32, R.drawable.photo64, R.drawable.photo128,
            R.drawable.photo256, R.drawable.photo512, R.drawable.photo1024,
            R.drawable.photo2048};
    final int[][] cardsId = new int[][] {
            { R.id.tv_card00, R.id.tv_card01, R.id.tv_card02, R.id.tv_card03 },
            { R.id.tv_card10, R.id.tv_card11, R.id.tv_card12, R.id.tv_card13 },
            { R.id.tv_card20, R.id.tv_card21, R.id.tv_card22, R.id.tv_card23 },
            { R.id.tv_card30, R.id.tv_card31, R.id.tv_card32, R.id.tv_card33 } };
    private int[][] matrix = new int[4][4];
    String scoreArray[];
    String  BestScore = "";
    int score = 0;
    int bestScore = 0;
    Button newGame = null;
    TextView scoreText = null;
    TextView bestScoreText = null;
    TextView card = null;
    //捕捉手势
    GestureDetector detector = null;
    //动画
    private Animation anim_enter;
    private Animation anim_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialization();
        setOnListener();
        gameStart();
    }


    // 初始化操作
    private void initialization() {

        setContentView(R.layout.activity_tzfe);
        initToolbar(getResources().getString(R.string.Game_Title_my2048),new Intent(this, MainActivity.class), this);
        scoreArray = getResources().getStringArray(R.array.scoreArray);
        // 初始化矩阵
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                matrix[i][j] = 0;

        // 初始化重新开始按钮， 成绩和最好成绩TextView， 手势监听器detector
        newGame = (Button) findViewById(R.id.newgame);
        scoreText = (TextView) findViewById(R.id.scoretext);
        bestScoreText = (TextView) findViewById(R.id.bestscoretext);
        detector = new GestureDetector(TZFEActivity.this, this);

        // 使用数据库读取最好成绩
        MyDatabase helper = new MyDatabase(TZFEActivity.this, "myrecord", null,
                1);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 查询数据库中数据
        Cursor cursor = db.rawQuery("select * from myrecord", null);
        if (cursor.moveToNext()) {
            // 数据存在时读出数据并显示最好成绩
            // BestScore = cursor.getString(cursor.getColumnIndex("bestscore"));
            bestScoreText.setText(cursor.getString(cursor.getColumnIndex("bestscore")));
        } else {
            // 数据不存在时创建数据并显示数据
            String sql = "insert into myrecord(bestscore) values('搭讪')";
            db.execSQL(sql);
            bestScoreText.setText(R.string.TZFEActivity_Game_InitScore);
        }
        db.close();

        // 初始化成绩
        scoreText.setText(R.string.TZFEActivity_Game_InitScore);

        anim_enter = AnimationUtils.loadAnimation(this, R.anim.scaleanim);
        anim_exit = AnimationUtils.loadAnimation(this, R.anim.scaleanim);
    }

    // 左滑事件处理
    private void leftShift() {
        // temp[4][4]数组来记录把原来matrix[4][4]数组中所有数推向左边后的结果
        int[][] temp = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        int nums = 0;
        // Equal zero numbers等于0的数的总数，为后面随机产生一张卡片做准备
        int ezn = 16;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (matrix[i][j] != 0) {
                    temp[i][nums] = matrix[i][j];
                    ++nums;
                }
            }
            nums = 0;
        }

        // 对temp数组执行必要的相加的逻辑
        for (int i = 0; i < 4; ++i) {
            if ((temp[i][0] != 0) && (temp[i][0] == temp[i][1])) {
                temp[i][0] = temp[i][0] << 1;
                temp[i][1] = 0;
                if ((temp[i][2] != 0) && (temp[i][2] == temp[i][3])) {
                    temp[i][2] = temp[i][2] << 1;
                    temp[i][3] = 0;
                }
            } else {
                if ((temp[i][1] != 0) && (temp[i][1] == temp[i][2])) {
                    temp[i][1] = temp[i][1] << 1;
                    temp[i][2] = 0;
                } else {
                    if ((temp[i][2] != 0) && (temp[i][2] == temp[i][3])) {
                        temp[i][2] = temp[i][2] << 1;
                        temp[i][3] = 0;
                    }
                }
            }
        }

        // 将matrix数组初始化
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                matrix[i][j] = 0;

        // 把temp数组重新赋值给matrix数组，并且把temp数组全部元素压到最左边
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (temp[i][j] != 0) {
                    matrix[i][nums] = temp[i][j];
                    ++nums;
                    --ezn;
                }
            }
            nums = 0;
        }

        if(ezn > 0){
            // 随机增加一张卡片
            Random rand = new Random();
            int where = rand.nextInt(ezn) + 1;
            for (int count = 0, i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j) {
                    if (matrix[i][j] == 0)
                        count++;
                    if (count == where) {
                        matrix[i][j] = getCard();
                        card = (TextView) findViewById(cardsId[i][j]);
                        card.startAnimation(anim_enter);
                        return;

                    }
                }
        }
    }

    // 右滑事件处理
    private void rightShift() {

        // temp[4][4]数组来记录把原来matrix[4][4]数组中所有数推向右边后的结果
        int[][] temp = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        int nums = 3;

        // Equal zero numbers等于0的数的总数，为后面随机产生一张卡片做准备
        int ezn = 16;
        for (int i = 0; i < 4; ++i) {
            for (int j = 3; j >= 0; --j) {
                if (matrix[i][j] != 0) {
                    temp[i][nums] = matrix[i][j];
                    --nums;
                }
            }
            nums = 3;
        }

        // 对temp数组执行必要的相加的逻辑
        for (int i = 0; i < 4; ++i) {
            if ((temp[i][3] != 0) && (temp[i][3] == temp[i][2])) {
                temp[i][3] = temp[i][3] << 1;
                temp[i][2] = 0;
                if ((temp[i][1] != 0) && (temp[i][1] == temp[i][0])) {
                    temp[i][1] = temp[i][1] << 1;
                    temp[i][0] = 0;
                }
            } else {
                if ((temp[i][2] != 0) && (temp[i][2] == temp[i][1])) {
                    temp[i][2] = temp[i][2] << 1;
                    temp[i][1] = 0;
                } else {
                    if ((temp[i][1] != 0) && (temp[i][1] == temp[i][0])) {
                        temp[i][1] = temp[i][1] << 1;
                        temp[i][0] = 0;
                    }
                }
            }
        }

        // 将matrix数组初始化
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                matrix[i][j] = 0;

        // 把temp数组重新赋值给matrix数组，并且把temp数组全部元素压到最左边
        for (int i = 0; i < 4; ++i) {
            for (int j = 3; j >= 0; --j) {
                if (temp[i][j] != 0) {
                    matrix[i][nums] = temp[i][j];
                    --nums;
                    --ezn;
                }
            }
            nums = 3;
        }

        if(ezn > 0){
            // 随机增加一张卡片
            Random rand = new Random();
            int where = rand.nextInt(ezn) + 1;
            for (int count = 0, i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j) {
                    if (matrix[i][j] == 0)
                        count++;
                    if (count == where) {
                        matrix[i][j] = getCard();
                        card = (TextView) findViewById(cardsId[i][j]);
                        card.startAnimation(anim_enter);
                        return;
                    }
                }
        }

    }

    // 上滑事件处理
    private void upShift() {
        // temp[4][4]数组来记录把原来matrix[4][4]数组中所有数推向上边后的结果
        int[][] temp = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        int nums = 0;
        // Equal zero numbers等于0的数的总数，为后面随机产生一张卡片做准备
        int ezn = 16;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (matrix[j][i] != 0) {
                    temp[nums][i] = matrix[j][i];
                    ++nums;
                }
            }
            nums = 0;
        }

        // 对temp数组执行必要的相加的逻辑
        for (int i = 0; i < 4; ++i) {
            if ((temp[0][i] != 0) && (temp[0][i] == temp[1][i])) {
                temp[0][i] = temp[0][i] << 1;
                temp[1][i] = 0;
                if ((temp[2][i] != 0) && (temp[2][i] == temp[3][i])) {
                    temp[2][i] = temp[2][i] << 1;
                    temp[3][i] = 0;
                }
            } else {
                if ((temp[1][i] != 0) && (temp[1][i] == temp[2][i])) {
                    temp[1][i] = temp[1][i] << 1;
                    temp[2][i] = 0;
                } else {
                    if ((temp[2][i] != 0) && (temp[2][i] == temp[3][i])) {
                        temp[2][i] = temp[2][i] << 1;
                        temp[3][i] = 0;
                    }
                }
            }
        }

        // 将matrix数组初始化
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                matrix[i][j] = 0;

        // 把temp数组重新赋值给matrix数组，并且把temp数组全部元素压到最上边
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (temp[j][i] != 0) {
                    matrix[nums][i] = temp[j][i];
                    ++nums;
                    --ezn;
                }
            }
            nums = 0;
        }

        if(ezn > 0){
            // 随机增加一张卡片
            Random rand = new Random();
            int where = rand.nextInt(ezn) + 1;
            for (int count = 0, i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j) {
                    if (matrix[i][j] == 0)
                        count++;
                    if (count == where) {
                        matrix[i][j] = getCard();
                        card = (TextView) findViewById(cardsId[i][j]);
                        card.startAnimation(anim_enter);
                        return;

                    }
                }
        }
    }

    // 下滑事件处理
    private void downShift() {

        // temp[4][4]数组来记录把原来matrix[4][4]数组中所有数推向下边后的结果
        int[][] temp = new int[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
        int nums = 3;
        // Equal zero numbers等于0的数的总数，为后面随机产生一张卡片做准备
        int ezn = 16;
        for (int i = 0; i < 4; ++i) {
            for (int j = 3; j >= 0; --j) {
                if (matrix[j][i] != 0) {
                    temp[nums][i] = matrix[j][i];
                    --nums;
                }
            }
            nums = 3;
        }

        // 对temp数组执行必要的相加的逻辑
        for (int i = 0; i < 4; ++i) {
            if ((temp[3][i] != 0) && (temp[3][i] == temp[2][i])) {
                temp[3][i] = temp[3][i] << 1;
                temp[2][i] = 0;
                if ((temp[1][i] != 0) && (temp[1][i] == temp[0][i])) {
                    temp[1][i] = temp[1][i] << 1;
                    temp[0][i] = 0;
                }
            } else {
                if ((temp[2][i] != 0) && (temp[2][i] == temp[1][i])) {
                    temp[2][i] = temp[2][i] << 1;
                    temp[1][i] = 0;
                } else {
                    if ((temp[1][i] != 0) && (temp[1][i] == temp[0][i])) {
                        temp[1][i] = temp[1][i] << 1;
                        temp[0][i] = 0;
                    }
                }
            }
        }

        // 将matrix数组初始化
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j)
                matrix[i][j] = 0;

        // 把temp数组重新赋值给matrix数组，并且把temp数组全部元素压到最下边
        for (int i = 0; i < 4; ++i) {
            for (int j = 3; j >= 0; --j) {
                if (temp[j][i] != 0) {
                    matrix[nums][i] = temp[j][i];
                    --nums;
                    --ezn;
                }
            }
            nums = 3;
        }

        if(ezn > 0){
            // 随机增加一张卡片
            Random rand = new Random();
            int where = rand.nextInt(ezn) + 1;
            for (int count = 0, i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j) {
                    if (matrix[i][j] == 0)
                        count++;
                    if (count == where) {
                        matrix[i][j] = getCard();
                        card = (TextView) findViewById(cardsId[i][j]);
                        card.startAnimation(anim_enter);
                        return;

                    }
                }
        }

    }

    // 为重新开始按钮设置监听
    private void setOnListener() {
        newGame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                for(int i = 0; i < 4; i++)
                    for(int j = 0; j < 4; j++)
                        matrix[i][j] = 0;
                showMatrix();
                score = 0;
                scoreText.setText(scoreArray[score]);
                gameStart();
            }
        });
    }

    // 设置成绩时调用的函数
    private void setScore(int[][] array) {
        int max = array[0][0];
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                if(array[i][j]>max){
                    max = array[i][j];
                }
            }
        Double xx = (Math.log(max) / Math.log(2));
        score = (new   Double(xx)).intValue() - 1 ;
        scoreText.setText(scoreArray[score]);
        for(int i=0;i<scoreArray.length;i++){
            if (scoreArray[i].equals(BestScore)){
                bestScore = i;
            }
        }

        // 当前分数大于最高分
        if (score > bestScore) {
            MyDatabase helper = new MyDatabase(TZFEActivity.this, "myrecord",
                    null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            // 更新数据库数据
            String sql = "update myrecord set bestscore = " + "'" + scoreArray[score] + "'";
            db.execSQL(sql);
            db.close();
            // 更改最好成绩
            bestScore = score;
            BestScore = scoreArray[score];
            bestScoreText.setText(BestScore);

        }
    }

    // 随机生成卡片，生成卡片2比卡片4的比例为4:1
    private int getCard() {
        Random rand = new Random();
        int num = rand.nextInt(5);
        if (num < 4)
            return 2;
        else
            return 4;
    }

    // 开始游戏,开始时只有两张卡片
    private void gameStart() {
        Random rand = new Random();
        int num = 0;
        while (num < 2) {
            int row = rand.nextInt(4);
            int column = rand.nextInt(4);
            if (matrix[row][column] == 0) {
                card = (TextView) findViewById(cardsId[row][column]);
                int cardNum = getCard();
                card.setBackgroundResource(backgrounds[cardNum / 2]);
                matrix[row][column] = cardNum;
                num++;
            }
        }
        showMatrix();
    }

    private void showMatrix() {
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                card = (TextView) findViewById(cardsId[i][j]);
                // 得到数字对应的背景图片
                if (matrix[i][j] == 0) {
                    card.setBackgroundResource(backgrounds[0]);
                } else {
                    int backgroundNum = (int) (Math.log(matrix[i][j]) / Math
                            .log(2));
                    card.setBackgroundResource(backgrounds[backgroundNum]);
                    if ( (i > 0 && (matrix[i][j] == matrix[i - 1][j])) || (j > 0 && (matrix[i][j] == matrix[i][j - 1])) ||
                            (i < 3 && matrix[i][j] == (matrix[i + 1][j])) || (j < 3 && (matrix[i][j] == matrix[i][j + 1]))) {
                    }
                }
            }
    }

    private void check() {
        boolean complete = true;
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                card = (TextView) findViewById(cardsId[i]
                        [j]);
                //相邻的可以消除并且有空的时候，游戏就没结束
                if (card.getText().equals(0) || matrix[i]
                        [j]==0 || (i > 0 && (matrix[i][j] == matrix[i - 1][j])) || (j
                        > 0 && (matrix[i][j] == matrix[i][j - 1])) ||
                        (i < 3 && matrix[i][j] == (matrix[i +
                                1][j])) || (j < 3 && (matrix[i][j] == matrix[i][j + 1]))) {
                    complete = false;
                }
            }
        if (complete){
            if(score == bestScore){
                LayoutInflater inflater =
                        LayoutInflater.from(TZFEActivity.this);
                View normalResultView = inflater.inflate
                        (R.layout.normalview, null);
                TextView normalScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalword)));
                TextView normalBestScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalresult)));
                normalScore.setText(getResources().getText
                        (R.string.TZFEActivity_Game_ResultContent1));
                normalBestScore.setText(BestScore);
                new AlertDialog.Builder(TZFEActivity.this)
                        .setTitle(R.string.TZFEActivity_Game_ResultTitle1)
                        .setView(normalResultView)
                        .setPositiveButton(R.string.TZFEActivity_Game_MakeSure, new
                                DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick
                                            (DialogInterface dialog, int which) {
                                        for (int i = 0; i < 4; i++)
                                            for (int j = 0; j < 4; j
                                                    ++)
                                                matrix[i][j] = 0;
                                        showMatrix();
                                        score = 0;
                                        scoreText.setText(scoreArray
                                                [score]);
                                        gameStart();
                                    }
                                })
                        .setNegativeButton(R.string.TZFEActivity_Game_Cancel, null)
                        .show();
            }
            else {
                LayoutInflater inflater =
                        LayoutInflater.from(TZFEActivity.this);
                View normalResultView = inflater.inflate
                        (R.layout.normalview, null);
                TextView normalScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalword)));
                TextView normalBestScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalresult)));
                normalScore.setText(R.string.TZFEActivity_Game_ResultContent2);
                normalBestScore.setText(scoreArray[score]);
                new AlertDialog.Builder(TZFEActivity.this)
                        .setTitle(R.string.TZFEActivity_Game_ResultTitle2)
                        .setView(normalResultView)
                        .setPositiveButton(R.string.TZFEActivity_Game_MakeSure, new
                                DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick
                                            (DialogInterface dialog, int which) {
                                        for (int i = 0; i < 4; i++)
                                            for (int j = 0; j < 4; j
                                                    ++)
                                                matrix[i][j] = 0;
                                        showMatrix();
                                        score = 0;
                                        scoreText.setText(scoreArray
                                                [score]);
                                        gameStart();
                                    }
                                })
                        .setNegativeButton(R.string.TZFEActivity_Game_Cancel, null)
                        .show();
            }
        }
        else{
            if (score == 10) {
                LayoutInflater inflater =
                        LayoutInflater.from(TZFEActivity.this);
                View normalResultView = inflater.inflate
                        (R.layout.normalview, null);
                TextView normalScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalword)));
                TextView normalBestScore =
                        ((TextView)
                                (normalResultView.findViewById(R.id.normalresult)));
                normalScore.setText(R.string.TZFEActivity_Game_ResultContent3);
                normalBestScore.setText(R.string.TZFEActivity_Game_BestScore);
                new AlertDialog.Builder(TZFEActivity.this)
                        .setTitle(R.string.TZFEActivity_Game_ResultTitle3)
                        .setView(normalResultView)
                        .setPositiveButton(R.string.TZFEActivity_Game_MakeSure, new
                                DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick
                                            (DialogInterface dialog, int which) {
                                        for (int i = 0; i < 4; i++)
                                            for (int j = 0; j < 4; j
                                                    ++)
                                                matrix[i][j] = 0;
                                        showMatrix();
                                        score = 0;
                                        scoreText.setText(scoreArray
                                                [score]);
                                        gameStart();
                                    }
                                })
                        .setNegativeButton(getResources
                                ().getText(R.string.TZFEActivity_Game_Cancel), null)
                        .show();
            }
        }
    }
    @Override
    public boolean onDown(MotionEvent e) {
        // TODO 自动生成的方法存根
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > 50) {
            leftShift();
            showMatrix();
            setScore(matrix);
            check();
        } else if (e2.getX() - e1.getX() > 50) {
            rightShift();
            showMatrix();
            setScore(matrix);
            check();
        } else if (e1.getY() - e2.getY() > 40) {
            upShift();
            showMatrix();
            setScore(matrix);
            check();
        } else if(e2.getY() - e1.getY() > 40){
            downShift();
            showMatrix();
            setScore(matrix);
            check();
        }
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
        // TODO 自动生成的方法存根
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {

        boolean state = detector.onTouchEvent(e);
        if (state)
            return true;
        else
            return super.dispatchTouchEvent(e);
    }

}
