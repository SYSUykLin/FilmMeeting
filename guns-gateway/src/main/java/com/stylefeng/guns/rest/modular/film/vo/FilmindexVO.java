package com.stylefeng.guns.rest.modular.film.vo;

import com.stylefeng.guns.api.user.film.vo.BannerVO;
import com.stylefeng.guns.api.user.film.vo.FilmInfo;
import com.stylefeng.guns.api.user.film.vo.FilmVO;
import com.sun.tools.javac.util.List;
import lombok.Data;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 10:16 PM
 */
@Data
public class FilmindexVO {
    private List<BannerVO> banners;
    private FilmVO hotFilms;
    private FilmVO soonFilms;
    private List<FilmInfo> boxRanking;
    private List<FilmInfo> expectRanking;
    private List<FilmInfo> top100;
}
