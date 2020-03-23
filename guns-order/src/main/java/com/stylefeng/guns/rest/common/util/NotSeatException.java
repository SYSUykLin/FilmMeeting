package com.stylefeng.guns.rest.common.util;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/2/1 4:08 PM
 */
public class NotSeatException extends RuntimeException {
    private String e;
    public NotSeatException(String e){
        super(e);
        this.e = e;
    }
}
