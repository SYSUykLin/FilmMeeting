package com.stylefeng.guns.rest.common;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/10 11:17 AM
 */
public class CurrentUser {
    private static final InheritableThreadLocal<String> threadlocal = new InheritableThreadLocal<>();

    public static void saveUserId(String userId) {
        threadlocal.set(userId);
    }

    public static String getCurrentUser() {
        return threadlocal.get();
    }

}
