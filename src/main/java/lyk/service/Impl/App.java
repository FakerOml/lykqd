package lyk.service.Impl;

import org.springframework.stereotype.Component;



@Component
public class App {
	public String test(String str) {
		// TODO Auto-generated method stub
		if (str == null)
			System.out.println("���޸Ĳ���");
		return str;
	}
}

