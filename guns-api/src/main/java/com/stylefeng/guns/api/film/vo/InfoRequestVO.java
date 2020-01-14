package com.stylefeng.guns.api.film.vo;

import com.sun.source.doctree.SerialDataTree;
import lombok.Data;

import java.io.Serializable;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/14 3:43 PM
 */
@Data
public class InfoRequestVO implements Serializable {
    private String biography;
    private ActorRequestVO actors;
    private ImgVO imgVO;
    private String filmId;
}
