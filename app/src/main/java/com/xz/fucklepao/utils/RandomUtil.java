package com.xz.fucklepao.utils;

import java.util.Random;

/**
 * @author czr
 * @email czr2001@outlook.com
 * @date 2020/11/29
 */
public class RandomUtil {

    private Random mRandom;
    private static boolean bx, by, bz;
    private static int maxStep = 30;

    public RandomUtil() {
        mRandom = new Random();
    }

    public RandomUtil(int maxStep) {
        RandomUtil.maxStep = maxStep;
        mRandom = new Random();
    }

    public float getRandomX() {
        if (bx) {
            bx = false;
            return mRandom.nextInt(maxStep) + mRandom.nextFloat();
        } else {
            bx = true;
            return -(mRandom.nextInt(maxStep) + mRandom.nextFloat());
        }
    }

    public float getRandomY() {
        if (by) {
            by = false;
            return mRandom.nextInt(maxStep) + mRandom.nextFloat();
        } else {
            by = true;
            return -(mRandom.nextInt(maxStep) + mRandom.nextFloat());
        }
    }

    public float getRandomZ() {
        if (bz) {
            bz = false;
            return mRandom.nextInt(maxStep) + mRandom.nextFloat();
        } else {
            bz = true;
            return -(mRandom.nextInt(maxStep) + mRandom.nextFloat());
        }
    }

}
