package lyk.mapper;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import lyk.mapper.TalkMapper;

@Component
class Talk{
	@Resource
	TalkMapper talkMapper;
	public void test(){
		System.out.println(talkMapper.insertTalk("125d3", "132", "546", "132"));
	}
}

public class Test {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("lyk/config/test.xml");
		Talk talk = (Talk) context.getBean("talk");
		System.out.println(talk);
		talk.test();
		
	}

}
