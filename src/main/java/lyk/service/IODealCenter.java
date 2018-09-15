package lyk.service;

import lyk.po.ReceiveXmlPojo;
import lyk.po.Talk;


public interface IODealCenter {

	String dealAll(ReceiveXmlPojo pojo);
	
	String dealTalk(ReceiveXmlPojo pojo,Talk talk);

}
