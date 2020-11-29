package com.xz.fucklepao;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.SystemClock;
import android.util.SparseArray;

import com.xz.fucklepao.utils.RandomUtil;

import java.lang.reflect.Field;
import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookSensor implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private static int Count = 1;
    private static int UPTATE_INTERVAL_TIME = 200;
    private static int max = Integer.MAX_VALUE;
    final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]);
    private long lastUpdateTime;
    private long lastUpdateTime1;
    private int m = 10;
    private XSharedPreferences sharedPreferences;
    private RandomUtil randomUtil;

    public void handleLoadPackage(final LoadPackageParam arg0) throws Throwable {
        Class<?> sensorEL = XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", arg0.classLoader);
        if (sensorEL == null) {
            throw new ClassNotFoundException(arg0.getClass().getName());
        }
        XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {

                 ((float[]) param.args[1])[0] = randomUtil.getRandomX();
                 ((float[]) param.args[1])[1] = randomUtil.getRandomY();
                 ((float[]) param.args[1])[2] = randomUtil.getRandomZ();

//                try {
//                    sharedPreferences.reload();
//                    if ((!sharedPreferences.getBoolean("isSys", true) || arg0.appInfo.flags != 1)
//                            && sharedPreferences.getBoolean("isStart", false)) {
//                        int handle = (Integer) param.args[0];
//                        Field field = param.thisObject.getClass().getDeclaredField("mSensorsEvents");
//                        field.setAccessible(true);
//                        Sensor ss = ((SensorEvent) ((SparseArray) field.get(param.thisObject)).get(handle)).sensor;
//                        if (ss != null) {
//                            int type = ss.getType();
//                            if (type != 11
//                                    && type != 4
//                                    && type != 3
//                                    && type != 2
//                                    && type != 5
//                                    && type != 9
//                                    && type != 8) {
//                                if (HookSensor.Count < 15) {
//                                    XposedBridge.log("传感器：" + ss.toString());
//                                    HookSensor.Count = HookSensor.Count + 1;
//                                }
//                            } else {
//                                return;
//                            }
//                        } else {
//                            throw new NullPointerException(arg0.getClass().getName());
//                        }
//                        if (sharedPreferences.getBoolean("isLock", false)) {
//                            ((float[]) param.args[1])[0] = 0.0f;
//                            ((float[]) param.args[1])[2] = 0.0f;
//                            ((float[]) param.args[1])[1] = 0.0f;
//
//                            return;
//                        }
//                        m = Integer.parseInt(sharedPreferences.getString("Time", "100"));
//                        long currentUpdateTime1 = System.currentTimeMillis();
//                        //间隔震动频率
//                        if (currentUpdateTime1 - lastUpdateTime1 >= ((long) m)) {
//                            long currentUpdateTime = System.currentTimeMillis();
//                            long timeInterval = currentUpdateTime - lastUpdateTime;
//                            if (sharedPreferences.getBoolean("isBaoli", false)) {
//                                ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
//                                ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
//                                ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
//                            } else if (!arg0.packageName.equals("com.sina.weibo")) {
//                                ((float[]) param.args[1])[0] = 125.0f + (1200.0f * new Random().nextFloat());
//                            } else {
//                                ((float[]) param.args[1])[0] = 0.0f;
//                            }
//                            if (timeInterval >= ((long) HookSensor.UPTATE_INTERVAL_TIME)) {
//                                lastUpdateTime = currentUpdateTime;
//                                lastUpdateTime1 = currentUpdateTime1;
//                                if (sharedPreferences.getBoolean("isBaoli", false)) {
//                                    ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
//                                    ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
//                                    ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
//                                } else if (!arg0.packageName.equals("com.sina.weibo")) {
//                                    ((float[]) param.args[1])[0] = 125.0f + (1200.0f * new Random().nextFloat());
//                                } else {
//                                    ((float[]) param.args[1])[0] = (float) (1.0d + (20.0d * Math.random()));
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                }
            }
        });
    }


    public void initZygote(StartupParam arg0) throws Throwable {
        this.sharedPreferences = new XSharedPreferences("com.xz.fucklepao", "setting");
        this.sharedPreferences.makeWorldReadable();
        randomUtil = new RandomUtil();
    }

}