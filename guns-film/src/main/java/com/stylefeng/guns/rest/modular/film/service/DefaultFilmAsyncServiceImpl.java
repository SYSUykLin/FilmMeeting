package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.api.film.vo.FilmDescVO;
import com.stylefeng.guns.api.film.vo.ImgVO;
import com.stylefeng.guns.rest.common.persistence.dao.ActorTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.FilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.model.ActorT;
import com.stylefeng.guns.rest.common.persistence.model.FilmInfoT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/12 12:42 AM
 */
@Component
@Service(interfaceClass = FilmAsyncServiceAPI.class)
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceAPI {

    @Resource
    private FilmInfoTMapper filmInfoTMapper;
    @Resource
    private ActorTMapper actorTMapper;


    private FilmInfoT getFilmInfo(String filmId) {
        FilmInfoT filmInfoT = new FilmInfoT();
        filmInfoT.setFilmId(filmId);
        FilmInfoT filmInfoT1 = filmInfoTMapper.selectOne(filmInfoT);
        return filmInfoT1;
    }

    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        FilmDescVO filmDescVO = new FilmDescVO();
        filmDescVO.setBiography(filmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    @Override
    public ImgVO getImgs(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        String filmImgStr = filmInfoT.getFilmImgs();
        String[] filmImgs = filmImgStr.split(",");
        ImgVO imgVO = new ImgVO();
        imgVO.setMainImg(filmImgs[0]);
        imgVO.setImg01(filmImgs[1]);
        imgVO.setImg02(filmImgs[2]);
        imgVO.setImg03(filmImgs[3]);
        imgVO.setImg04(filmImgs[4]);
        return imgVO;
    }

    @Override
    public ActorVO getDectInfo(String filmId) {
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        Integer directId = filmInfoT.getDirectorId();
        ActorT actorT = actorTMapper.selectById(directId);
        ActorVO actorVO = new ActorVO();
        actorVO.setImgAddress(actorT.getActorImg());
        actorVO.setDirectorName(actorT.getActorName());
        return actorVO;
    }

    @Override
    public List<ActorVO> getActors(String filmId) {
        List<ActorVO> actors = actorTMapper.getActors(filmId);

        return actors;
    }
}
