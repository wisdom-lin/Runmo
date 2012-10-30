package com.ciotc.feemo.component.tabcomponent.impl;

import java.util.Arrays;

import com.ciotc.feemo.component.tabcomponent.Doc;
import com.citoc.feemo.config.Configurge;
/**
 * 使用代理模式
 * @author Linxiaozhi
 *
 */
public class ViewFilterDoc extends Doc implements ViewDocImpl {

	ViewDoc doc;

	public ViewFilterDoc(ViewDoc doc) {
		this.doc = doc;
	}

	/**
	 * 如果低于最低值时，就需要滤掉.用于数值
	 * @param src 原数组
	 * @return 滤值后的数组
	 */
	private int[] doFilter(int[] src) {
		String string = Configurge.getProperty(Configurge.FILTER_VAULE);
		try {
			int value = Integer.parseInt(string.trim());
			int[] dest = Arrays.copyOf(src, src.length);
			for (int i = 0; i < dest.length; i++) {
				if (dest[i] <= value)
					dest[i] = 0;

			}
			return dest;
		} catch (Exception e) {
			e.printStackTrace();
			return src;
		}

	}

	/**
	 * 如果低于最低值时，就需要滤掉.用于单个值
	 * @param src 原数值
	 * @return 低于阈值的话，就返回0.
	 */
	private int doFilter(int f) {
		String string = Configurge.getProperty(Configurge.FILTER_VAULE);
		try {
			int value = Integer.parseInt(string.trim());
			if (f <= value)
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public int[] getSelectedFrame(int frameIndex) {
		return doFilter(doc.getSelectedFrame(frameIndex));
	}

	/**
	 * 
	 * @param in
	 * @param x
	 *                坐标轴上的横坐标
	 * @param y
	 *                坐标轴上的纵坐标
	 * @return
	 */
	public int getForceinXY(int in, int x, int y) {
		return doFilter(doc.getForceinXY(in, x, y));
	}

	@Override
	public boolean readDataFromFile() {
		return doc.readDataFromFile();
	}

	@Override
	public boolean readDataFromTeemoFile() {
		return doc.readDataFromTeemoFile();
	}

	@Override
	public int getFrameLen() {
		return doc.getFrameLen();
	}
	
	@Override
	public String getPath() {
		return doc.getPath();
	}
	
	@Override
	public String getSimplePath() {
		return doc.getSimplePath();
	}
}
