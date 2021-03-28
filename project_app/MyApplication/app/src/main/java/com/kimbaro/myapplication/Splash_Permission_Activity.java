package com.kimbaro.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kimbaro.myapplication.bt_module.DeviceScanActivity;

public class Splash_Permission_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__permission_);
        check();
    }


    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private final int PERMISSION = 1002;
    private static final int REQUEST_ENABLE_BT = 3;
    private boolean isPermission = true;

    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        for (int x : grantResults) {
            if (requestCode == PERMISSION && x != PackageManager.PERMISSION_GRANTED) {
                isPermission = false;
            }
        }

        if (isPermission) {
            //수정
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            new AlertDialog.Builder(Splash_Permission_Activity.this)
                    .setTitle("NOTICE")
                    .setMessage("앱 사용을 위해서 [설정 -> 권한] 허용 후 앱을 다시 시작해주세요")
                    .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
    }

    public void check() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // 장치가 블루투스 지원하지 않는 경우
            Toast.makeText(this.getApplicationContext(), "해당 모바일은 블루투스를 지원하지 않음", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // 장치가 블루투스 지원하는 경우
            if (!mBluetoothAdapter.isEnabled()) {
                // 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요첨
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    //권한요청
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
                } else {

                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        check();
    }
}
