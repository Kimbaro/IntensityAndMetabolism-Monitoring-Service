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
import retrofit2.http.QueryName;

public interface RetrofitService {
    @GET("/user_create")
    Call<JsonObject> user_create(@QueryMap Map<String, String> data);

    @GET("/user_join")
    Call<JsonObject> user_join(@QueryMap Map<String, String> data);

    @GET("/input_data")
    Call<JsonObject> input_data(@QueryMap Map<String, String> data);

    @GET("/user_group_create")
    Call<JsonObject> user_group_create();

    //-------

    @GET("/test")
    Call<JsonObject> test();

    @GET(value = "/c_ch") //create channel
    Call<JsonObject> c_ch();

    @GET(value = "/search_ch")
    Call<JsonObject>  search_ch(@Query(value = "channel") String channel);

    @GET(value = "/join_ch") //join channel
    Call<JsonObject> join_ch(@QueryMap Map<String, String> data);

    @GET(value = "/mobile/update")
    Call<JsonObject>  mobile_update (@QueryMap Map<String, String> data);
}
