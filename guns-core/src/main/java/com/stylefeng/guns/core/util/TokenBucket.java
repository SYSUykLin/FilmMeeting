package com.stylefeng.guns.core.util;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/20 12:01 AM
 */
public class TokenBucket {
    private int bucketNums = 100;
    private int rate = 1;
    private int nowTokens = 0;
    private long timestamp = getNowTime();

    private long getNowTime() {
        return System.currentTimeMillis();
    }

    private int min(int tokens) {
        return Math.min(tokens, bucketNums);
    }

    public boolean getToken() {
        long nowTime = getNowTime();
        nowTokens = nowTokens + (int) ((nowTime - timestamp) * rate);
        nowTokens = min(nowTokens);
        setTimestamp(nowTime);
        System.out.println("当前令牌数：" + nowTokens);
        if (nowTokens < 1) {
            return false;
        } else {
            nowTokens--;
            return true;
        }
    }

    public int getBucketNums() {
        return bucketNums;
    }

    public void setBucketNums(int bucketNums) {
        this.bucketNums = bucketNums;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getNowTokens() {
        return nowTokens;
    }

    public void setNowTokens(int nowTokens) {
        this.nowTokens = nowTokens;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
