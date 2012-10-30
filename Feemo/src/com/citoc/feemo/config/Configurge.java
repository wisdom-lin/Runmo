package com.citoc.feemo.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 程序的所有配置，用于初始化
 * @author Linxiaozhi
 *
 */
public class Configurge {

	public static final String FILE_NAME = "config/config.properties";

	//对应了config/config.properties内的配置
	/**
	 * 滤值
	 */
	public static final String FILTER_VAULE = "filterValue";

	static Properties prop = new Properties();
	/**
	 * 配置文件是否改过
	 */
	static boolean change = false;

	public static void open() {
		try {
			prop.load(new FileInputStream("config/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		if (!change)
			return;
		try {
			prop.store(new FileOutputStream(FILE_NAME), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return prop.getProperty(key, defaultValue);
	}

	public static Object setProperty(String key, String value) {
		change = true;
		return prop.put(key, value);
	}

//	public static void main(String[] args) {
//		Configurge.open();
//		String string = Configurge.getProperty(Configurge.FILTER_VAULE);
//		System.out.println(string);
//		int value = Integer.parseInt(string.trim());
//		System.out.println(value);
//		Configurge.close();
//	}
}
