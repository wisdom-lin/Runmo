package com.ciotc.feemo.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.ciotc.feemo.util.image.ImageWrapperResizableIcon;
import com.ciotc.feemo.util.image.ResizableIcon;

public class Util {
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
	 * 根据文件对话框，选取文件
	 * 
	 * @param parent
	 * @param currentDirectory
	 * @param filter
	 *            文件过滤器
	 * @return
	 * @throws
	 */
	public static String chooseFile(Component parent, String currentDirectory) {
		JFileChooser chooser = new JFileChooser(currentDirectory);
		FileFilter filter = TmoFileFilter.getInstance();
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(parent);
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
				return file.getName().toLowerCase().endsWith(".tmo");
			else
				return true;
		}

		public static TmoFileFilter getInstance() {
			return new TmoFileFilter();
		}
	}
}
