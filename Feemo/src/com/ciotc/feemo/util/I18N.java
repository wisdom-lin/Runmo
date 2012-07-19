package com.ciotc.feemo.util;

import java.io.FileReader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class I18N {
	private static ResourceBundle resources;
	static {
		try {

			String fileName = "config\\language.properties";
			Properties prop = new Properties();
			Locale locale = null;
			try {
				prop.load(new FileReader(fileName));
				String language = prop.getProperty("language");
				String country = prop.getProperty("country");
				//System.out.println(language+"_"+country);
				locale = new Locale(language, country);
				// System.out.println(Locale.getDefault());

			} catch (Exception e) {
				locale = null;
			}
			if (locale != null)
				//如果找不到语言的话 它会以本机默认的语言显示
				resources = ResourceBundle.getBundle("feemo", locale);
			else
				resources = ResourceBundle.getBundle("feemo");

		} catch (MissingResourceException mre) {
			//System.err.println("res/teemo.properties not found");
			//JOptionPane.showMessageDialog(null, "未找到语言包，请在安装目录下的程序中选择", "Teemo",JOptionPane.WARNING_MESSAGE); 
			System.exit(1);
		}
	}

	public static String getString(String nm) {
		String str;
		try {
			str = resources.getString(nm);
		} catch (MissingResourceException mre) {
			str = null;
		}
		return str;
	}

	public static String getString(String nm, Locale locale) {
		String str;
		try {
			str = resources.getString(nm);
		} catch (MissingResourceException mre) {
			str = null;
		}
		return str;
	}
}
