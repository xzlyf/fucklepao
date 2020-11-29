package com.xz.fucklepao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    CheckBox cb_keep_run;
    CheckBox cb_keep_sys;
    CheckBox cb_lock;
    CheckBox cb_mode_baoli;
    CheckBox cb_move;
    Context mContext;
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
    private SensorManager sensorManager;
    EditText text;
    TextView tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        cb_keep_run = findViewById(R.id.checkBox_keep_run);
        cb_lock = findViewById(R.id.checkBox_lock);
        text = findViewById(R.id.editText1);
        cb_mode_baoli = findViewById(R.id.checkBox_mode_change);
        cb_keep_sys = findViewById(R.id.checkBox_keep_sys);
        cb_move = findViewById(R.id.checkBox_move);
        tv = findViewById(R.id.textView1);
        sensorManager = (SensorManager) getSystemService("sensor");
        cb_lock.setChecked(isLock());
        cb_keep_sys.setChecked(isSys());
        cb_keep_run.setChecked(isStart());
        cb_mode_baoli.setChecked(isBaoli());
        cb_move.setChecked(isMove());
        text.setText(getTime());
        cb_lock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setLock(isChecked);
            }
        });
        cb_move.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setMove(isChecked);
            }
        });
        cb_mode_baoli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBaoli(isChecked);
            }
        });
        cb_keep_sys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSys(isChecked);
            }
        });
        cb_keep_run.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log(isChecked ? "模拟运动已开启" : "模拟运动已关闭");
                setStart(isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    public void savetime(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putString("Time", text.getText().toString());
        editor.apply();
    }

    public Boolean isLock() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getBoolean("isLock", false);
    }

    public Boolean isStart() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getBoolean("isStart", false);
    }

    public Boolean isSys() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getBoolean("isSys", true);
    }

    public Boolean isMove() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getBoolean("isMove", true);
    }

    public String getTime() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getString("Time", "100");
    }

    public void setMove(Boolean isMove) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isMove", isMove);
        editor.commit();
    }

    public void setSys(Boolean isSys) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isSys", isSys);
        editor.commit();
    }

    public void setLock(Boolean isLock) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isLock", isLock);
        editor.commit();
    }

    public void setStart(Boolean isStart) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isStart", isStart);
        editor.commit();
    }

    public void setBaoli(Boolean isBaoli) {
        SharedPreferences.Editor editor = getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isBaoli", isBaoli);
        editor.commit();
    }

    public Boolean isBaoli() {
        return getSharedPreferences("setting", Context.MODE_PRIVATE).getBoolean("isBaoli", false);
    }

    public void log(String paramString) {
        Log.d("xz", "log: "+paramString);
//        Toast.makeText(MainActivity.this, paramString, Toast.LENGTH_SHORT).show();
    }

}
