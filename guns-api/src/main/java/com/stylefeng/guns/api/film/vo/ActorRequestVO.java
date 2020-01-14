package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/14 3:38 PM
 */
@Data
public class ActorRequestVO implements Serializable {
    private ActorVO director;
    private List<ActorVO> actors;

}
