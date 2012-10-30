package com.citoc.feemo.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

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

	/**
	 * 滤值的默认值
	 */
	public static final int FILTER_VALUE_VALUE = 0;

	static Properties prop = new Properties();

	static Set<String> keys = new HashSet<String>();

	/**
	 * 配置文件是否改过
	 */
	static boolean change = false;

	public static void open() {
		//如果没有config文件夹，就新建。用于初始化的时候
		File dir = new File("config");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		//如果没有这个文件就新建一个文件
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			prop.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//找出所有的key...		
		//然后比较有没有这个变量，如果没有，就插入一条默认的，如果有，就不动
		if (!prop.containsKey(FILTER_VAULE))
			prop.setProperty(FILTER_VAULE, FILTER_VALUE_VALUE + "");
		//如果还有在此添加if语句

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
