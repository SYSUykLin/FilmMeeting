package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 11:46 AM
 */
public interface CinemaServiceAPI {
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    List<BrandVO> getBrands(Integer brandId);

    List<AreaVO> getAreas(Integer areaId);

    List<HallTypeVO> getHallTypes(Integer hallType);

    CinemaInfoVO getCinemaInfoById(Integer cinemaId);

    FilmInfoVO getFilmInfoByCinemaId(Integer cinemaId);

    FilmFieldVO getFilmFieldInfo(Integer fieldId);

    FilmInfoVO getFilmInfoByFieldId(Integer fieldId);
}
