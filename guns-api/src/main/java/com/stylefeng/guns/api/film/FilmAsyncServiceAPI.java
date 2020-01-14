package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.ImgVO;

import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:05 AM
 */
public interface FilmAsyncServiceAPI {
    FilmDescVO getFilmDesc(String filmId);

    ImgVO getImgs(String filmId);

    /**
     * 导演信息
     *
     * @param uuid
     * @return
     */
    ActorVO getDectInfo(String filmId);

    List<ActorVO> getActors(String filmId);

}
