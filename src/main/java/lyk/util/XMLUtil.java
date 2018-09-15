package lyk.util;

import java.lang.reflect.Field;
/*import java.lang.reflect.Method;*/
import java.util.Iterator;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import lyk.po.ReceiveXmlPojo;

public class XMLUtil {

	public static String streamToString(HttpServletRequest request) {
		String Data = null;
		int count = 0;
		try (ServletInputStream sis = request.getInputStream();) {
			int size = request.getContentLength();
			byte[] buffer = new byte[size];
			count = sis.read(buffer);
			Data = new String(buffer, 0, count, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return Data;
	}

	public static Object getMsg(HttpServletRequest request, String className) {
		Object msg = null;
		String xml = streamToString(request);
		try {
			if (xml.length() <= 0 || xml == null)
				return null;
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();// 得到根节�?
			Iterator<?> iter = root.elementIterator();// 调用迭代�?
			Class<?> c = Class.forName(className);
			msg = c.newInstance();
			while (iter.hasNext()) {
				Element ele = (Element) iter.next();
				Field field = c.getDeclaredField(ele.getName());
				/*
				 * Method method = c.getDeclaredMethod("set" + ele.getName(),
				 * field.getType()); method.invoke(msg, ele.getText());
				 */
				field.setAccessible(true);
				field.set(msg, ele.getData());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return msg;
	}

	public static String textToXml(ReceiveXmlPojo object) {
		StringBuffer stf = new StringBuffer("<xml>  <ToUserName>");
		stf.append(object.getFromUserName()).append("</ToUserName>  <FromUserName>");
		stf.append(object.getToUserName()).append("</FromUserName>  <CreateTime>");// 注意空格
		stf.append(object.getCreateTime()).append("</CreateTime>  <MsgType>text</MsgType>  <Content>");
		stf.append(object.getContent()).append("</Content>  <MsgId>");// 注意空格
		stf.append(object.getMsgId()).append("</MsgId>  </xml>");
		return stf.toString();
	}

	public static String imageToXml(ReceiveXmlPojo object) {
		StringBuffer stf = new StringBuffer("<xml> <ToUserName>");
		stf.append(object.getFromUserName()).append("</ToUserName> <FromUserName>");
		stf.append(object.getToUserName()).append("</FromUserName> <CreateTime>");
		stf.append(object.getCreateTime()).append("</CreateTime> <MsgType>image</MsgType> <PicUrl>");
		stf.append(object.getPicUrl()).append("</PicUrl> <MediaId>");
		stf.append(object.getMediaId()).append("</MediaId> <MsgId>");
		stf.append(object.getMediaId()).append("</MsgId> </xml>");
		return stf.toString();
	}

	// <xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName><
	// ![CDATA[fromUser] ]></FromUserName> <CreateTime>1348831860</CreateTime>
	// <MsgType>< ![CDATA[image] ]></MsgType> <PicUrl>< ![CDATA[this is a url]
	// ]></PicUrl> <MediaId>< ![CDATA[media_id] ]></MediaId>
	// <MsgId>1234567890123456</MsgId> </xml>
	public static String imagePo2Xml(ReceiveXmlPojo object) {
		StringBuffer stf = new StringBuffer("<xml><ToUserName>");
		stf.append(object.getFromUserName()).append("</ToUserName><FromUserName>");
		stf.append(object.getToUserName()).append("</FromUserName><CreateTime>");
		stf.append(object.getCreateTime()).append("</CreateTime><MsgType>voice</MsgType><Image><MediaId>");
		stf.append(object.getMediaId()).append("</MediaId> </Image></xml>");
		return stf.toString();
	}
}
