package com.kimbaro.myapplication.module;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class Karvonen {
    public void setKarvonen(String gender, int min, int max) {
        int age = Integer.valueOf(UserConfig.AGE);//나이
        int min_st = min;//최소강도
        int max_st = max;//최대강도
        int stable_rate = Integer.valueOf(UserConfig.STABLE_RATE); //안정시심박수

        double max_heartrate = 0.0; //최대심박수
        if (gender.equals("남")) {
            max_heartrate = (214 - (0.8 * age));
        } else if (gender.equals("여")) {
            max_heartrate = (209 - (0.7 * age));
        }
        //카르보넨 최소,최대
        UserConfig.MIN_RATE = (int) ((max_heartrate - stable_rate) * (min_st / 100.0) + stable_rate) + "";
        UserConfig.MAX_RATE = (int) ((max_heartrate - stable_rate) * (max_st / 100.0) + stable_rate) + "";
    }

    public boolean checkTime(int min, int max) {
        int rate = Integer.valueOf(UserConfig.RATE);
        int min_rate = min;
        int max_rate = max;
        if (rate > 0) {
            if (rate >= min_rate && rate <= max_rate) {
                if (runCheck) {
                    runCheck = false;
                    startUpdate();
                }
                UserConfig.TIMER = String.valueOf(time);
                return true;
            } else {

            }
        }
        return false;
    }

    int time = 0;
    Handler mHandler = new Handler();
    boolean runCheck = true;

    public void startUpdate() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time += 1;
                runCheck = true;
            }
        }, 1000);
    }

    public void stopUpcate() {
        try {
            mHandler.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
