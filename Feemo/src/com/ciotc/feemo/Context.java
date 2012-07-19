package com.ciotc.feemo;

public interface Context {

	/**
	 * 打开一个文件
	 * @param path
	 */
	void openMovie();
	
	/**
	 * 新建一个文件
	 */
	void newMovie();
	
	/**
	 * 关闭录制的影片文件
	 */
	void closeMovie();
	
}
