package com.example.mywork06_surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;


//--------------------------------
//  MySurface Class
//--------------------------------
public class MySurface extends SurfaceView implements SurfaceHolder.Callback{
    //SurfaceView

    MyThread mThread; // 쓰레드 변수 생성
    SurfaceHolder mHolder;  // SurfaceHolder 변수 생성
    Context mContext;   // Context 변수 생성

    int Width, Height;  // 화면 사이즈
    Bitmap basket;  // 바구니 bitmap
    int basket_x, basket_y; // 바구니 위치
    int basketWidth, basketHeight;    // 바구니 사이즈
    int button_width;   // 버튼 사이즈
    Bitmap leftKey, rightKey;  // 좌우 버튼 bitmap
    int leftKey_x, leftKey_y;   // 왼쪽 버튼 위치
    int rightKey_x, rightKey_y;   // 오른쪽 버튼 위치
    Bitmap balloonImg;  // 풍선 bitmap
    int balloonWidth, balloonHeight;   // 풍선 사이즈
    ArrayList<Balloon> balloon;    // 풍선 ArrayList
    int balloonSpeed;   // 풍선 속도
    AnswerBalloon answerBalloon;    // 정답풍선
    int score;  // 점수
    int basketSpeed;    // 바구니 속도
    int num1, num2; // 문제 숫자
    int answer; // 정답
    int wrongNum[] = new int[5];    // 오답
    SoundPool sPool;    // SoundPool 클래스 객체
    int correct, wrong;     // 효과음
    MediaPlayer backgroundMusic;    // MediaPlayer 클래스 객체
    int bgm;    // bgm
    int count = 3000;  // 시간 카운트3
    int timeValue = 1;  // 시간 난이도 -> 0:30초 1:1분 2:2분
    Bitmap menuButton;    // 메뉴버튼 bitmap
    int menuButton_x, menuButton_y; // 메뉴버튼 위치
    int menuOk = 1;     // 메뉴 상태 -> 0:게임화면, 1:메뉴, 2:도움말. 3:결과화면
    Bitmap plusIcon, minusIcon, multiIcon;  // 연산선택 Bitmap
    int plusIcon_x, plusIcon_y; // 더하기 아이콘 위치
    int minusIcon_x, minusIcon_y;   // 빼기 아이콘 위치
    int multiIcon_x, multiIcon_y;   // 곱하기 아이콘 위치
    Bitmap level1, level2, level3;  // 난이도 Bitmap
    int level1_x, level1_y; // 난이도 1 위치
    int level2_x, level2_y; // "    2 위치
    int level3_x, level3_y; //  "   3 위치
    int level = 1;  // 난이도 -> 0:쉬움 1:중간 2:어려움
    Bitmap timeIcon;    // 시간선택 Bitmap
    int time30_x, time30_y;     // 30초 위치
    int time60_x, time60_y;     // 1분 위치
    int time120_x, time120_y;    // 2분 위치
    Bitmap apply;           // 적용버튼 Bitmap
    int apply_x, apply_y;   // 적용버튼 위치
    Bitmap close;           // 닫기버튼 Bitmap
    int close_x, close_y;   // 닫기버튼 위치
    int operater;   // 문제 유형 -> 0:더하기, 1:빼기, 2:곱하기
    Bitmap help;    // 도움말버튼 Bitmap
    int help_x, help_y; // 도움말버튼 위치
    Bitmap gameOver;    // 게임오버 Bitmap
    int gameOver_x, gameOver_y; // 게임오버 아이콘 위치
    int oNumber, xNumber;   // 틀린개수, 맞은개수


    // 앱 시작시 가동되는 메소드 -> onCreate 대체
    public void initApp(){
        // [디스플레이 사이즈 참조]
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        Width = metrics.widthPixels;
        Height = metrics.heightPixels;

        // 버튼
        button_width = Width/6;

        // 메뉴버튼
        menuButton = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
        menuButton = Bitmap.createScaledBitmap(menuButton, button_width, button_width, true);
        menuButton_x = Width-button_width-button_width*1/5;
        menuButton_y = Height/30;

        // 점수 초기화
        score = 0;

        // [Bitmap 이미지 설정]
        // 바구니
        basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        int x = Width/4;
        int y = Height/14;
        basket = Bitmap.createScaledBitmap(basket, x, y, true);
        basketWidth = basket.getWidth();
        basketHeight = basket.getHeight();
        basket_x = Width*1/9;
        basket_y = Height*5/9;
        basketSpeed = Width/40;

        // 좌우 버튼
        leftKey = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        leftKey_x = button_width/2;
        leftKey_y = Height-button_width*3;
        leftKey = Bitmap.createScaledBitmap(leftKey, button_width, button_width, true);
        rightKey = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        rightKey_x = Width-button_width-button_width/2;
        rightKey_y = Height-button_width*3;
        rightKey = Bitmap.createScaledBitmap(rightKey, button_width, button_width, true);

        // 풍선
        balloon = new ArrayList<Balloon>();
        balloonSpeed = Width/140;
        Random r1 = new Random();
        int xx = r1.nextInt(Width-button_width);
        answerBalloon = new AnswerBalloon(xx, 0, balloonSpeed);
        balloonImg = BitmapFactory.decodeResource(getResources(), R.drawable.balloon);
        balloonImg = Bitmap.createScaledBitmap(balloonImg, button_width, button_width*3/4, true);
        balloonWidth = balloonImg.getWidth();
        balloonHeight = balloonImg.getHeight();

        // 효과음
        sPool = new SoundPool.Builder().build();    // SoundPool 객체 빌드
        correct = sPool.load(this.mContext, R.raw.correct, 1);  // 정답 효과음 등록
        wrong = sPool.load(this.mContext, R.raw.wrong, 1);  // 오답 효과음 등록

        // 연산 아이콘
        plusIcon = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
        plusIcon = Bitmap.createScaledBitmap(plusIcon, button_width, button_width, true);
        plusIcon_x = button_width;
        plusIcon_y = Height/5;
        minusIcon = BitmapFactory.decodeResource(getResources(), R.drawable.minus);
        minusIcon = Bitmap.createScaledBitmap(minusIcon, button_width, button_width, true);
        minusIcon_x = button_width*2+button_width/4;
        minusIcon_y = Height/5;
        multiIcon = BitmapFactory.decodeResource(getResources(), R.drawable.multi);
        multiIcon = Bitmap.createScaledBitmap(multiIcon, button_width, button_width, true);
        multiIcon_x = button_width*3+button_width/2;
        multiIcon_y = Height/5;

        // 난이도 아이콘
        level1 = BitmapFactory.decodeResource(getResources(), R.drawable.level1);
        level1 = Bitmap.createScaledBitmap(level1, button_width, button_width, true);
        level1_x = plusIcon_x;
        level1_y = plusIcon_y+button_width*2;
        level2 = BitmapFactory.decodeResource(getResources(), R.drawable.level2);
        level2 = Bitmap.createScaledBitmap(level2, button_width, button_width, true);
        level2_x = minusIcon_x;
        level2_y = plusIcon_y+button_width*2;
        level3 = BitmapFactory.decodeResource(getResources(), R.drawable.level3);
        level3 = Bitmap.createScaledBitmap(level3, button_width, button_width, true);
        level3_x = multiIcon_x;
        level3_y = plusIcon_y+button_width*2;

        // 시간 아이콘
        timeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.square);
        timeIcon = Bitmap.createScaledBitmap(timeIcon, button_width, button_width, true);
        time30_x = plusIcon_x;
        time30_y = plusIcon_y+button_width*4;
        time60_x = minusIcon_x;
        time60_y = plusIcon_y+button_width*4;
        time120_x = multiIcon_x;
        time120_y = plusIcon_y+button_width*4;

        // 적용버튼
        apply = BitmapFactory.decodeResource(getResources(), R.drawable.apply);
        apply = Bitmap.createScaledBitmap(apply, button_width*2/3, button_width*2/3, true);
        apply_x = Width/5 - button_width;
        apply_y = plusIcon_y + button_width*5 + button_width/8;

        // 닫기버튼
        close = BitmapFactory.decodeResource(getResources(), R.drawable.close);
        close = Bitmap.createScaledBitmap(close, button_width*2/3, button_width*2/3, true);
        close_x = Width*2/3+button_width;
        close_y = plusIcon_y+button_width*5+button_width/8;

        // 도움말버튼
        help = BitmapFactory.decodeResource(getResources(), R.drawable.help);
        help = Bitmap.createScaledBitmap(help, button_width*2/3, button_width*2/3, true);
        help_x = Width*8/10;
        help_y = Height/10;

        // 게임오버 아이콘
        gameOver = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
        gameOver = Bitmap.createScaledBitmap(gameOver, Width*2/3, Height*1/3, true);
        gameOver_x = button_width;
        gameOver_y = button_width/2;
    }

    // 생성자
    public MySurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder(); // SurfaceHolder 클래스 참조
        holder.addCallback(this);   // 콜백 인터페이스 설정 : MySurface

        mThread = new MyThread(holder, context);    // 쓰레드 생성
        initApp();  // 앱 시작
    }



    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        mThread.start();    // 스레드 가동
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    //--------------------------------
    //  MyThread Class
    //--------------------------------
    class MyThread extends Thread { // 스레드

        // 생성자
        public MyThread(SurfaceHolder holder, Context context){
            mHolder = holder;   // mHolder 변수에 SurfaceHolder 등록
            mContext = context; // mContext 변수에 context 등록
            makeQuestion();     // 문제 생성
        }

        //--------------------------------
        //  drawEverything
        //--------------------------------
        public void drawEverything(Canvas canvas){  // 그리기 메소드
            // 오답풍선 5개 미만이면 ArrayList에 풍선 추가
            if(balloon.size() < 4){
                // 랜덤 위치에 생성
                Random r1 = new Random();
                int x = r1.nextInt(Width-button_width);
                int y = r1.nextInt(Height/4);
                balloon.add(new Balloon(x, -y, balloonSpeed));
            }

            // Paint 설정
            Paint p1 = new Paint(); // 풍선안에 들어가는 흰 글씨
            Paint p2 = new Paint(); // 투명도 있음
            Paint p3 = new Paint(); // 문제용 파란 글씨
            Paint p4 = new Paint(); // 검은 글씨
            Paint p5 = new Paint(); // 메뉴 타이틀
            Paint pp = new Paint(); // 배경

            p1.setColor(Color.WHITE);
            p1.setTextSize(Width/20);

            p2.setColor(Color.WHITE);
            p2.setTextSize(Width/20);
            p2.setAlpha(100);

            p3.setColor(Color.BLUE);
            p3.setTextSize(Width/12);

            p4.setColor(Color.BLACK);
            p4.setTextSize(Width/14);

            p5.setColor(Color.BLACK);
            p5.setTextSize(Width/10);

            pp.setColor(0xFFFFD9EC);

            // canvas에 그리기
            // 배경
            canvas.drawRect(0,0,Width, Height, pp);

            // 게임화면
            if(menuOk == 0) {
                canvas.drawText("남은 시간 : " + Integer.toString(count/50), 0, Height / 7, p4);
                canvas.drawText("점수 : " + Integer.toString(score), 0, Height / 5, p4);
                // 문제
                switch (operater){
                    case 0:
                        canvas.drawText("문제 : " + Integer.toString(num1) + " + " + Integer.toString(num2) + " =",
                                0, Height / 13, p3);
                        break;
                    case 1:
                        if(num1 > num2)
                            canvas.drawText("문제 : " + Integer.toString(num1) + " - " + Integer.toString(num2) + " =",
                                    0, Height / 13, p3);
                        else   canvas.drawText("문제 : " + Integer.toString(num2) + " - " + Integer.toString(num1) + " =",
                                0, Height / 13, p3);
                        break;
                    case 2:
                        canvas.drawText("문제 : " + Integer.toString(num1) + " * " + Integer.toString(num2) + " =",
                                0, Height / 13, p3);
                        break;
                }
                canvas.drawBitmap(basket, basket_x, basket_y, p1);
                canvas.drawBitmap(rightKey, rightKey_x, rightKey_y, p1);

                canvas.drawBitmap(leftKey, leftKey_x, leftKey_y, p1);
                canvas.drawBitmap(menuButton, menuButton_x, menuButton_y, p1);

                // 오답 풍선 그리기
                for (Balloon tmp : balloon) {
                    canvas.drawBitmap(balloonImg, tmp.x, tmp.y, p1);
                }

                int i = 0;
                // 오답 풍선에 오답 적기
                for (Balloon tmp : balloon) {
                    canvas.drawText(Integer.toString(wrongNum[i++]), tmp.x + button_width * 1 / 3, tmp.y + button_width * 1 / 2, p1);
                }

                // 정답 풍선 그리기
                canvas.drawBitmap(balloonImg, answerBalloon.x, answerBalloon.y, p1);

                // 정답 풍선에 정답 적기
                canvas.drawText(Integer.toString(answer), answerBalloon.x + button_width * 1 / 3,
                        answerBalloon.y + button_width * 1 / 2, p1);

                // 풍선 이동
                moveBalloon();

                // 충돌체크
                checkCollision();

                // 카운트다운
                if(count > 0) count--;
                else {  // 시간 초과시
                    count = 0;
                    // 결과화면으로 이동
                    menuOk = 3;
                }
            }

            // 메뉴화면
            if(menuOk == 1){
                // 타이틀
                canvas.drawText("바구니 수학게임", Width*1/12, Height/10, p5);

                // 문제유형 선택
                canvas.drawText("* 문제유형 선택", button_width, plusIcon_y-button_width/4, p4);
                // Alpha 처리
                // 더하기 선택시
                if(operater == 0){
                    canvas.drawBitmap(plusIcon, plusIcon_x, plusIcon_y, p1);
                    canvas.drawBitmap(minusIcon, minusIcon_x, minusIcon_y, p2);
                    canvas.drawBitmap(multiIcon, multiIcon_x, multiIcon_y, p2);
                }
                // 빼기 선택시
                if(operater == 1){
                    canvas.drawBitmap(plusIcon, plusIcon_x, plusIcon_y, p2);
                    canvas.drawBitmap(minusIcon, minusIcon_x, minusIcon_y, p1);
                    canvas.drawBitmap(multiIcon, multiIcon_x, multiIcon_y, p2);
                }
                // 곱하기 선택시
                if(operater == 2){
                    canvas.drawBitmap(plusIcon, plusIcon_x, plusIcon_y, p2);
                    canvas.drawBitmap(minusIcon, minusIcon_x, minusIcon_y, p2);
                    canvas.drawBitmap(multiIcon, multiIcon_x, multiIcon_y, p1);
                }

                // 난이도 선택
                canvas.drawText("난이도 선택", button_width, level1_y-button_width/4, p4);
                // Alpha 처리
                // 레벨 1 선택시
                if(level == 0) {
                    canvas.drawBitmap(level1, level1_x, level1_y, p1);
                    canvas.drawBitmap(level2, level2_x, level2_y, p2);
                    canvas.drawBitmap(level3, level3_x, level3_y, p2);
                }
                // 레벨 2 선택시
                if(level == 1){
                    canvas.drawBitmap(level1, level1_x, level1_y, p2);
                    canvas.drawBitmap(level2, level2_x, level2_y, p1);
                    canvas.drawBitmap(level3, level3_x, level3_y, p2);
                }
                // 레벨 3 선택시
                if(level == 2) {
                    canvas.drawBitmap(level1, level1_x, level1_y, p2);
                    canvas.drawBitmap(level2, level2_x, level2_y, p2);
                    canvas.drawBitmap(level3, level3_x, level3_y, p1);
                }

                // 시간 선택
                canvas.drawText("제한시간 선택", button_width, time30_y-button_width/4, p4);
                // Alpha 처리
                // 30초 선택시
                if(timeValue == 0){
                    canvas.drawBitmap(timeIcon, time30_x, time30_y, p1);
                    canvas.drawText("30초", time30_x+button_width*1/4, time30_y+button_width/2, p1);
                    canvas.drawBitmap(timeIcon, time60_x, time60_y, p2);
                    canvas.drawText("1분", time60_x+button_width*1/4, time60_y+button_width/2, p2);
                    canvas.drawBitmap(timeIcon, time120_x, time120_y, p2);
                    canvas.drawText("2분", time120_x+button_width*1/4, time120_y+button_width/2, p2);
                }
                // 1분 선택시
                if(timeValue == 1){
                    canvas.drawBitmap(timeIcon, time30_x, time30_y, p2);
                    canvas.drawText("30초", time30_x+button_width*1/4, time30_y+button_width/2, p2);
                    canvas.drawBitmap(timeIcon, time60_x, time60_y, p1);
                    canvas.drawText("1분", time60_x+button_width*1/4, time60_y+button_width/2, p1);
                    canvas.drawBitmap(timeIcon, time120_x, time120_y, p2);
                    canvas.drawText("2분", time120_x+button_width*1/4, time120_y+button_width/2, p2);
                }
                if(timeValue == 2){
                    canvas.drawBitmap(timeIcon, time30_x, time30_y, p2);
                    canvas.drawText("30초", time30_x+button_width*1/4, time30_y+button_width/2, p2);
                    canvas.drawBitmap(timeIcon, time60_x, time60_y, p2);
                    canvas.drawText("1분", time60_x+button_width*1/4, time60_y+button_width/2, p2);
                    canvas.drawBitmap(timeIcon, time120_x, time120_y, p1);
                    canvas.drawText("2분", time120_x+button_width*1/4, time120_y+button_width/2, p1);
                }

                // 도움말버튼
                canvas.drawBitmap(help, help_x, help_y, p1);

                // 적용,닫기버튼
                canvas.drawBitmap(apply, apply_x, apply_y, p1);
                canvas.drawBitmap(close, close_x, close_y, p1);
            }

            // 도움말화면
            if(menuOk == 2){
                // 도움말 내용
                canvas.drawText("바구니를 움직여서", Width/20, Height/20, p4);
                canvas.drawText("정답풍선을 받는 게임", Width/20, Height/9, p4);
                canvas.drawText("만든이 : 김준호", Width/20, Height/4, p4);
                // 닫기버튼
                canvas.drawBitmap(close, close_x-close_x/2, close_y, p1);
            }

            // 결과화면
            if(menuOk == 3){
                // 게임오버 아이콘
                canvas.drawBitmap(gameOver, gameOver_x, gameOver_y, p1);
                // 게임결과
                canvas.drawText("맞은 개수 : " + oNumber + "개", Width/10, Height*9/20, p4);
                canvas.drawText("틀린 개수 : " + xNumber + "개", Width/10, Height*1/2, p4);
                // 닫기버튼
                canvas.drawBitmap(close, close_x-close_x/2, close_y, p1);
            }

        }

        //--------------------------------
        //  run
        //--------------------------------
        public void run() { // 스레드 작동 메소드 -> 재구현 필수
            Canvas canvas = null;
            while (true) {
                canvas = mHolder.lockCanvas();  // 서피스홀더의 잠겨진 캔버스 참조
                try {
                    synchronized (mHolder){ // 동기화 유지
                        // @@ 반복처리되는 부분 @@
                        drawEverything(canvas); // 그리기 메소드 호출
                    }
                } finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);    // 화면에 그려진 뷰 표시
                }
            }
        }

        //--------------------------------
        //  moveBalloon
        //--------------------------------
        public void moveBalloon(){  // 풍선 이동 메소드
            Random random = new Random();

            // 오답 풍선 이동
            for(int i = balloon.size()-1; i>=0; i--){
                balloon.get(i).y += balloonSpeed;
            }

            // 오답 풍선 밑으로 사라지면 위에서 다시 나옴
            for (int i = balloon.size()-1; i>=0; i--){
                if(balloon.get(i).y > Height) {
                    balloon.get(i).y = -100;
                    balloon.get(i).x = random.nextInt(Width - button_width);
                }
            }

            // 정답 풍선 이동
            answerBalloon.y += balloonSpeed;

            // 정답 풍선 밑으로 사라지면 위에서 다시 나옴
            if(answerBalloon.y > Height){
                answerBalloon.y = -100;
                answerBalloon.x = random.nextInt(Width-button_width);
            }

        }

        //--------------------------------
        //  checkCollision
        //--------------------------------
        public void checkCollision() {  // 충돌 감지 메소드
            // 정답 바구니 처리
            if((basket_x<answerBalloon.x+button_width/2 && answerBalloon.x+button_width/2<basket_x+basketWidth)
                    && (answerBalloon.y+button_width > basket_y && answerBalloon.y+button_width<basket_y+basketHeight/2)){
                makeQuestion(); // 새 문제 생성
                int xx;
                Random random = new Random();
                xx = random.nextInt(Width-button_width);
                answerBalloon.x = xx;
                answerBalloon.y = -200;
                score += 30;
                oNumber++;
                sPool.play(correct, 1,1,9,0,1);
            }

            // 오답 바구니 처리
            for (int i=balloon.size()-1; i>=0; i--){
                if((basket_x<balloon.get(i).x+button_width/2 && balloon.get(i).x+button_width/2<basket_x+basketWidth)
                        && (balloon.get(i).y+button_width>basket_y && balloon.get(i).y+button_width<basket_y+basketHeight/2)){
                    balloon.remove(i);
                    score -= 10;
                    xNumber++;
                    sPool.play(wrong, 1, 1, 9, 0, 1);
                }

            }
        }

        //--------------------------------
        //  makeQuestion
        //--------------------------------
        public void makeQuestion() {    // 문제 생성 메소드
            Random r1 = new Random();
            num1 = r1.nextInt(99) + 1;
            num2 = r1.nextInt(99) + 1;

            switch (operater){
                case 0: // 더하기
                    // 정답 생성
                    answer = num1 + num2;
                    // 오답 생성
                    for (int i = 0; i < 5; i++) {
                        wrongNum[i] = r1.nextInt(197) + 1;
                        while (wrongNum[i] == answer) {
                            wrongNum[i] = r1.nextInt(197) + 1;
                        }
                    }
                    break;
                case 1: // 빼기
                    // 정답 생성
                    answer = (num1>num2) ? num1-num2 : num2-num1;
                    // 오답 생성
                    for (int i = 0; i < 5; i++) {
                        wrongNum[i] = r1.nextInt(197) + 1;
                        while (wrongNum[i] == answer) {
                            wrongNum[i] = r1.nextInt(197) + 1;
                        }
                    }
                    break;
                case 2: // 곱하기
                    r1 = new Random();
                    num1 = r1.nextInt(9) + 1;
                    num2 = r1.nextInt(9) + 1;
                    // 정답 생성
                    answer = num1 * num2;
                    // 오답 생성
                    for (int i = 0; i < 5; i++) {
                        wrongNum[i] = r1.nextInt(80) + 1;
                        while (wrongNum[i] == answer) {
                            wrongNum[i] = r1.nextInt(80) + 1;
                        }
                    }
                    break;
            }

        }

    }   // End of MyThread


    //--------------------------------
    // onTouchEvent
    //--------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {    // 화면 터치 이벤트 처리 메소드
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            int x = (int)event.getX();
            int y = (int)event.getY();

            // 게임화면
            if(menuOk == 0) {
                // 터치한 곳이 왼쪽 버튼일 경우
                if ((x > leftKey_x && x < leftKey_x + button_width) && (y > leftKey_y && y < leftKey_y + button_width)) {
                    if (basket_x > 0)
                        basket_x -= basketSpeed;
                }
                // 터치한 곳이 오른쪽 버튼일 경우
                if ((x > rightKey_x && x < rightKey_x + button_width) && (y > rightKey_y && y < rightKey_y + button_width)) {
                    if (basket_x + basketWidth < Width)
                        basket_x += basketSpeed;
                }

                // 메뉴버튼 터치시
                if ((menuButton_x<x && x<menuButton_x+button_width) && (menuButton_y<y && y<menuButton_y+button_width)){
                    // 메뉴화면으로 이동
                    menuOk = 1;
                }
            }

            // 메뉴화면
            if(menuOk == 1) {
                // 문제유형 선택
                if((plusIcon_x<x && x<plusIcon_x+button_width) && (plusIcon_y<y && y<plusIcon_y+button_width)){
                    // 더하기 터치시
                    operater = 0;
                    mThread.makeQuestion();
                }
                if((minusIcon_x<x && x<minusIcon_x+button_width) && (minusIcon_y<y && y<minusIcon_y+button_width)){
                    // 빼기 터치시
                    operater = 1;
                    mThread.makeQuestion();
                }
                if((multiIcon_x<x && x<multiIcon_x+button_width) && (multiIcon_y<y && y<multiIcon_y+button_width)){
                    // 곱하기 터치시
                    operater = 2;
                    mThread.makeQuestion();
                }

                // 난이도 선택
                if((level1_x<x && x<level1_x+button_width) && (level1_y<y && y<level1_y+button_width)) {
                    // 쉬움
                    balloonSpeed = Width/200;
                    level = 0;
                }
                if((level2_x<x && x<level2_x+button_width) && (level2_y<y && y<level2_y+button_width)) {
                    // 쉬움
                    balloonSpeed = Width/140;
                    level = 1;
                }
                if((level3_x<x && x<level3_x+button_width) && (level3_y<y && y<level3_y+button_width)) {
                    // 쉬움
                    balloonSpeed = Width/70;
                    level = 2;
                }

                // 시간 선택
                if((time30_x<x && x<time30_x+button_width) && (time30_y<y && y<time30_y+button_width)){
                    // 30초
                    count = 50*30;
                    timeValue = 0;
                }
                if((time60_x<x && x<time60_x+button_width) && (time60_y<y && y<time60_y+button_width)){
                    // 1분
                    count = 50*60;
                    timeValue = 1;
                }
                if((time120_x<x && x<time120_x+button_width) && (time120_y<y && y<time120_y+button_width)){
                    // 2분
                    count = 50*120;
                    timeValue = 2;
                }

                // 도움말버튼
                if((help_x<x && x<help_x+button_width*2/3) && (help_y<y && y<help_y+button_width*2/3)){
                    // 도움말 화면 열기
                    menuOk = 2;
                }

                // 적용버튼
                if((apply_x<x && x<apply_x+button_width*2/3) && (apply_y<y && y<apply_y+button_width*2/3)){
                    // 메뉴 닫기
                    menuOk = 0;
                    oNumber = 0;
                    xNumber = 0;
                }

                // 닫기버튼
                if((close_x<x && x<close_x+button_width*2/3) && (close_y<y && y<close_y+button_width*2/3)){
                    // 메뉴 닫기
                    menuOk = 0;
                    oNumber = 0;
                    xNumber = 0;
                }
            }

            // 도움말화면
            if(menuOk == 2 || menuOk == 3) {
                // 닫기버튼
                if((close_x-close_x/2<x && x<close_x-close_x/2+button_width*2/3) && (close_y<y && y<close_y+button_width*2/3)){
                    // 도움말 닫기 -> 메뉴화면으로 이동
                    menuOk = 1;
                }
            }

      }
        return true;
    }

}
