package com.ciotc.feemo.component.tabcomponent;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ciotc.feemo.component.TriggerCloser;

/**
 * 继承JPanel是为了让它有{link JComponent#addPropertyChangeListener(java.beans.PropertyChangeListener)}方法
 * @author Linxiaozhi
 *
 */
public abstract class TabComponent extends JPanel implements ChangeListener ,TriggerCloser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Doc doc;
	protected Model model;
	protected View view;

	protected String title;
	protected String name;

	/**
	 * 窗口开启或关闭状态
	 * 开启true
	 * 关闭false
	 */
	//protected boolean component_window_status = false;

	public Doc getDoc() {
		return doc;
	}

	public Model getModel() {
		return model;
	}

	public View getView() {
		return view;
	}

	public String getTitle() {
		return title;
	}

	public void close() {

	}

	public void open() {

	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
	}

	
}
