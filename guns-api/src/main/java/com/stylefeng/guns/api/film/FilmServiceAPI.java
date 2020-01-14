package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:05 AM
 */
public interface FilmServiceAPI {
    //get banners info
    List<BannerVO> getBanners();

    //get hot films
    FilmVO getHotFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId);

    //get films displayed soon
    FilmVO getSoonFilms(boolean isLimit, Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId);

    FilmVO getClassicFilms(Integer nums, Integer nowPage, Integer sortId, Integer sourceId, Integer yearId, Integer catId);

    //get boxRanking
    List<FilmInfo> getBoxRanking();

    //population Ranking
    List<FilmInfo> getExpectRanking();

    //get top
    List<FilmInfo> getTop();

    List<CatVO> getCats();

    List<SourceVO> getSources();

    List<YearVO> getYears();

    FilmDetailVO getFilmDetail(Integer searchType, String searchParam);

    FilmDescVO getFilmDesc(String filmId);

    ImgVO getImgs(String filmId);

    /**
     * 导演信息
     * @param uuid
     * @return
     */
    ActorVO getDectInfo(String filmId);

    List<ActorVO> getActors(String filmId);

}
