package com.kimbaro.myapplication.module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.kimbaro.myapplication.JoinActivity;
import com.kimbaro.myapplication.MainActivity;
import com.kimbaro.myapplication.MeasureActivity;
import com.kimbaro.myapplication.bt_module.DeviceScanActivity;

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
//                StartModule startModule = new StartModule(activity);
//                startModule.requestStart();
                Intent intent = new Intent(activity, DeviceScanActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    };

    public LoginModule(Activity activity) {
        this.activity = activity;
    }

    public void search_channel(final String groupCode){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Call<JsonObject> call = trco.search_ch(groupCode);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("Kim", "응답 : "+response.code());
                        Log.e("Kim", "응답 : "+response.body());
                        Log.e("Kim", "응답 : "+response.message());
                        if(response.code() == 200){
                            JsonObject jsonObject = response.body();
                            UserConfig.channel = jsonObject.get("id").getAsString();
                            activity.finish();
                            activity.startActivity(new Intent(activity, MainActivity.class));
                        }else{
                            activity.finish();
                            activity.startActivity(new Intent(activity, JoinActivity.class));
                        }
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

    public void join(final HashMap<String, String> data, final int type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Call<JsonObject> call = trco.join_ch(data);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("Kim", "응답 : "+response.code());
                        Log.e("Kim", "응답 : "+response.body());
                        Log.e("Kim", "응답 : "+response.message());
//                        JsonObject jsonObject = response.body();
//                        mHandler.sendEmptyMessage(type);
                        if(response.code() == 200){
                            JsonObject jsonObject = response.body();
                            UserConfig.mobile_id = jsonObject.get("id").getAsString();
                            UserConfig.channel = jsonObject.get("channel").getAsString();
                            MainActivity.mProgress.cancel();
                            mHandler.sendEmptyMessage(type);
                        }else{
                            MainActivity.mProgress.cancel();
                            activity.finish();
                            activity.startActivity(new Intent(activity, JoinActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        //데이터가 디비로 부터 반환되지 않았거나 에러인경우
                        Log.e("Kim", "에러입니다." + t.getCause());
                    }
                });

            /*
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
                });*/
            }
        });
        thread.start();

    }
}


