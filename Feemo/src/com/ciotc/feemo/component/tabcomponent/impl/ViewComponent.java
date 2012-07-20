package com.ciotc.feemo.component.tabcomponent.impl;

import com.ciotc.feemo.component.tabcomponent.TabComponent;
import com.ciotc.feemo.util.ActionConstants;

public class ViewComponent extends TabComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewComponent(String path) {

	}

	public String getPath() {
		return doc.getPath();
	}

	public String getSimplePath() {
		return doc.getSimplePath();
	}
	
	@Override
	public void open() {
		super.open();
		firePropertyChange(ActionConstants.VIEW_COMPONENT_OPEN, null, this);
	}

	@Override
	public void close() {
		super.close();
		firePropertyChange(ActionConstants.VIEW_COMPONENT_CLOSE, null, this);
	}

	@Override
	public void triggerClose() {
		close();
	}

}
