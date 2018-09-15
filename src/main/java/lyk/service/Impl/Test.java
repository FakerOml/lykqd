package lyk.service.Impl;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lyk.mapper.TalkMapper;

@Component
public class Test {

	@Resource
	TalkMapper talkMapper;
	public void test(){
		System.out.println(talkMapper.insertTalk("123", "132", "546", "132"));
	}
}
