package com.ciotc.feemo.component.tabcomponent.impl;

import com.ciotc.feemo.component.tabcomponent.TabComponent;

public class ViewComponent extends TabComponent {

	public ViewComponent(String path) {

	}

	public String getPath() {
		return doc.getPath();
	}

	public String getSimplePath() {
		return doc.getSimplePath();
	}

}
