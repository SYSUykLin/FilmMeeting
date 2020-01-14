package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/14 11:06 AM
 */
@Data
public class FilmDescVO implements Serializable {
    private String biography;
    private String filmId;
}
