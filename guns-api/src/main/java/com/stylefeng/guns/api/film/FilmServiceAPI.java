package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.*;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:05 AM
 */
public interface FilmServiceAPI {
    //get banners info
    List<BannerVO> getBanners();
    //get hot films
    FilmVO getHotFilms(boolean isLimit, Integer nums);
    //get films displayed soon
    FilmVO getSoonFilms(boolean isLimit, Integer nums);
    //get boxRanking
    List<FilmInfo> getBoxRanking();
    //population Ranking
    List<FilmInfo> getExpectRanking();
    //get top
    List<FilmInfo> getTop();

    List<CatVO> getCats();

    List<SourceVO> getSources();

    List<YearVO> getYears();

}
