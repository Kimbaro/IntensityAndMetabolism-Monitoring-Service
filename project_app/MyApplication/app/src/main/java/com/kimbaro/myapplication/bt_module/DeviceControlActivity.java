/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kimbaro.myapplication.bt_module;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.kimbaro.myapplication.MainActivity;
import com.kimbaro.myapplication.R;
import com.kimbaro.myapplication.module.Karvonen;
import com.kimbaro.myapplication.module.UpdateModule;
import com.kimbaro.myapplication.module.UserConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private TextView mCode;

    private LinearLayout comment_info2;
    private LinearLayout comment_info;
    private RelativeLayout comment_start;
    private RelativeLayout comment_ready;


    private String mDeviceName;
    private String mDeviceAddress;
    private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private boolean screenCheck = true;

    public void readyScreen() {
        if (screenCheck == true) {
            screenCheck = false;
            comment_info.setVisibility(View.GONE);
            comment_info2.setVisibility(View.GONE);
            comment_start.setVisibility(View.GONE);
            comment_ready.setVisibility(View.VISIBLE);
        }
    }

    public void startScreen() {
        if (screenCheck == false) {
            screenCheck = true;
            comment_info.setVisibility(View.VISIBLE);
            comment_info2.setVisibility(View.VISIBLE);
            comment_start.setVisibility(View.VISIBLE);
            comment_ready.setVisibility(View.GONE);
        }
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    Karvonen karvonen;
    UpdateModule updateModule;
    boolean sendCheck = true;
    Handler mHandler;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                intent = new Intent(getApplicationContext(), DeviceScanActivity.class);
                startActivity(intent);
                finish();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                // displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                if (UserConfig.RATE.equals("0")) {
                    readyScreen();
                } else {
                    startScreen();
                    karvonen.checkTime(Integer.parseInt(UserConfig.MIN_RATE), Integer.parseInt(UserConfig.MAX_RATE));
                    if (sendCheck) {
                        sendCheck = false;
                        Log.e("ASDKIM", UserConfig.USERID);
                        Log.e("ASDKIM", UserConfig.GROUPCODE);
                        Log.e("ASDKIM", UserConfig.RATE);
                        Log.e("ASDKIM", UserConfig.MIN_RATE);
                        Log.e("ASDKIM", UserConfig.MAX_RATE);
                        Log.e("ASDKIM", UserConfig.MIN_STRENGTH);
                        Log.e("ASDKIM", UserConfig.MAX_STRENGTH);
                        Log.e("ASDKIM", UserConfig.NAME);
                        Log.e("ASDKIM", "운동유지시간 : " + UserConfig.TIMER);
                        updateModule.requestUpdate();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendCheck = true;
                            }
                        }, 1000);
                    }
                }
            }
        }
    };

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        comment_info2 = findViewById(R.id.comment_info2);
        comment_info = findViewById(R.id.comment_info);
        comment_start = findViewById(R.id.comment_start);
        comment_ready = findViewById(R.id.comment_ready);

        readyScreen();

        mHandler = new Handler();
        karvonen = new Karvonen();
        updateModule = new UpdateModule();

        min_streng = findViewById(R.id.min_strength);
        max_streng = findViewById(R.id.max_strength);

        min_rate = findViewById(R.id.min_rate);
        max_rate = findViewById(R.id.max_rate);

        mCode = findViewById(R.id.code);
        mCode.setText(" / "+UserConfig.GROUPCODE);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);


        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        // mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        // mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                Log.e("ASDKIM", mDeviceAddress);
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mDataField.setText(data);
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(
                this,
                gattServiceData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData,
                android.R.layout.simple_expandable_list_item_2,
                new String[]{LIST_NAME, LIST_UUID},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        mGattServicesList.setAdapter(gattServiceAdapter);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    TextView min_streng;
    TextView max_streng;
    TextView min_rate;
    TextView max_rate;

    public void type_click(View v) {
        switch (v.getId()) {
            case R.id.type1://워밍업
                UserConfig.MIN_STRENGTH = "0";
                UserConfig.MAX_STRENGTH = "60";
                karvonen.setKarvonen(UserConfig.GENDER, 0, 60);
                min_streng.setText(UserConfig.MIN_STRENGTH);
                max_streng.setText(UserConfig.MAX_STRENGTH);
                min_rate.setText(UserConfig.MIN_RATE);
                max_rate.setText(UserConfig.MAX_RATE);
                break;
            case R.id.type2://유산소 약
                UserConfig.MIN_STRENGTH = "60";
                UserConfig.MAX_STRENGTH = "70";
                karvonen.setKarvonen(UserConfig.GENDER, 60, 70);
                min_streng.setText(UserConfig.MIN_STRENGTH);
                max_streng.setText(UserConfig.MAX_STRENGTH);
                min_rate.setText(UserConfig.MIN_RATE);
                max_rate.setText(UserConfig.MAX_RATE);
                break;
            case R.id.type3://유산소 강
                UserConfig.MIN_STRENGTH = "70";
                UserConfig.MAX_STRENGTH = "80";
                karvonen.setKarvonen(UserConfig.GENDER, 70, 80);
                min_streng.setText(UserConfig.MIN_STRENGTH);
                max_streng.setText(UserConfig.MAX_STRENGTH);
                min_rate.setText(UserConfig.MIN_RATE);
                max_rate.setText(UserConfig.MAX_RATE);
                break;
            case R.id.type4://고강도운동
                UserConfig.MIN_STRENGTH = "80";
                UserConfig.MAX_STRENGTH = "100";
                karvonen.setKarvonen(UserConfig.GENDER, 80, 100);
                min_streng.setText(UserConfig.MIN_STRENGTH);
                max_streng.setText(UserConfig.MAX_STRENGTH);
                min_rate.setText(UserConfig.MIN_RATE);
                max_rate.setText(UserConfig.MAX_RATE);
                break;
        }
    }
}
