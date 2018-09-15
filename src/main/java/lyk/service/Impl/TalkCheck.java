package lyk.service.Impl;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lyk.mapper.TalkMapper;
import lyk.po.ReceiveXmlPojo;
import lyk.po.Talk;
import lyk.service.IODealCenter;
import lyk.util.XMLUtil;

@Component
@Aspect
public class TalkCheck {
	@Resource
	TalkMapper talkMapper;
	@Resource
	IODealCenter ioDealCenter;

	@Pointcut("execution(* lyk.service.IODealCenter.dealAll(..))")
	public void talkCheck() {

	}

	@Around("talkCheck()")
	public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] object = joinPoint.getArgs();
		String result = "";
		ReceiveXmlPojo pojo = (ReceiveXmlPojo) object[0];
		Talk talk = talkMapper.selectTalk(pojo.getFromUserName());
		String id = talkMapper.selectOpenid(pojo.getFromUserName());
		// �󶨵Ľ��лỰ�ж�
		if (id != null) {
			/* return joinPoint.proceed(object); */
			// û�лỰ���߻Ự��ʱ�ķ���
			if (talk == null || Integer.valueOf(talk.getCreateTime()) + Integer.valueOf(talk.getKeepTime()) < Integer
					.valueOf(pojo.getCreateTime())) {
				try {
					// ɾ����ʱ�Ի�
					talkMapper.deleteTalk(pojo.getFromUserName());
				} catch (Exception e) {
				}
				if("��".equals(pojo.getContent()))
				{
					pojo.setContent("���΢�ź��Ѿ�����"+id.charAt(0)+"****"+id.substring(5)+"�󶨣������ظ���Ŷ�������Ҫ�����󶨣���ȥ������ϵ������");
					return XMLUtil.textToXml(pojo);
				}
				return joinPoint.proceed(object);
			}
			// �лỰ�ĵ��ûỰ������
			else {
				result = ioDealCenter.dealTalk(pojo, talk);
				return result;
			}
		}
		// û�а󶨵�������󶨶Ի��ķ���,�����ж��Ƿ��ڻỰ�У��ڴ��������ᴦ��
		if (id == null && "��".equals(pojo.getContent()))
			return joinPoint.proceed(object);
		// ΢�ź�δ�󶨣����Ǵ��ڰ󶨶Ի������ûỰ������
		if (id == null && talk != null && "��".equals(talk.getType())) {
			result = ioDealCenter.dealTalk(pojo, talk);
			return result;
		}
		// �ܾ�����
		pojo.setContent("�����롰�󶨡����а󶨲����������޷�����ʹ�ù��ںŹ���!");
		return XMLUtil.textToXml(pojo);
	}
}
