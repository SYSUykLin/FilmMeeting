package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/18 10:03 PM
 */
@Data
public class OrderQueryVO implements Serializable {
    private String cinemaId;
    private String filmPrice;
}
