package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/14 11:28 AM
 */
@Data
public class ActorVO implements Serializable {
    private String imgAddress;
    private String directorName;
    private String roleName;
}
