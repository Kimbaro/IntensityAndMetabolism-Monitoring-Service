package com.kimbaro.myapplication.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    @GET("/user_create")
    Call<JsonObject> user_create(@QueryMap Map<String, String> data);

    @GET("/user_join")
    Call<JsonObject> user_join(@QueryMap Map<String, String> data);

    @GET("/input_data")
    Call<JsonObject> input_data(@QueryMap Map<String, String> data);

    @GET("/user_group_create")
    Call<JsonObject> user_group_create();
}
