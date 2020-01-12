package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 7:05 PM
 */
@Data
public class SourceVO implements Serializable {
    private String sourceId;
    private String sourceName;
    private boolean isActive;
}
