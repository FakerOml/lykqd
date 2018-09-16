package lyk.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import lyk.mapper.StudentMapper;
import lyk.mapper.TalkMapper;
import lyk.mapper.TeacherMapper;
import lyk.po.ReceiveXmlPojo;
import lyk.po.Student;
import lyk.po.Talk;
import lyk.po.Teacher;
import lyk.service.IODealCenter;
import lyk.util.XMLUtil;

@Service
public class DealCenter implements IODealCenter {

	@Resource
	TalkMapper talkMapper;
	@Resource
	TeacherMapper teacherMapper;
	@Resource
	StudentMapper studentMapper;
	public static final String SENDNAME = "WxMsg";
	@Override
	public String dealAll(ReceiveXmlPojo pojo) {
		// TODO Auto-generated method stub
		
		if (pojo.getMsgType().equals("text") && pojo.getContent().contains("绑定")) {
			pojo.setContent("请在5分钟内输入你的学号工号进行绑定！");
			System.out.println(pojo.getFromUserName() + " " + pojo.getCreateTime());
			System.out.println(talkMapper.insertTalk(pojo.getFromUserName(), pojo.getCreateTime(), "300", "绑定"));
			return XMLUtil.textToXml(pojo);
		} else if (pojo.getMsgType()
				.equals("text") /* && pojo.getContent().contains("你好") */) {
			long time = new Date().getTime();
			long createtime = Long.parseLong(pojo.getCreateTime()) * 1000L;
			long times = createtime + 5 * 60 * 1000L;
			System.out.println(createtime);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(format.format(new Date(time)));
			System.out.println(format.format(new Date(createtime)));
			System.out.println(format.format(new Date(times)));
			return XMLUtil.textToXml(pojo);
		}
		return null;
	}

	@Override
	public String dealTalk(ReceiveXmlPojo pojo, Talk talk) {
		// TODO Auto-generated method stub
		try {
			if ("绑定".equals(talk.getType()))
				return dealBinding(pojo);
			if ("请假".equals(talk.getType()))
				return dealLeave(pojo);
		} finally {
			try {
				talkMapper.deleteTalk(pojo.getFromUserName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("此对话没有处理方式");
		return null;
	}

	public String dealBinding(ReceiveXmlPojo pojo) {
		boolean tmp = true;
		Student student = null;
		Teacher teacher = null;
		if (tmp && pojo.getContent().length() != 9) {
			pojo.setContent("请确认正确学号或者工号位数，再次触发绑定按钮进行绑定！");
			tmp = false;
		}

		if (tmp && pojo.getContent().startsWith("s")) {
			if ((student = studentMapper.selectStudent(pojo.getContent())) != null) {
				try {
					talkMapper.insertOpenid(pojo.getFromUserName(), student.getId());
					pojo.setContent(student.getName() + "同学已绑定成功！");
				} catch (Exception e) {
					System.err.println("AOP拦截失败！");
				}
			} else {
				pojo.setContent("学号" + pojo.getContent() + "未在数据库注册！请联系教务处！");
			}
			tmp = false;
		}
		if (tmp && pojo.getContent().startsWith("t")) {
			if ((teacher = teacherMapper.selectTeacher(pojo.getContent())) != null) {
				try {
					talkMapper.insertOpenid(pojo.getFromUserName(), teacher.getId());
					pojo.setContent(teacher.getName() + "老师已绑定成功！");
				} catch (Exception e) {
					System.err.println("AOP拦截失败！");
				}
			} else {
				pojo.setContent("工号" + pojo.getContent() + "未在数据库注册！请联系教务处！");
			}
			tmp = false;
		}
		if (tmp) {
			pojo.setContent("请确认正确学号或者工号信息，再次触发绑定按钮进行绑定！");
		}
		return XMLUtil.textToXml(pojo);
	}

	public String dealLeave(ReceiveXmlPojo pojo) {
		return null;
	}

}
