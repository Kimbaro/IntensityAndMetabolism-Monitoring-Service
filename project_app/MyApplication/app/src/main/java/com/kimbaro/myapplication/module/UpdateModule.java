package com.kimbaro.myapplication.module;

import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonObject;
import com.kimbaro.myapplication.MainActivity;
import com.kimbaro.myapplication.bt_module.DeviceScanActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateModule {

    public void requestUpdate() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap data = new HashMap();
                data.put("groupId", UserConfig.GROUPCODE);
                data.put("userId", UserConfig.USERID);
                data.put("rate", UserConfig.RATE);
                data.put("timer", UserConfig.TIMER);
                data.put("min_strength", UserConfig.MIN_STRENGTH);
                data.put("max_strength", UserConfig.MAX_STRENGTH);
                data.put("min_rate", UserConfig.MIN_RATE);
                data.put("max_rate", UserConfig.MAX_RATE);


                Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerConfig.IP).addConverterFactory(GsonConverterFactory.create()).build();
                RetrofitService trco = retrofit.create(RetrofitService.class);

                Call<JsonObject> call = trco.input_data(data);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e("ASDKIM","UpdateModule : "+response.body());
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
