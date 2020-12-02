package com.xz.fucklepao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.xz.fucklepao.utils.PreferencesUtilV2;

public class MainActivity extends Activity {

    private CheckBox cbIsStart;
    private CheckBox cbIsBaoli;
    private Context mContext;
    private SensorManager sensorManager;
    private EditText edText;
    private TextView tv;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            tv.setText("x轴方向的重力加速度" + x + "\ny轴方向的重力加速度" + y + "\nz轴方向的重力加速度" + z);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        cbIsStart = findViewById(R.id.checkBox_keep_run);
        edText = findViewById(R.id.editText1);
        cbIsBaoli = findViewById(R.id.checkBox_mode_change);
        tv = findViewById(R.id.textView1);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        cbIsBaoli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBaoli(isChecked);
            }
        });
        cbIsStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log(isChecked ? "模拟运动已开启" : "模拟运动已关闭");
                setStart(isChecked);
            }
        });

    }

    private void getState() {
        Local.isStart = PreferencesUtilV2.getBoolean(Local.SHARED_START, false);
        Local.isBaoli = PreferencesUtilV2.getBoolean(Local.SHARED_BAOLI, false);
        Local.time = PreferencesUtilV2.getInt(Local.SHARED_TIME, 100);
        cbIsStart.setChecked(Local.isStart);
        cbIsBaoli.setChecked(Local.isBaoli);
        edText.setText(String.valueOf(Local.time));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);
        }

        getState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    public void savetime(View v) {
        PreferencesUtilV2.putInt(Local.SHARED_TIME, Integer.parseInt(edText.getText().toString().trim()));
        Local.time = Integer.parseInt(edText.getText().toString().trim());
    }

    public void setStart(Boolean isStart) {
        PreferencesUtilV2.putBoolean(Local.SHARED_START, isStart);
        Local.isStart = isStart;
    }

    public void setBaoli(Boolean isBaoli) {
        PreferencesUtilV2.putBoolean(Local.SHARED_BAOLI, isBaoli);
        Local.isBaoli = isBaoli;
    }


    public void log(String paramString) {
        Log.d("xz", "log: " + paramString);
        //        Toast.makeText(MainActivity.this, paramString, Toast.LENGTH_SHORT).show();
    }

}
