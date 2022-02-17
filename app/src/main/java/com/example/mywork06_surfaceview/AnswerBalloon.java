package com.example.mywork06_surfaceview;

public class AnswerBalloon {

    int x, y;   // 정답 풍선 위치
    int speed;  // 정답 풍선 속도

    AnswerBalloon(int x, int y, int speed){  // 생성자
        // 초기화
        this.x = x; this.y = y; this.speed = speed;
    }

    public void move(){ // 정답 풍선 이동 메소드
        y += speed;
    }
}
