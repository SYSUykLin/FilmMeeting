package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 7:06 PM
 */
@Data
public class YearVO implements Serializable {
    private String yearId;
    private String yearName;
    private boolean isActive;
}
