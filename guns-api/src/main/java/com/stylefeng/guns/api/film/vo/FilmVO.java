package com.stylefeng.guns.api.film.vo;

import java.util.*;
import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 11:35 PM
 */
@Data
public class FilmVO implements Serializable {
    private Integer filmNum;
    private List<FilmInfo> filmInfo;
}
