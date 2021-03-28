package com.kimbaro.myapplication.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kimbaro.myapplication.MeasureActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginModule {
    Activity activity;
    String id = "";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) { //개인
                JoinModule joinModule = new JoinModule(activity);
                joinModule.requestJoin();
            } else if (msg.what == 2) {  //단체
                StartModule startModule = new StartModule(activity);
                startModule.requestStart();
            }
        }
    };

    public LoginModule(Activity activity) {
        this.activity = activity;
    }

    public void requestCreate(final HashMap<String, String> data, final int type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Call<JsonObject> call = trco.user_create(data);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject jsonObject = response.body();

                        UserConfig.USERID = jsonObject.get("id").getAsString();
                        mHandler.sendEmptyMessage(type);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //데이터가 디비로 부터 반환되지 않았거나 에러인경우
                        Log.e("Kim", "에러입니다." + t.getCause());
                    }
                });
            }
        });
        thread.start();

    }
}


