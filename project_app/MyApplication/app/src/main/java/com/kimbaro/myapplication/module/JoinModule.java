package com.kimbaro.myapplication.module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kimbaro.myapplication.MainActivity;
import com.kimbaro.myapplication.MeasureActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinModule { //개인 사용자 운동 모니터링 채널 연동   //type = 1
    Activity activity;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) { //개인
                StartModule startModule = new StartModule(activity);
                startModule.requestStart();
            }
        }
    };

    public JoinModule(Activity activity) {
        this.activity = activity;
    }

    public void requestJoin() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String groupcode = UserConfig.USERID.substring(UserConfig.USERID.length() - 6, UserConfig.USERID.length());

                HashMap data = new HashMap();
                data.put("groupcode", groupcode);
                data.put("type", "1");

                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Call<JsonObject> call = trco.user_join(data);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();
                        UserConfig.GROUPCODE = jsonObject.get("groupcode").getAsString();
                        mHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //데이터가 디비로 부터 반환되지 않았거나 에러인경우
                        MainActivity.mProgress.cancel();
                        activity.finish();
                        Log.e("Kim", "에러입니다." + t.getCause());
                    }
                });
            }
        });
        thread.start();
    }
}
