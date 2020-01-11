package com.stylefeng.guns.api.user.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 10:17 PM
 */
@Data
public class BannerVO implements Serializable {
    private String bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
