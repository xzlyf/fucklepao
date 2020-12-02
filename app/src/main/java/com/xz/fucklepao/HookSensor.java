package com.xz.fucklepao;

import android.util.Log;

import com.xz.fucklepao.utils.PreferencesUtilV2;
import com.xz.fucklepao.utils.RandomUtil;

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
    private RandomUtil randomUtil;
    private XSharedPreferences sharedPreferences;
    public void handleLoadPackage(final LoadPackageParam arg0) throws Throwable {
        Class<?> sensorEL = XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", arg0.classLoader);
        if (sensorEL == null) {
            throw new ClassNotFoundException(arg0.getClass().getName());
        }
        XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                //                ((float[]) param.args[1])[0] = randomUtil.getRandomX();
                //                ((float[]) param.args[1])[1] = randomUtil.getRandomY();
                //                ((float[]) param.args[1])[2] = randomUtil.getRandomZ();

            }

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws NoSuchFieldException, IllegalAccessException {
                //                ((float[]) param.args[1])[0] = randomUtil.getRandomX();
                //                ((float[]) param.args[1])[1] = randomUtil.getRandomY();
                //                ((float[]) param.args[1])[2] = randomUtil.getRandomZ();
                getState();
                Log.d("xz", "beforeHookedMethod: "+Local.isStart);
                if ( Local.isStart) {

                    if (Local.isBaoli) {
                        Log.d("xz", "beforeHookedMethod: A");
                        ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
                        ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
                        ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
                    } else {
                        Log.d("xz", "beforeHookedMethod: B");
                        ((float[]) param.args[1])[0] = randomUtil.getRandomX();
                        ((float[]) param.args[1])[1] = randomUtil.getRandomY();
                        ((float[]) param.args[1])[2] = randomUtil.getRandomZ();
                    }


//                    int handle = (Integer) param.args[0];
//                    Field field = param.thisObject.getClass().getDeclaredField("mSensorsEvents");
//                    field.setAccessible(true);
//                    Sensor ss = ((SensorEvent) ((SparseArray) field.get(param.thisObject)).get(handle)).sensor;
//                    if (ss != null) {
//                        int type = ss.getType();
//                        if (type != 11
//                                && type != 4
//                                && type != 3
//                                && type != 2
//                                && type != 5
//                                && type != 9
//                                && type != 8) {
//                            if (HookSensor.Count < 15) {
//                                XposedBridge.log("传感器：" + ss.toString());
//                                HookSensor.Count = HookSensor.Count + 1;
//                            }
//                        } else {
//                            return;
//                        }
//                    } else {
//                        throw new NullPointerException(arg0.getClass().getName());
//                    }
//                    m = Local.time;
//                    long currentUpdateTime1 = System.currentTimeMillis();
//                    //间隔震动频率
//                    if (currentUpdateTime1 - lastUpdateTime1 >= m) {
//                        long currentUpdateTime = System.currentTimeMillis();
//                        long timeInterval = currentUpdateTime - lastUpdateTime;
//                        if (Local.isBaoli) {
//                            ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
//                            ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
//                            ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
//                        } else {
//                            ((float[]) param.args[1])[0] = randomUtil.getRandomX();
//                            ((float[]) param.args[1])[1] = randomUtil.getRandomY();
//                            ((float[]) param.args[1])[2] = randomUtil.getRandomZ();
//                        }
//                        if (timeInterval >= ( HookSensor.UPTATE_INTERVAL_TIME)) {
//                            lastUpdateTime = currentUpdateTime;
//                            lastUpdateTime1 = currentUpdateTime1;
//                            if (Local.isBaoli) {
//                                ((float[]) param.args[1])[0] = 10.0f + (1000.0f * new Random().nextFloat());
//                                ((float[]) param.args[1])[2] = 10.0f + (1000.0f * new Random().nextFloat());
//                                ((float[]) param.args[1])[1] = 10.0f + (1000.0f * new Random().nextFloat());
//                            } else {
//                                ((float[]) param.args[1])[0] = randomUtil.getRandomX();
//                                ((float[]) param.args[1])[1] = randomUtil.getRandomY();
//                                ((float[]) param.args[1])[2] = randomUtil.getRandomZ();
//                            }
//                        }
//                    }
                }
            }
        });
    }


    private void getState() {
        sharedPreferences.reload();
        Local.isStart = sharedPreferences.getBoolean(Local.SHARED_START, false);
        Local.isBaoli = sharedPreferences.getBoolean(Local.SHARED_BAOLI, false);
        Local.time = sharedPreferences.getInt(Local.SHARED_TIME, 100);
    }

    public void initZygote(StartupParam arg0) {
        randomUtil = new RandomUtil(500);
        sharedPreferences = new XSharedPreferences("com.xz.fucklepao", "my_app");
        sharedPreferences.makeWorldReadable();
    }

}