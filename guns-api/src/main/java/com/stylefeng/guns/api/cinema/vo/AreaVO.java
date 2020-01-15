package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 3:26 PM
 */
@Data
public class AreaVO implements Serializable {
    private String AreaId;
    private String AreaName;
    private boolean isActive;
}
