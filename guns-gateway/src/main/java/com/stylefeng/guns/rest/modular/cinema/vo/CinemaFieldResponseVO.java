package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import lombok.Data;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/16 5:29 PM
 */
@Data
public class CinemaFieldResponseVO {
    private CinemaInfoVO cinemaInfo;
    private FilmInfoVO filmInfo;
    private HallInfoVO hallInfo;
}
