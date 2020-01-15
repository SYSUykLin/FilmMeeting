package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 3:28 PM
 */
@Data
public class HallTypeVO implements Serializable {
    private String halltypeId;
    private String halltypeName;
    private boolean isActive;
}
