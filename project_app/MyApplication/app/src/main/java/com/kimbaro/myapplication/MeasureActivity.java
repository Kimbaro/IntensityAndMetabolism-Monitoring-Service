package com.kimbaro.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kimbaro.myapplication.module.UserConfig;

public class MeasureActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        Toast.makeText(this, "생성상태, : " + UserConfig.GROUPCODE, Toast.LENGTH_LONG).show();


    }
}
