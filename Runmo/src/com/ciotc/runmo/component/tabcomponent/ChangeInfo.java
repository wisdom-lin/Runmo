package com.ciotc.runmo.component.tabcomponent;

/**
 * ChangeEvent携带的信息
 * @author Linxiaozhi
 *
 */
public class ChangeInfo {

	/**
	 * 消息类型
	 */
	String tag;
	/**
	 * 真正的数据
	 */
	Object object;
	public ChangeInfo(String tag, Object object) {
		super();
		this.tag = tag;
		this.object = object;
	}
	public String getTag() {
		return tag;
	}
	public Object getObject() {
		return object;
	}
	
	
}
