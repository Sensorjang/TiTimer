package com.sensorjang.titimer;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.github.sshadkany.CircleButton;
import com.github.sshadkany.RectButton;
import com.mut_jaeryo.circletimer.CircleTimer;
import com.sensorjang.titimer.utils.ColorUtil;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import net.crosp.libs.android.circletimeview.CircleTimeView;

public class MainActivity extends AppCompatActivity {

    RectButton r_button1;
    RectButton r_button2;
    RectButton r_button3;
    RectButton r_button4;
    RectButton r_button5;
    RectButton btn_fllstus;
    RectButton btn_clr;
    RectButton start_btn;
    RectButton fll_btn;
    RectButton ok_btn;
    CircleButton c_button1;
    ImageView sunOrMoonImg;
    CircleTimer timer;
    Balloon balloon;
    TextView startTxt;
    TextView fllstusTxt;
    int flag = 1;//循环标志 1开 0关
    int timeMem=3600;//时间循环段长度记忆
    int dingMen=0;//定长时间循环段长度记忆
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //透明状态栏
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init(){

        r_button1 = findViewById(R.id.btn1);
        r_button2 = findViewById(R.id.btn2);
        r_button3 = findViewById(R.id.btn3);
        r_button4 = findViewById(R.id.btn4);
        r_button5 = findViewById(R.id.btn5);
        btn_fllstus = findViewById(R.id.btn_fllstus);
        start_btn = findViewById(R.id.btn_start);
        fll_btn = findViewById(R.id.btn_fll);
        c_button1 = findViewById(R.id.cbtn1);
        timer = findViewById(R.id.timer);
        btn_clr = findViewById(R.id.btn_clr);
        startTxt = findViewById(R.id.txt_start);
        fllstusTxt = findViewById(R.id.fllstus_txt);
        sunOrMoonImg = findViewById(R.id.sunOrMoon);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //启动自动适配夜间日间
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                ColorUtil.turnToNight(r_button1);
                ColorUtil.turnToNight(r_button2);
                ColorUtil.turnToNight(r_button3);
                ColorUtil.turnToNight(r_button4);
                ColorUtil.turnToNight(r_button5);
                ColorUtil.turnToNight(btn_fllstus);
                ColorUtil.turnToNight(fll_btn);
                ColorUtil.turnToNight(c_button1);
                ColorUtil.turnToNight(btn_clr);
                ColorUtil.turnToNight(start_btn);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                ColorUtil.turnToDay(r_button1);
                ColorUtil.turnToDay(r_button2);
                ColorUtil.turnToDay(r_button3);
                ColorUtil.turnToDay(r_button4);
                ColorUtil.turnToDay(r_button5);
                ColorUtil.turnToDay(btn_fllstus);
                ColorUtil.turnToDay(fll_btn);
                ColorUtil.turnToDay(c_button1);
                ColorUtil.turnToDay(btn_clr);
                ColorUtil.turnToDay(start_btn);
                break;
        }

        //初始化Timer
        timer.setMaximumTime(3600);
        timer.setValue(300);


        r_button2.setOnClickListener(new View.OnClickListener() {//dark mode btn
            @Override
            public void onClick(View v) {
                vib(52);
                int mode;
                if(r_button1.getDark_color()==Color.parseColor("#807E7E")) {//如果处于light色彩下
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mode = AppCompatDelegate.MODE_NIGHT_YES;
                }else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mode = AppCompatDelegate.MODE_NIGHT_NO;
                }

                timer.stop();
                int mem = timer.getValue();
                timer.reset();
                AppCompatDelegate.setDefaultNightMode(mode);
                timer.setValue(mem);
                if(!startTxt.getText().equals("Start"))timer.start();

            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                if(timer.getValue()==0){
                    Toast toast = Toast.makeText(getApplicationContext(),"记得设定时间哦!",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(startTxt.getText().equals("Start")){
                    timer.stop();
                    timeMem = timer.getValue();
                    timer.start();
                    startTxt.setText("Stop");
                }else {
                    timer.stop();
                    startTxt.setText("Start");
                }
            }
        });
        c_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                if(timer.getValue()==0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"记得设定时间哦!",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(startTxt.getText().equals("Start")){
                    timer.stop();
                    timeMem = timer.getValue();
                    timer.start();
                    startTxt.setText("Stop");
                }else {
                    timer.stop();
                    startTxt.setText("Start");
                }
            }
        });

        fll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                if(flag==1){
                    flag=0;//循环已关
                    fllstusTxt.setText("循环：OFF");
                }else {
                    flag=1;//循环已开
                    fllstusTxt.setText("循环： ON");
                }
            }
        });

        r_button1.setOnClickListener(new View.OnClickListener() {//60s
            @Override
            public void onClick(View v) {
                vib(50);
                timer.stop();
                timer.setMaximumTime(60);
                timer.setValue(60);
                dingMen=60;
                timer.start();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });

        r_button3.setOnClickListener(new View.OnClickListener() {//5min
            @Override
            public void onClick(View v) {
                vib(50);
                timer.stop();
                timer.setMaximumTime(60*5);
                timer.setValue(60*5);
                dingMen=60*5;
                timer.start();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });

        r_button4.setOnClickListener(new View.OnClickListener() {//10min
            @Override
            public void onClick(View v) {
                vib(50);
                timer.stop();
                timer.setMaximumTime(60*10);
                timer.setValue(60*10);
                dingMen=60*10;
                timer.start();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });

        r_button5.setOnClickListener(new View.OnClickListener() {//自定时间
            @Override
            public void onClick(View v) {
                vib(50);
//                timer.stop();
//                timer.setMaximumTime(60*10);
//                timer.setValue(60*10);
//                dingMen=60*10;
//                timer.start();
//                startTxt.setText("Stop");
                balloon = new Balloon.Builder(r_button5.getContext())
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.BOTTOM)
                        .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        .setArrowPosition(0.5f)
                        .setWidth(BalloonSizeSpec.WRAP)
                        .setHeight(260)
                        .setTextSize(15f)
                        .setCornerRadius(35f)
                        .setAlpha(0.9f)
                        .setLayout(R.layout.activity_dsgtimelayout)
                        .setTextIsHtml(true)
                        .setBalloonAnimation(BalloonAnimation.ELASTIC)
                        .setArrowColor(Color.parseColor("#7A7878"))
                        .build();
                SnappingStepper snappingStepper1 = balloon.getContentView().findViewById(R.id.stepper1);
                SnappingStepper snappingStepper2 = balloon.getContentView().findViewById(R.id.stepper2);
                SnappingStepper snappingStepper3 = balloon.getContentView().findViewById(R.id.stepper3);

                ok_btn = balloon.getContentView().findViewById(R.id.btn_ok);
                if(r_button1.getDark_color()==Color.parseColor("#807E7E")){//如果在light下
                    ColorUtil.turnToDay(ok_btn);
                    ColorUtil.turnToDay(snappingStepper1);//绘制SnappingStepper
                    ColorUtil.turnToDay(snappingStepper2);
                    ColorUtil.turnToDay(snappingStepper3);
                }else {
                    ColorUtil.turnToNight(ok_btn);
                    ColorUtil.turnToNight(snappingStepper1);//绘制SnappingStepper
                    ColorUtil.turnToNight(snappingStepper2);
                    ColorUtil.turnToNight(snappingStepper3);
                }
                balloon.showAlignTop(findViewById(R.id.btn5));
                initOkBtn();
            }
        });

        btn_clr.setOnClickListener(new View.OnClickListener() {//归位
            @Override
            public void onClick(View v) {
                vib(52);
                timeMem=3600;//时间循环段长度记忆
                dingMen=0;//定长时间循环段长度记忆
                timer.stop();
                timer.setMaximumTime(3600);
                timer.setValue(300);
                startTxt.setText("Start");
            }
        });

        timer.setBaseTimerEndedListener(new CircleTimer.baseTimerEndedListener() { //timer
            @Override
            public void OnEnded() {
                startTxt.setText("Start");
                timer.stop();
                vib(1500);
                if(flag==1){//循环开
                    timer.setMaximumTime(dingMen==0?timeMem:dingMen);//循环
                    timer.setValue(dingMen==0?timeMem:dingMen);
                    timer.start();
                    startTxt.setText("Stop");
                    Toast toast = Toast.makeText(getApplicationContext(),"我会继续循环提醒你哦!",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),"时间到咯!",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        timer.stop();
    }

    private void initOkBtn(){
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(50);
                SnappingStepper snappingStepper1 = balloon.getContentView().findViewById(R.id.stepper1);//时
                SnappingStepper snappingStepper2 = balloon.getContentView().findViewById(R.id.stepper2);//分
                SnappingStepper snappingStepper3 = balloon.getContentView().findViewById(R.id.stepper3);//秒
                int time_h = snappingStepper1.getValue();
                int time_m = snappingStepper2.getValue();
                int time_s = snappingStepper3.getValue();
                int time_total = time_h*60*60+time_m*60+time_s;//自定时间循环

                if(time_total==0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"记得设定时间哦!",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                timer.stop();
                timer.setMaximumTime(time_total);
                timer.setValue(time_total);
                dingMen=time_total;
                timer.start();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });
    }

    private void vib(int time){//Vibrator震动
        AudioAttributes VIBRATION_ATTRIBUTES = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN) /*USAGE_ALARM*/
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        vibrator.vibrate(new long[]{0,time}, -1,VIBRATION_ATTRIBUTES);
    }



}