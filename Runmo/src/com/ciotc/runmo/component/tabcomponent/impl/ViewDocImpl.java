package com.ciotc.runmo.component.tabcomponent.impl;

public interface ViewDocImpl {
	int[] getSelectedFrame(int frameIndex) ;
	/**
	 * 
	 * @param in
	 * @param x
	 *                坐标轴上的横坐标
	 * @param y
	 *                坐标轴上的纵坐标
	 * @return
	 */
	int getForceinXY(int in, int x, int y) ;
	
	boolean readDataFromFile();
	
	public boolean readDataFromTeemoFile();
	
	public int getFrameLen();
}
