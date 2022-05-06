package com.sensorjang.titimer.utils;

import android.graphics.Color;

import com.bigkoo.snappingstepper.SnappingStepper;
import com.github.sshadkany.CircleButton;
import com.github.sshadkany.RectButton;
import com.sensorjang.titimer.R;

public class ColorUtil {

    public static void turnToDay(RectButton r_button1){
        r_button1.setBackground_color(Color.parseColor("#EFEFED"));
        r_button1.setDark_color(Color.parseColor("#807E7E"));
        r_button1.setLight_color(Color.parseColor("#FFFFFF"));
        r_button1.setBorderColor(Color.parseColor("#F1F4EB"));
    }

    public static void turnToNight(RectButton r_button1){
        r_button1.setBackground_color(Color.parseColor("#3F4042"));
        r_button1.setDark_color(Color.parseColor("#151515"));
        r_button1.setLight_color(Color.parseColor("#4E4D4D"));
        r_button1.setBorderColor(Color.parseColor("#424141"));
    }

    public static void turnToDay(CircleButton r_button1){
        r_button1.setBackground_color(Color.parseColor("#EFEFED"));
        r_button1.setDark_color(Color.parseColor("#807E7E"));
        r_button1.setLight_color(Color.parseColor("#FFFFFF"));
        r_button1.setBorderColor(Color.parseColor("#F1F4EB"));
    }

    public static void turnToNight(CircleButton r_button1){
        r_button1.setBackground_color(Color.parseColor("#3F4042"));
        r_button1.setDark_color(Color.parseColor("#151515"));
        r_button1.setLight_color(Color.parseColor("#4E4D4D"));
        r_button1.setBorderColor(Color.parseColor("#424141"));
    }

    public static void turnToDay(SnappingStepper snappingStepper){
        snappingStepper.setLeftButtonResources(R.drawable.left);
        snappingStepper.setRightButtonResources(R.drawable.right);
        snappingStepper.setContentTextColor(R.color.black);
        snappingStepper.setButtonBackGround(R.color.buttonBackGroundD);
        snappingStepper.setContentBackground(R.color.contentBackgroundD);
    }

    public static void turnToNight(SnappingStepper snappingStepper){
        snappingStepper.setLeftButtonResources(R.drawable.left_d);
        snappingStepper.setRightButtonResources(R.drawable.right_d);
        snappingStepper.setContentTextColor(R.color.white);
        snappingStepper.setButtonBackGround(R.color.buttonBackGroundN);
        snappingStepper.setContentBackground(R.color.contentBackgroundN);
    }
}
