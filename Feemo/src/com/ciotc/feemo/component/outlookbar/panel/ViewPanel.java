package com.ciotc.feemo.component.outlookbar.panel;

import com.ciotc.feemo.OutlookBar;

public class ViewPanel extends OutlookBarPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String className = "OutlookBar.ViewPanel.";
	String title = "view";
	String[] buttons = { "view2D", "images/view2D.png", 
			"viewContour", "images/viewContour.png",
			"view3D", "images/view3D.png",
			"play", "images/play.png", 
			"stop", "images/stop.png",
			"first", "images/previous.png",
			"last","images/next.png",
			"forward","images/forward.png",
			"backward","images/backward.png",
			"speed","images/speed.png"};

	public ViewPanel(OutlookBar tabs) {
		super(tabs);
		constructPanel(className, title, buttons);
	}

}
