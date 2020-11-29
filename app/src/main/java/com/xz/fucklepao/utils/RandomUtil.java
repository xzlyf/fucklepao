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

    public RandomUtil() {
        mRandom = new Random();
    }

    private float getRandomX() {
        if (bx) {
            bx = false;
            return mRandom.nextInt(30) + mRandom.nextFloat();
        } else {
            bx = true;
            return -(mRandom.nextInt(30) + mRandom.nextFloat());
        }
    }

    private float getRandomY() {
        if (by) {
            by = false;
            return mRandom.nextInt(30) + mRandom.nextFloat();
        } else {
            by = true;
            return -(mRandom.nextInt(30) + mRandom.nextFloat());
        }
    }

    private float getRandomZ() {
        if (bz) {
            bz = false;
            return mRandom.nextInt(30) + mRandom.nextFloat();
        } else {
            bz = true;
            return -(mRandom.nextInt(30) + mRandom.nextFloat());
        }
    }

}
