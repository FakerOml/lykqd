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
		// 绑定的进行会话判断
		if (id != null) {
			/* return joinPoint.proceed(object); */
			// 没有会话或者会话超时的放行
			if (talk == null || Integer.valueOf(talk.getCreateTime()) + Integer.valueOf(talk.getKeepTime()) < Integer
					.valueOf(pojo.getCreateTime())) {
				try {
					// 删除超时对话
					talkMapper.deleteTalk(pojo.getFromUserName());
				} catch (Exception e) {
				}
				if("绑定".equals(pojo.getContent()))
				{
					pojo.setContent("你的微信号已经被："+id.charAt(0)+"****"+id.substring(5)+"绑定，请勿重复绑定哦。如果需要更换绑定，请去教务处联系更换！");
					return XMLUtil.textToXml(pojo);
				}
				return joinPoint.proceed(object);
			}
			// 有会话的调用会话处理函数
			else {
				result = ioDealCenter.dealTalk(pojo, talk);
				return result;
			}
		}
		// 没有绑定但是申请绑定对话的放行,无需判断是否在会话中，在处理函数处会处理
		if (id == null && "绑定".equals(pojo.getContent()))
			return joinPoint.proceed(object);
		// 微信号未绑定，但是存在绑定对话，调用会话处理函数
		if (id == null && talk != null && "绑定".equals(talk.getType())) {
			result = ioDealCenter.dealTalk(pojo, talk);
			return result;
		}
		// 拒绝访问
		pojo.setContent("请输入“绑定”进行绑定操作，否则无法正常使用公众号功能!");
		return XMLUtil.textToXml(pojo);
	}
}
