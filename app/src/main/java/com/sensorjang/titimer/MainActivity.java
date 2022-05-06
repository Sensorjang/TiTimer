package com.sensorjang.titimer;

import static android.content.ContentValues.TAG;

import static net.crosp.libs.android.circletimeview.CircleTimeView.FORMAT_SECONDS_MINUTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.snappingstepper.SnappingStepper;
import com.github.sshadkany.CircleButton;
import com.github.sshadkany.RectButton;
import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.sensorjang.titimer.utils.ColorUtil;
import com.sensorjang.titimer.utils.httpGitAPICallable;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.crosp.libs.android.circletimeview.CircleTimeView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MainActivity extends AppCompatActivity {

    Map getMap ;//用于获取版本更新

    RectButton r_button1;
    RectButton r_button2;
    RectButton r_button3;
    RectButton r_button4;
    RectButton r_button5;
    RectButton btn_fllstus;
    RectButton btn_clr;
    RectButton start_btn;
    RectButton btn_update;
    RectButton ok_btn;
    CircleButton c_button1;
    ImageView sunOrMoonImg;
    CircleTimeView timer;
    Balloon balloon;
    TextView startTxt;
    TextView fllstusTxt;
    int flag = 1;//循环标志 1开 0关
    long timeMem=3600;//时间循环段长度记忆
    int dingMen=0;//定长时间循环段长度记忆
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            System.out.println(getVersionName(MainActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        btn_update = findViewById(R.id.btn_update);
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
                ColorUtil.turnToNight(btn_update);
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
                ColorUtil.turnToDay(btn_update);
                ColorUtil.turnToDay(c_button1);
                ColorUtil.turnToDay(btn_clr);
                ColorUtil.turnToDay(start_btn);
                break;
        }

        //初始化Timer
//        timer.setMaximumTime(3600);
//        timer.setValue(300);
        timer.setCurrentTime(0,FORMAT_SECONDS_MINUTES);


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
                AppCompatDelegate.setDefaultNightMode(mode);
                if(!startTxt.getText().equals("Start"))timer.startTimer();

            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                if(timer.getCurrentTimeInSeconds()==0){
                    Toast toast = Toast.makeText(getApplicationContext(),"记得设定时间哦!",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(startTxt.getText().equals("Start")){
                    timer.stopTimer();
                    timeMem = timer.getCurrentTimeInSeconds();
                    timer.startTimer();
                    startTxt.setText("Stop");
                }else {
                    timer.stopTimer();
                    startTxt.setText("Start");
                }
            }
        });
        c_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                if(timer.getCurrentTimeInSeconds()==0) {
                    Toast toast = Toast.makeText(getApplicationContext(),"记得设定时间哦!",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(startTxt.getText().equals("Start")){
                    timer.stopTimer();
                    timeMem = timer.getCurrentTimeInSeconds();
                    timer.startTimer();
                    startTxt.setText("Stop");
                }else {
                    timer.stopTimer();
                    startTxt.setText("Start");
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vib(52);
                Toast toast1 = Toast.makeText(getApplicationContext(),"稍等我三秒钟叭!",Toast.LENGTH_SHORT);
                toast1.show();
                //gitee api url:https://gitee.com/api/v5/repos/Sensorjang/TiTimer/releases/latest
                //github api url:https://api.github.com/repos/Sensorjang/TiTimer/releases/latest
                String giteeUrl = "https://gitee.com/api/v5/repos/Sensorjang/TiTimer/releases/latest";
                String result = null;
                try {
                    result = sendGetRequest(giteeUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(result!=null){
                    getMap = (Map) JSON.parse(result);
                    Log.i(TAG, "Version info get success");
                }else{
                    Log.i(TAG, "Version info get failed");
                }
                String newVersionName = null;
                if(getMap!=null){
                    newVersionName= getMap.get("tag_name").toString();
                    Log.i(TAG, "NEW Version info get success");
                }else{
                    Log.i(TAG, "NEW Version info get failed");
                }
                boolean isWillUpdate = false;
                try {
                    if(getVersionName(MainActivity.this).equals(newVersionName)){
                        Toast toast2 = Toast.makeText(getApplicationContext(),"已经是最新版了哦!",Toast.LENGTH_SHORT);
                        toast2.show();
                    }else isWillUpdate = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(isWillUpdate){
                    List l =JSON.parseArray(getMap.get("assets").toString());
                    Map simpleMap = (Map) JSON.parse(l.get(0).toString());
                    String downloadUrl = simpleMap.get("browser_download_url").toString();
                    String desc = "船新版本："+getMap.get("name")+"\n船新体验："+getMap.get("body");

                    AppDialogConfig config = new AppDialogConfig(MainActivity.this);
                    config.setTitle("马上开始升级")
                            .setConfirm("升级") //旧版本使用setOk
                            .setContent("desc")
                            .setOnClickConfirm(new View.OnClickListener() { //旧版本使用setOnClickOk
                                @Override
                                public void onClick(View v) {
                                    new AppUpdater.Builder()
                                            .setUrl(downloadUrl)
                                            .build(MainActivity.this)
                                            .start();
                                    AppDialog.INSTANCE.dismissDialog();
                                }
                            });
                    AppDialog.INSTANCE.showDialog(MainActivity.this,config);
                }

            }
        });

        btn_fllstus.setOnClickListener(new View.OnClickListener() {
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
                timer.stopTimer();
//                timer.setMaximumTime(60);
                timer.setCurrentTime(60);
                dingMen=60;
                timer.startTimer();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });

        r_button3.setOnClickListener(new View.OnClickListener() {//5min
            @Override
            public void onClick(View v) {
                vib(50);
                timer.stopTimer();
//                timer.setMaximumTime(60*5);
                timer.setCurrentTime(60*5);
                dingMen=60*5;
                timer.startTimer();
                startTxt.setText("Stop");

                flag=1;//循环已开
                fllstusTxt.setText("循环： ON");
            }
        });

        r_button4.setOnClickListener(new View.OnClickListener() {//10min
            @Override
            public void onClick(View v) {
                vib(50);
                timer.stopTimer();
//                timer.setMaximumTime(60*10);
                timer.setCurrentTime(60*10);
                dingMen=60*10;
                timer.startTimer();
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
                timer.stopTimer();
//                timer.setMaximumTime(3600);
                timer.setCurrentTime(0);
                startTxt.setText("Start");
            }
        });

        timer.setCircleTimeListener(new CircleTimeView.CircleTimeListener() {
            @Override
            public void onTimeManuallySet(long time) {
                Log.d("TIME LISTENER", "onTimeManuallySet " + time);
            }

            @Override
            public void onTimeManuallyChanged(long time) {
                Log.d("TIME LISTENER", "onTimeManuallyChanged " + time);
            }

            @Override
            public void onTimeUpdated(long time) {
                Log.d("TIME LISTENER", "onTimeUpdated " + time);
            }
        });
        timer.setCircleTimerListener(new CircleTimeView.CircleTimerListener() {
            @Override
            public void onTimerStop() {
                Log.d("TIMER LISTENER", "onTimerStop ");
            }
            @Override
            public void onTimerStart(long time) {
                Log.d("TIMER LISTENER", "onTimerStart " + time);
            }

            @Override
            public void onTimerTimeValueChanged(long time) {
                Log.d("TIMER LISTENER", "onTimerTimeValueChanged " + time);
                if(time==0){
                    startTxt.setText("Start");
                    timer.stopTimer();
                    vib(1500);
                    if(flag==1){//循环开
//                    timer.setMaximumTime(dingMen==0?timeMem:dingMen);//循环
                        timer.setCurrentTime(dingMen==0?timeMem:dingMen);
                        timer.startTimer();
                        startTxt.setText("Stop");
                        Toast toast = Toast.makeText(getApplicationContext(),"我会继续循环提醒你哦!",Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(getApplicationContext(),"时间到咯!",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

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

                timer.stopTimer();
//                timer.setMaximumTime(time_total);
                timer.setCurrentTime(time_total);
                dingMen=time_total;
                timer.startTimer();
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

    // 利用http client发起get请求获取信息
    public static String sendGetRequest(String url) throws IOException, ExecutionException, InterruptedException {

        httpGitAPICallable callable = new httpGitAPICallable();
        callable.setUrl(url);
        FutureTask<String> ft = new FutureTask<>(callable);
        Thread thread = new Thread(ft);
        thread.start();
        SystemClock.sleep(3000);
        return ft.get();
    }
//        //1.创建httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        //2.创建httpget对象，设置url地址
//        HttpGet httpGet = new HttpGet(url);
//        CloseableHttpResponse response = null;
//        //在外面创建，为方便最后关闭
//        String content=null;
//        try {
//            //3.使用httpclient发起请求，获取response
//            response = httpClient.execute(httpGet);
//            //4.解析响应
//
//            if (response.getStatusLine().getStatusCode() == 200){
//                //状态码200为成功
//                content = EntityUtils.toString(response.getEntity(), "utf8");
//                System.out.println(content.length());
//                Log.e(TAG, "sendGetRequest: "+response.toString() );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            //关闭response
//            try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return content;
//        }


    public static String getVersionName(Context context) throws Exception {
// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
// getPackageName()是你当前类的包名
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }



}