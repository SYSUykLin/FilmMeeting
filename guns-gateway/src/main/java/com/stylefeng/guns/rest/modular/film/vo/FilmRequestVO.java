package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/13 11:13 AM
 */
@Data
public class FilmRequestVO {
    private Integer showType = 1;
    private Integer sortId = 1;
    private Integer sourceId = 99;
    private Integer catId = 99;
    private Integer yearId = 99;
    private Integer nowPage = 1;
    private Integer pageSize = 18;
}
