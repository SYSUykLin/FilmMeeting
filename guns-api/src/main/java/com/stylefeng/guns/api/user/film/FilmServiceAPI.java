package com.stylefeng.guns.api.user.film;

import com.stylefeng.guns.api.user.film.vo.BannerVO;
import com.stylefeng.guns.api.user.film.vo.FilmInfo;
import com.stylefeng.guns.api.user.film.vo.FilmVO;
import java.util.*;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:05 AM
 */
public interface FilmServiceAPI {
    //get banners info
    BannerVO getBanners();
    //get hot films
    FilmVO getHotFilms(boolean isLimit, Integer nums);
    //get films displayed soon
    FilmVO getSoonFilms(boolean isLimit, Integer nums);
    //get boxRanking
    List<FilmInfo> getBoxRanking();
    //population Ranking
    List<FilmInfo> expectRanking();
    //get top
    List<FilmInfo> getTop();

}
