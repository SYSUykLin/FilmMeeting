package com.stylefeng.guns.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GunsRestApplicationTests {
	@Resource
	private FilmTMapper filmTMapper;

	@Test
	public void contextLoads() {
		System.out.println(filmTMapper.getFilmDetailById("2"));
	}

}
