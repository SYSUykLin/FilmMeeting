package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/18 9:44 PM
 */
public class UUIDUtil {
    public static String genUuid(){
        return UUID.randomUUID().toString();
    }
}
