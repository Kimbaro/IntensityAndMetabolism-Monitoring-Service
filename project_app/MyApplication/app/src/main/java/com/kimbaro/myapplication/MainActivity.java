package com.kimbaro.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.kimbaro.myapplication.module.LoginModule;
import com.kimbaro.myapplication.module.UserConfig;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText name, age, stablerate, valueA, valueB;

    RadioGroup radioGroup;
    RadioGroup radioGroup_gender;
    EditText groupCode;
    Button bt_start;
    int typeCheck = 1; // 1개인 , 2단체
    String gender = "남"; //성별
    Activity activity;
    public static ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.activity = this;

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        stablerate = findViewById(R.id.stablerate);
        valueA = findViewById(R.id.valueA);
        valueB = findViewById(R.id.valueB);

        groupCode = findViewById(R.id.groupcode);
        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        radioGroup = findViewById(R.id.radioGroup);
        bt_start = findViewById(R.id.start);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("ASDKIM", checkedId + "");
                if (checkedId == R.id.radioGroup_privt) { //개인
                    groupCode.setVisibility(View.GONE);
                    typeCheck = 1;
                } else if (checkedId == R.id.radioGroup_group) { //단체
                    groupCode.setVisibility(View.VISIBLE);
                    typeCheck = 2;
                }
            }
        });

        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioGroup_female) { //여
                    gender = "여";
                } else if (checkedId == R.id.radioGroup_male) { //남
                    gender = "남";
                }
            }
        });

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress = ProgressDialog.show(activity, "작동중", "회원정보를 등록중 입니다.");
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("name", name.getText().toString());
                data.put("age", age.getText().toString());
                data.put("stablerate", stablerate.getText().toString());
//                data.put("valueA", UserConfig.MIN_STRENGTH);
//                data.put("valueB", UserConfig.MAX_STRENGTH);

                UserConfig.AGE = age.getText().toString();
                UserConfig.NAME = name.getText().toString();
                UserConfig.STABLE_RATE = stablerate.getText().toString();
//                UserConfig.MIN_STRENGTH = valueA.getText().toString();
//                UserConfig.MAX_STRENGTH = valueB.getText().toString();
                UserConfig.GENDER = gender;
//                setKarvonen(gender);

                if (typeCheck == 1) {//개인
                    LoginModule loginModule = new LoginModule(activity);
                    loginModule.requestCreate(data, typeCheck);


                } else if (typeCheck == 2) { //단체
                    UserConfig.GROUPCODE = groupCode.getText().toString();
                    LoginModule loginModule = new LoginModule(activity);
                    loginModule.requestCreate(data, typeCheck);
                }
            }
        });
    }

    public void setKarvonen(String gender) {
        int age = Integer.valueOf(UserConfig.AGE);//나이
        int min_st = Integer.valueOf(UserConfig.MIN_STRENGTH);//최소강도
        int max_st = Integer.valueOf(UserConfig.MAX_STRENGTH);//최대강도
        int stable_rate = Integer.valueOf(UserConfig.STABLE_RATE); //안정시심박수

        double max_heartrate = 0.0; //최대심박수
        if (gender.equals("남")) {
            max_heartrate = (214 - (0.8 * age));
        } else if (gender.equals("여")) {
            max_heartrate = (209 - (0.7 * age));
        }
        //카르보넨 최소,최대
        UserConfig.MIN_RATE = String.format("%.2f", (max_heartrate - stable_rate) * (min_st / 100.0) + stable_rate);
        UserConfig.MAX_RATE = String.format("%.2f", (max_heartrate - stable_rate) * (max_st / 100.0) + stable_rate);
    }
}
