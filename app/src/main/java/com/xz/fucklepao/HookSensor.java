package com.xz.fucklepao;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.util.SparseArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.lang.reflect.Field;
import java.util.Random;

public class HookSensor implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private static int Count = 1;
    private static int UPTATE_INTERVAL_TIME = 200;
    private static int max = Integer.MAX_VALUE;
    final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0]);
    private long lastUpdateTime;
    private long lastUpdateTime1;
    private int m = 10;
    private XSharedPreferences sharedPreferences;

    public void handleLoadPackage(final LoadPackageParam arg0) throws Throwable {
        Class<?> sensorEL = XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", arg0.classLoader);
        Class<?> getSL = XposedHelpers.findClass("android.hardware.SensorManager", arg0.classLoader);
        if (sensorEL == null || getSL == null) {
            throw new ClassNotFoundException(arg0.getClass().getName());
        }
        try {
            XposedBridge.hookAllMethods(getSL, "getSensorList", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if ((Integer) param.args[0] == 1 || (Integer) param.args[0] == -1) {
                        param.setResult(null);
                        XposedBridge.log("获取传感器信息：" + param.getResult().toString());
                    } else {
                        XposedBridge.log("getSensorList：" + ((Integer) param.args[0]) + "获取到的传感器信息：" + param.getResult().toString());
                    }
                    afterHookedMethod(param);
                }
            });
            XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    try {
                        sharedPreferences.reload();
                        if ((!sharedPreferences.getBoolean("isSys", true) || arg0.appInfo.flags != 1)
                                && sharedPreferences.getBoolean("isStart", false)) {
                            int handle = (Integer) param.args[0];
                            Field field = param.thisObject.getClass().getDeclaredField("mSensorsEvents");
                            field.setAccessible(true);
                            SparseArray<SensorEvent> se = (SparseArray) field.get(param.thisObject);
                            if (se != null) {
                                Sensor ss = se.get(handle).sensor;
                                if (ss != null) {
                                    if (ss.getType() !=
                                            11 && ss.getType() !=
                                            4 && ss.getType() !=
                                            3 && ss.getType() != 2 && ss.getType() !=
                                            5 && ss.getType() != 9 && ss.getType() != 8) {
                                        if (HookSensor.Count < 15) {
                                            XposedBridge.log("传感器：" + ss.toString());
                                            HookSensor.Count = HookSensor.Count + 1;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                            if (sharedPreferences.getBoolean("isLock", false)) {
                                ((float[]) param.args[1])[0] = 0.0f;
                                ((float[]) param.args[1])[2] = 0.0f;
                                ((float[]) param.args[1])[1] = 0.0f;
                                return;
                            }
                            m = Integer.parseInt(sharedPreferences.getString("Time", "100"));
                            long currentUpdateTime1 = System.currentTimeMillis();
                            if (currentUpdateTime1 - lastUpdateTime1 >= ((long) m)) {
                                long currentUpdateTime = System.currentTimeMillis();
                                long timeInterval = currentUpdateTime - lastUpdateTime;
                                if (sharedPreferences.getBoolean("isBaoli", false)) {
                                    ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
                                    ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
                                    ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
                                } else if (!arg0.packageName.equals("com.sina.weibo")) {
                                    ((float[]) param.args[1])[0] = 125.0f + (1200.0f * new Random().nextFloat());
                                } else {
                                    ((float[]) param.args[1])[0] = 0.0f;
                                }
                                if (timeInterval >= ((long) HookSensor.UPTATE_INTERVAL_TIME)) {
                                    lastUpdateTime = currentUpdateTime;
                                    lastUpdateTime1 = currentUpdateTime1;
                                    if (sharedPreferences.getBoolean("isBaoli", false)) {
                                        ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
                                        ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
                                        ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
                                    } else if (!arg0.packageName.equals("com.sina.weibo")) {
                                        ((float[]) param.args[1])[0] = 125.0f + (1200.0f * new Random().nextFloat());
                                    } else {
                                        ((float[]) param.args[1])[0] = (float) (1.0d + (20.0d * Math.random()));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void initZygote(StartupParam arg0) throws Throwable {
        this.sharedPreferences = new XSharedPreferences("com.xz.fucklepao", "setting");
        this.sharedPreferences.makeWorldReadable();
    }
}