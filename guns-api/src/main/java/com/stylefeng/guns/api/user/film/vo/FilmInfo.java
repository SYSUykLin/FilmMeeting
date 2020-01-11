package com.stylefeng.guns.api.user.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 11:37 PM
 */
@Data
public class FilmInfo implements Serializable {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private Integer expectNum;
    private String showTime;
    private Integer boxNum;
    private String score;
}
