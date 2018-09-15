package lyk.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigUtil {
	private static String appId;
	private static String appSecret;
	private static String host;
	private static String username;
	private static String password;

	static {
		try {
			String path = ConfigUtil.class.getClassLoader().getResource("").toString().substring(6);
			System.out.println(path);
			path = path + "lyk/config/AppConfig.properties";
			System.out.println(path);
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(path)));
			appId = properties.getProperty("appId");
			appSecret = properties.getProperty("appSecret");
			host = properties.getProperty("host");
			username = properties.getProperty("username");
			password = properties.getProperty("password");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getAppId() {
		return appId;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static String getHost() {
		return host;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return password;
	}

}
