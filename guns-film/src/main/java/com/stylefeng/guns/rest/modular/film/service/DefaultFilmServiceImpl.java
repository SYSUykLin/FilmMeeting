package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.user.film.FilmServiceAPI;
import com.stylefeng.guns.api.user.film.vo.BannerVO;
import com.stylefeng.guns.api.user.film.vo.FilmInfo;
import com.stylefeng.guns.api.user.film.vo.FilmVO;
import com.stylefeng.guns.rest.common.persistence.dao.BannerTMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:42 AM
 */
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI {
    @Resource
    private BannerTMapper bannerTMapper;

    @Override
    public BannerVO getBanners() {
        return null;
    }

    @Override
    public FilmVO getHotFilms(boolean isLimit, Integer nums) {
        return null;
    }

    @Override
    public FilmVO getSoonFilms(boolean isLimit, Integer nums) {
        return null;
    }

    @Override
    public List<FilmInfo> getBoxRanking() {
        return null;
    }

    @Override
    public List<FilmInfo> expectRanking() {
        return null;
    }

    @Override
    public List<FilmInfo> getTop() {
        return null;
    }
}
