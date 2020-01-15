package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 12:02 PM
 */
@Data
public class CinemaQueryVO implements Serializable {
    private Integer brandId = 99;
    private Integer districtId = 99;
    private Integer hallId = 99;
    private Integer pageSize = 12;
    private Integer nowPage = 1;
}
