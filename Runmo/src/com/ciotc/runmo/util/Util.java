package com.ciotc.runmo.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.ciotc.runmo.setting.Settings;
import com.ciotc.runmo.util.image.FilteredResizableIcon;
import com.ciotc.runmo.util.image.ImageWrapperResizableIcon;
import com.ciotc.runmo.util.image.ResizableIcon;
import com.ciotc.runmo.util.image.ResizableIconUIResource;

public class Util {

	/**
	 * 从文件读出Setting<br>
	 * 使用方法：<br>
	 * readSetting();(此方法只在程序启动时执行一次)<br>
	 * Settings set = Settings.getInstance();<br>
	 * set.getRecordFrame();
	 * @return
	 */
	public static void readSetting() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(Constants.SETTINGS_PATH));
			Settings setting = (Settings) ois.readObject();
			Settings.setInstance(setting);
		} catch (IOException e) {
			e.printStackTrace();

			Settings setting = new Settings();
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(new FileOutputStream(Constants.SETTINGS_PATH));
				oos.writeObject(setting);
			} catch (IOException e2) {
				e2.printStackTrace();
			} finally {
				if (oos != null)
					try {
						oos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
			Settings.setInstance(setting);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将Setting写回到文件<br>
	 * 使用方法<br>
	 * Settings set = Settings.getInstance();<br>
	 * set.setRecordFrame(2000);<br>
	 * writeSetting();(此方法只在程序关闭时执行一次)<br>
	 */
	public static void writeSetting() {

		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(Constants.SETTINGS_PATH));
			oos.writeObject(Settings.getInstance());
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

	}

	/**
	 * 根据屏幕大小返回合适的尺寸
	 * @param widthRate 宽度比例
	 * @param heightRate 高度比例
	 * @return 尺寸大小
	 */
	public static Dimension getSizeDependOnScreen(double widthRate, double heightRate) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((int) (screenSize.width * widthRate), (int) (screenSize.height * heightRate));
	}

	/**
	 * 取得调整后的图片
	 * 大小为Constants.ICON_WIDTH和Constants.ICON_HEIGHT
	 * @param url
	 * @return
	 */
	public static ResizableIcon getResizableIconFromResource(URL url) {
		return ImageWrapperResizableIcon.getIcon(url, new Dimension(Constants.ICON_WIDTH, Constants.ICON_HEIGHT));
	}

	public static ResizableIcon getResizableIconFromResource(URL url, int width, int height) {
		return ImageWrapperResizableIcon.getIcon(url, new Dimension(width, height));
	}

	/**
	 * 取得调整后的图片
	 * 大小为Constants.ICON_WIDTH和Constants.ICON_HEIGHT
	 * @param url
	 * @return
	 */
	public static ResizableIcon getResizableIconFromResource(Class<?> clazz, String path) {
		return ImageWrapperResizableIcon.getIcon(clazz.getResource(path), new Dimension(Constants.ICON_WIDTH, Constants.ICON_HEIGHT));
	}
	
	/**
	 * 取得调整后的图片的变灰图片
	 * 大小为Constants.ICON_WIDTH和Constants.ICON_HEIGHT
	 * @param url
	 * @return
	 */
	public static ResizableIcon getResizableDisableIcon(ResizableIcon icon) {
		return new ResizableIconUIResource(new FilteredResizableIcon(icon, new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null)));
	}

	
	
	/**
	 * 打开对话框
	 * 根据文件对话框，选取文件
	 * @param parent
	 * @param currentDirectory
	 * @param filter
	 *            文件过滤器
	 * @return
	 * @throws
	 */
	public static String chooseOpenFile(Component parent, String currentDirectory) {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		//FileFilter filter1 = TmoFileFilter.getInstance();
		//chooser.addChoosableFileFilter(filter1);
		FileFilter filter2 = FmoFileFilter.getInstance();
		chooser.addChoosableFileFilter(filter2);
		int returnVal = chooser.showOpenDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	/**
	 * 保存对话框
	 * 根据文件对话框，保存文件
	 * @param parent
	 * @param currentDirectory
	 * @param filter
	 *            文件过滤器
	 * @param fileName 文件名
	 * @return
	 * @throws
	 */
	public static String chooseSaveFile(Component parent, String currentDirectory, String fileName) {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		FileFilter filter = FmoFileFilter.getInstance();
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(new File(fileName));
		int returnVal = chooser.showSaveDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}

		return null;
	}

	public static class TmoFileFilter extends FileFilter {
		public String getDescription() {
			return "*.tmo(teemo源文件)";
		}

		public boolean accept(File file) {
			return check(file);
		}

		public boolean check(File file) {
			if (file.isFile())
				return file.getName().toLowerCase().endsWith(Constants.TEEMO_FILE_SUFFIX);
			else
				return true;
		}

		public static TmoFileFilter getInstance() {
			return new TmoFileFilter();
		}
	}

	public static class FmoFileFilter extends FileFilter {
		public String getDescription() {
			return "*.fmo(feemo源文件)";
		}

		public boolean accept(File file) {
			return check(file);
		}

		public boolean check(File file) {
			if (file.isFile())
				return file.getName().toLowerCase().endsWith(Constants.FEEMO_FILE_SUFFIX);
			else
				return true;
		}

		public static FmoFileFilter getInstance() {
			return new FmoFileFilter();
		}
	}
}
