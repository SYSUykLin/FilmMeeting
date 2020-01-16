package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.AreaVO;
import com.stylefeng.guns.api.cinema.vo.BrandVO;
import com.stylefeng.guns.api.cinema.vo.HallTypeVO;
import lombok.Data;

import java.awt.geom.Area;
import java.util.*;
/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/16 4:51 PM
 */
@Data
public class CinemaConditionResponseVO {
    private List<BrandVO> brandList;
    private List<AreaVO> areaList;
    private List<HallTypeVO> halltypeList;
}
