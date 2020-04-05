package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/15 8:50 PM
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class, filter = "tracing")
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    @Resource
    private CinemaTMapper cinemaTMapper;

    @Resource
    private AreaDictTMapper areaDictTMapper;

    @Resource
    private BrandDictTMapper brandDictTMapper;

    @Resource
    private HallDictTMapper hallDictTMapper;

    @Resource
    private HallFilmInfoTMapper hallFilmInfoTMapper;

    @Resource
    private FieldTMapper fieldTMapper;

    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {

        List<CinemaVO> cinemas = new ArrayList<>();

        Page<CinemaT> page = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        EntityWrapper<CinemaT> entityWrapper = new EntityWrapper<>();
        if (cinemaQueryVO.getBrandId() != 99) {
            entityWrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getDistrictId() != 99) {
            entityWrapper.eq("area_id", cinemaQueryVO.getDistrictId());
        }
        if (cinemaQueryVO.getHallId() != 99) {
            entityWrapper.like("hall_ids", "%#+" + cinemaQueryVO.getHallId() + "+#%");
        }
        List<CinemaT> cinemaTs = cinemaTMapper.selectPage(page, entityWrapper);
        for (CinemaT cinemaT : cinemaTs) {
            CinemaVO cinemaVO = new CinemaVO();

            cinemaVO.setUuid(cinemaT.getUuid() + "");
            cinemaVO.setMinimumPrice(cinemaT.getMinimumPrice() + "");
            cinemaVO.setAddress(cinemaT.getCinemaAddress());
            cinemaVO.setCinemaName(cinemaT.getCinemaName());
            cinemas.add(cinemaVO);
        }
        long counts = cinemaTMapper.selectCount(entityWrapper);
        Page<CinemaVO> result = new Page<>();
        result.setRecords(cinemas);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(counts);
        return result;
    }

    @Override
    public List<BrandVO> getBrands(Integer brandId) {

        List<BrandVO> brandVOS = new ArrayList<>();
        boolean flag = false;
        BrandDictT brandDictT = brandDictTMapper.selectById(brandId);
        if (brandId == 99 || brandDictT == null || brandDictT.getUuid() == null) {
            flag = true;
        }

        List<BrandDictT> brandDictTS = brandDictTMapper.selectList(null);
        for (BrandDictT brand : brandDictTS) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandName(brand.getShowName());
            brandVO.setBrandId(brand.getUuid() + "");
            if (flag) {
                if (brand.getUuid() == 99) {
                    brandVO.setActive(true);
                } else {
                    brandVO.setActive(false);
                }
            } else {
                if (brand.getUuid() == brandId) {
                    brandVO.setActive(false);
                } else {
                    brandVO.setActive(false);
                }
            }
            brandVOS.add(brandVO);
        }
        return brandVOS;
    }

    @Override
    public List<AreaVO> getAreas(Integer areaId) {

        List<AreaVO> areaVOS = new ArrayList<>();
        boolean flag = false;
        AreaDictT areaDictT = areaDictTMapper.selectById(areaId);
        if (areaId == 99 || areaDictT == null || areaDictT.getUuid() == null) {
            flag = true;
        }

        List<AreaDictT> areaDictTS = areaDictTMapper.selectList(null);
        for (AreaDictT area : areaDictTS) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(area.getShowName());
            areaVO.setAreaId(area.getUuid() + "");
            if (flag) {
                if (area.getUuid() == 99) {
                    areaVO.setActive(true);
                } else {
                    areaVO.setActive(false);
                }
            } else {
                if (area.getUuid() == areaId) {
                    areaVO.setActive(false);
                } else {
                    areaVO.setActive(false);
                }
            }
            areaVOS.add(areaVO);
        }
        return areaVOS;
    }

    @Override
    public List<HallTypeVO> getHallTypes(Integer hallType) {

        List<HallTypeVO> hallTypeVOS = new ArrayList<>();
        boolean flag = false;
        HallDictT hallDictT = hallDictTMapper.selectById(hallType);
        if (hallType == 99 || hallDictT == null || hallDictT.getUuid() == null) {
            flag = true;
        }

        List<HallDictT> hallDictTS = hallDictTMapper.selectList(null);
        for (HallDictT hall : hallDictTS) {
            HallTypeVO hallVO = new HallTypeVO();
            hallVO.setHalltypeName(hall.getShowName());
            hallVO.setHalltypeId(hall.getUuid() + "");
            if (flag) {
                if (hall.getUuid() == 99) {
                    hallVO.setActive(true);
                } else {
                    hallVO.setActive(false);
                }
            } else {
                if (hall.getUuid() == hallType) {
                    hallVO.setActive(false);
                } else {
                    hallVO.setActive(false);
                }
            }
            hallTypeVOS.add(hallVO);
        }
        return hallTypeVOS;
    }

    @Override
    public CinemaInfoVO getCinemaInfoById(Integer cinemaId) {
        CinemaT cinemaT = cinemaTMapper.selectById(cinemaId);
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        cinemaInfoVO.setCinemaAddress(cinemaT.getCinemaAddress());
        cinemaInfoVO.setImgUrl(cinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(cinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaId(cinemaT.getUuid() + "");
        cinemaInfoVO.setCinemaName(cinemaT.getCinemaName());
        return cinemaInfoVO;
    }

    @Override
    public List<FilmInfoVO> getFilmInfoByCinemaId(Integer cinemaId) {
        List<FilmInfoVO> filmInfos = fieldTMapper.getFilmInfos(cinemaId);
        return filmInfos;
    }

    @Override
    public HallInfoVO getFilmFieldInfo(Integer fieldId) {
        HallInfoVO hallInfoVO = fieldTMapper.getHallInfo(fieldId);
        return hallInfoVO;
    }

    @Override
    public FilmInfoVO getFilmInfoByFieldId(Integer fieldId) {
        FilmInfoVO filmInfoVO = fieldTMapper.getFilmInfoById(fieldId);
        return filmInfoVO;
    }

    @Override
    public OrderQueryVO getOrderNeeds(Integer fieldId) {
        OrderQueryVO orderQueryVO = new OrderQueryVO();
        FieldT fieldT = fieldTMapper.selectById(fieldId);
        orderQueryVO.setCinemaId(fieldT.getCinemaId() + "");
        orderQueryVO.setFilmPrice(fieldT.getPrice() + "");
        return orderQueryVO;
    }
}
