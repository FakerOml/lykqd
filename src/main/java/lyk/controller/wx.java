package lyk.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lyk.po.AccessToken;
import lyk.util.ConfigUtil;
import lyk.util.HttpClientUtil;
import lyk.po.ReceiveXmlPojo;
import lyk.service.IODealCenter;
import lyk.util.XMLUtil;
import net.sf.json.JSONObject;

@Controller
public class wx {
	@Resource
	IODealCenter dc;
	static TokenThread thread;
	public static final String SENDNAME = "WxMsg";

	static {
		thread = new TokenThread();
		thread.start();
	}

	@RequestMapping(value="/wx.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("echostr") != null && request.getParameter("echostr") != "")
			return request.getParameter("echostr");
		ReceiveXmlPojo pojo = (ReceiveXmlPojo) XMLUtil.getMsg(request, "lyk.po.ReceiveXmlPojo");
		return dc.dealAll(pojo);
	}
}

class TokenThread extends Thread {
	public static String appId = ConfigUtil.getAppId();

	public static String appSecret = ConfigUtil.getAppSecret();
	
	public static AccessToken accessToken = null;

	public void run() {
		while (true) {
			try {
				accessToken = this.catchAccessToken();
				if (accessToken!=null) {
					Thread.sleep(7000 * 1000); 
				} else {
					Thread.sleep(1000 * 3);
				}
			} catch (Exception e) {
				System.out.println("∑¢…˙“Ï≥££∫" + e.getMessage());
				e.printStackTrace();
				try {
					Thread.sleep(1000 * 10);
				} catch (Exception e1) {

				}
			}
		}
	}

	
	private AccessToken catchAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.appId
				+ "&secret=" + this.appSecret;
		String result = HttpClientUtil.httpGetMethod(url);
		JSONObject jsonobject = JSONObject.fromObject(result);
		AccessToken token = (AccessToken) JSONObject.toBean(jsonobject, AccessToken.class);
		System.out.println(token.toString());
		return token;
	}

	public static AccessToken getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(AccessToken accessToken) {
		TokenThread.accessToken = accessToken;
	}

}