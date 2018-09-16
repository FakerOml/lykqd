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
		
		if (pojo.getMsgType().equals("text") && pojo.getContent().contains("��")) {
			pojo.setContent("����5�������������ѧ�Ź��Ž��а󶨣�");
			System.out.println(pojo.getFromUserName() + " " + pojo.getCreateTime());
			System.out.println(talkMapper.insertTalk(pojo.getFromUserName(), pojo.getCreateTime(), "300", "��"));
			return XMLUtil.textToXml(pojo);
		} else if (pojo.getMsgType()
				.equals("text") /* && pojo.getContent().contains("���") */) {
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
			if ("��".equals(talk.getType()))
				return dealBinding(pojo);
			if ("���".equals(talk.getType()))
				return dealLeave(pojo);
		} finally {
			try {
				talkMapper.deleteTalk(pojo.getFromUserName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("�˶Ի�û�д���ʽ");
		return null;
	}

	public String dealBinding(ReceiveXmlPojo pojo) {
		boolean tmp = true;
		Student student = null;
		Teacher teacher = null;
		if (tmp && pojo.getContent().length() != 9) {
			pojo.setContent("��ȷ����ȷѧ�Ż��߹���λ�����ٴδ����󶨰�ť���а󶨣�");
			tmp = false;
		}

		if (tmp && pojo.getContent().startsWith("s")) {
			if ((student = studentMapper.selectStudent(pojo.getContent())) != null) {
				try {
					talkMapper.insertOpenid(pojo.getFromUserName(), student.getId());
					pojo.setContent(student.getName() + "ͬѧ�Ѱ󶨳ɹ���");
				} catch (Exception e) {
					System.err.println("AOP����ʧ�ܣ�");
				}
			} else {
				pojo.setContent("ѧ��" + pojo.getContent() + "δ�����ݿ�ע�ᣡ����ϵ���񴦣�");
			}
			tmp = false;
		}
		if (tmp && pojo.getContent().startsWith("t")) {
			if ((teacher = teacherMapper.selectTeacher(pojo.getContent())) != null) {
				try {
					talkMapper.insertOpenid(pojo.getFromUserName(), teacher.getId());
					pojo.setContent(teacher.getName() + "��ʦ�Ѱ󶨳ɹ���");
				} catch (Exception e) {
					System.err.println("AOP����ʧ�ܣ�");
				}
			} else {
				pojo.setContent("����" + pojo.getContent() + "δ�����ݿ�ע�ᣡ����ϵ���񴦣�");
			}
			tmp = false;
		}
		if (tmp) {
			pojo.setContent("��ȷ����ȷѧ�Ż��߹�����Ϣ���ٴδ����󶨰�ť���а󶨣�");
		}
		return XMLUtil.textToXml(pojo);
	}

	public String dealLeave(ReceiveXmlPojo pojo) {
		return null;
	}

}
