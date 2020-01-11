package com.stylefeng.guns.rest.modular.film;

import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/11 8:55 PM
 */
@RestController
@RequestMapping("/film/")
public class FilmController {

    @RequestMapping(value = "getIndex", method = RequestMethod.GET)
    public ResponseVO getIndex(){

        /**
         * banner信息
         * 正在热映影片
         * 即将上映
         * 票房排行
         * 人气榜单
         * 前100
         */

        return null;
    }



}
