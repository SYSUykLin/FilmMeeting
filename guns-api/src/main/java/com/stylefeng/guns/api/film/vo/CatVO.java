package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 7:03 PM
 */
@Data
public class CatVO implements Serializable {
    private String catId;
    private String catName;
    private boolean isActive;
}
