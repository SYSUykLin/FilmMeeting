package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 11:35 PM
 */
@Data
public class FilmVO implements Serializable {
    private int filmNum;
    private int nowPage;
    private int totalPage;
    private List<FilmInfo> filmInfo;
}
